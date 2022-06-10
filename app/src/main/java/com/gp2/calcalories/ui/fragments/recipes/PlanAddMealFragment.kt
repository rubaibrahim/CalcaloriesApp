package com.gp2.calcalories.ui.fragments.recipes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.config.getNavigationResultLiveData
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.util.*
import com.gp2.calcalories.databinding.FragmentPlanAddMealBinding
import com.gp2.calcalories.remote.model.meal.entity.MealRecipe
import com.gp2.calcalories.remote.model.meal.request.PostMealTypeRequest
import com.gp2.calcalories.remote.model.plan.request.PostUserPlanMealRequest
import com.gp2.calcalories.ui.fragments.home.NotificationsFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.Exception
import java.util.*


class PlanAddMealFragment : BaseFragment() {
    private var _binding: FragmentPlanAddMealBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentPlanAddMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val args by navArgs<PlanAddMealFragmentArgs>()
    var request: PostUserPlanMealRequest = PostUserPlanMealRequest()
    private val validator = Validator.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            btnSearchByImage.setOnClickListener {
                pickFromGallery()
            }
            btnSelectRecipe.setOnClickListener {
                binding.webView.clearCache(true)
                // View recipes list to select
                findNavController().navigate(NotificationsFragmentDirections
                    .navigateToRecipesSearchFragment("", true))
            }

            txtName.setStartIconOnClickListener {
                val name = binding.txtName.editText?.text.toString()
                searchByName(name)
            }
            txtName.editText?.setOnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    val name = binding.txtName.editText?.text.toString()
                    searchByName(name)
                }
                false
            }

            btnSave.setOnClickListener {
                if (validator.isEmpty(binding.txtName, getString(R.string.meal_name))) {
                    return@setOnClickListener
                }
                repository?.postUserPlanMeal(request) {
                    when (it.status) {
                        HTTPCode.Success -> {
                            Alert.toast(requireContext(), R.string.saved_successfully)
                            // close page
                            findNavController().popBackStack()
                        }
                        HTTPCode.ValidatorError -> {
                            if (it.errors.size > 0) {
                                if (!validator.hasError(binding.txtName, "name", it.errors)) {
                                    Alert.alert(requireActivity(), it.errors.toString())
                                }
                            }
                        }
                    }
                }
            }
        }

        getNavigationResultLiveData<MealRecipe>("recipe")?.observe(viewLifecycleOwner) {
            setRequest(it.name, it.calories, it.vitamin_protein, it.vitamin_iron, it.vitamin_a)
        }

        request.user_plan_id = args.planId
    }

    var name: String = ""
    private fun searchByName(name: String) {
        this.name = name

        binding.txtName.editText?.setText(name)
        progress?.show()

        searchByGoogleImage("https://www.myfitnesspal.com/food/search?page=1&query=$name", false)
    }

    private var isAlertVisible: Boolean = false
    private fun setRequest(
        name: String,
        calories: Int,
        protein: Float,
        iron: Float,
        vitamin_a: Float,
    ) {

        request.name = name
        request.calories = calories
        request.vitamin_protein = protein
        request.vitamin_iron = iron
        request.vitamin_a = vitamin_a
        request.updated_at = Utility.getCurrentDate("yyyy-MM-dd")

        if (!isDetached) {
            progress?.hide()

            val totals: String = String.format(Locale.ENGLISH,
                "%s \nCalories: %s \nProtein: %s, Iron: %s, Vitamin A: %s",
                name,
                calories.toString(),
                protein.toString(),
                iron.toString(),
                vitamin_a.toString())

            binding.apply {
                txtName.editText?.setText(name)
                txtTotals.text = totals
            }

            if (!isAlertVisible) {

                isAlertVisible = true
                Alert.alert(requireActivity(), getString(R.string.add_meal), totals,
                    approved = {
                        isAlertVisible = false
                        // Save meal to database
                        binding.btnSave.performClick()
                    },
                    canceled = {
                        isAlertVisible = false
                        // remove old search from textView
                        binding.txtName.editText?.setText("")
                    }, approved_title = R.string.save)
            }
        }
    }


    var isGoogleSearch: Boolean = true

    //  GET IMAGE NAME BY GOOGLE SEARCH ============================================================
    @SuppressLint("JavascriptInterface")
    private fun searchByGoogleImage(url: String, isGoogleSearch: Boolean = true) {
        this.isGoogleSearch = isGoogleSearch
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            webViewClient = WebClient()
            addJavascriptInterface(this@PlanAddMealFragment, "PlanAddMealFragment");
            loadUrl(url)
        }
    }

    class WebClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            //super.onPageStarted(view, url, favicon)
            Log.d("WebView", "onPageStarted: $url")
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.d("WebView", "onPageFinished: $url")
            view.loadUrl("javascript:PlanAddMealFragment.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        }
    }

    @JavascriptInterface
    fun showHTML(html: String?) {
        Log.d("WebView", "showHTML: $html")
        html?.let {
            if (isGoogleSearch) {

                val search1 = "Possible related search"
                if (html.contains(search1)) {
                    val result = html.substring(html.indexOf(search1), html.indexOf(search1) + 50)
                    Log.d("WebView 1", result)
                    if (result.contains("<")) {
                        val name = result.substring(0, result.indexOf("<"))
                            .replace(search1, "")
                            .replace("&nbsp;", "")
                        Log.d("WebView 1 name", name)
                        runBlocking(Dispatchers.Main) { searchByName(name) }
                        return@let
                    }
                }

                val search2 = "نتائج عن"
                if (html.contains(search2)) {

                    val result = html.substring(html.indexOf(search2), html.indexOf(search2) + 60)
                        .replace("<span dir=\"ltr\">", "")
                    Log.d("WebView 2", result)
                    if (result.contains("<")) {
                        val name = result
                            .substring(0, result.indexOf("<"))
                            .replace(search2, "")
                            .replace("&nbsp;", "")
                            .replace("اكلات ", "")
                            .replace("صوره ل", "")
                        Log.d("WebView 2 name", name)
                        runBlocking(Dispatchers.Main) { searchByName(name) }
                        return@let
                    }
                }

                val search3 = "results for"
                if (html.toLowerCase().contains(search3)) {

                    val result = html.substring(html.toLowerCase().indexOf(search3),
                        html.toLowerCase().indexOf(search3) + 60)
                        .replace("<span dir=\"ltr\">", "")
                    Log.d("WebView 2", result)
                    if (result.contains("<")) {
                        val name = result.toLowerCase()
                            .substring(0, result.indexOf("<"))
                            .replace(search3, "")
                            .replace("&nbsp;", "")
                            .replace("اكلات ", "")
                            .replace("صوره ل", "")
                        Log.d("WebView 3 name", name)
                        runBlocking(Dispatchers.Main) { searchByName(name) }
                        return@let
                    }
                }


            } else {
                if (it.contains(">Calories:")) {
                    val caloriesIndex = it.indexOf(">Calories:")

                    if (it.length >= caloriesIndex + 200) {
                        val string = it.substring(caloriesIndex, caloriesIndex + 500)
                        Alert.log("string", string)

                        val mealData: MealData = string.getMealData(name)
                        Alert.log("mealData", mealData.toString())


                        runBlocking(Dispatchers.Main) {
                            setRequest(name,
                                mealData.calories,
                                mealData.protein,
                                mealData.carbs,
                                mealData.fat)
                        }
                        //callback(mealData)
                    }
                }
            }
        }
        runBlocking(Dispatchers.Main) {
            progress?.hide()
        }
    }


    //  SELECT IMAGE FROM GALLERY OR CAMERA ========================================================
    private fun pickFromGallery() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
            return
        }
        openImageIntent()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // If request is cancelled, the result arrays are empty.
            permissions.entries.forEach {
                if (it.value) {
                    openImageIntent()
                    return@registerForActivityResult
                }
            }
        }
    private var outputFileUri: Uri? = null
    private fun openImageIntent() {
        // Filesystem.
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        // galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.action = Intent.ACTION_OPEN_DOCUMENT
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)


        // Chooser of filesystem options.
        val chooserIntent =
            Intent.createChooser(galleryIntent, getString(R.string.select_picture))

        // Determine Url of camera image to save.
        val root = File(Environment.getExternalStorageDirectory()
            .toString() + File.separator + "Pictures" + File.separator)
        val success = root.exists() || root.mkdir()
        if (success) {
            val fName = "img_" + System.currentTimeMillis() + ".jpg"
            outputFileUri = Uri.fromFile(File(root, fName))

            // Camera.
            val cameraIntents: MutableList<Intent> = ArrayList()
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val packageManager: PackageManager = requireActivity().packageManager
            val listCam = packageManager.queryIntentActivities(captureIntent, 0)
            for (res: ResolveInfo in listCam) {
                val packageName = res.activityInfo.packageName
                val intent = Intent(captureIntent)
                intent.component = ComponentName(packageName, res.activityInfo.name)
                intent.setPackage(packageName)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                cameraIntents.add(intent)
            }

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toTypedArray())
        }
        galleryIntent.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

//        startActivityForResult(chooserIntent, REQUEST_SELECT_PICTURE)
        requestLauncher.launch(chooserIntent)
    }

    private val requestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val isCamera: Boolean = if (result.data == null) true else {
                        val action = result.data?.action
                        if (action == null) false else (action == MediaStore.ACTION_IMAGE_CAPTURE)
                    }
                    val selectedImageUri = if (isCamera) outputFileUri else result.data?.data


                    if (selectedImageUri != null) {
                        try {
                            binding.image.setImageURI(selectedImageUri, requireContext())
                            binding.image.visibility = View.VISIBLE
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Alert.alert(requireContext(), "Exception 0", e.localizedMessage)
                        }

                        try {
                            val image = ImageUtils.getBitmapFromByte(
                                ImageUtils.compressImage(requireContext(), selectedImageUri))
                            val base64Image = ImageUtils.getStringFromBitmap(image)

                            repository?.postMealType(PostMealTypeRequest(base64Image)) {
                                if (it.status == HTTPCode.Success) {
                                    val url = it.data.google_url
                                    if (url.startsWith("http"))
                                        searchByGoogleImage(url)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Alert.alert(requireContext(), "Exception 1", e.localizedMessage)
                        }
                    } else
                        Alert.alert(requireContext(),"Exception 2",
                            getString(R.string.cannot_retrieve_cropped_image))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Alert.alert(requireContext(), "Exception 3", e.localizedMessage)
            }
        }

}


data class MealData(
    var name: String = "",
    var calories: Int = 0,
    var carbs: Float = 0f,
    var fat: Float = 0f,
    var protein: Float = 0f,
)

fun String.getMealData(mealName: String): MealData {
    val mealData = MealData(mealName)

    if (contains(">Calories:")) {
        val calories = substring(indexOf(">Calories:"), indexOf(">Calories:") + 25)
        mealData.calories = calories.getNumber().toInt()
        Alert.log("Calories", calories + "    =    " + calories.getNumber())
    }

    if (contains(">Carbs:")) {
        val carbs = substring(indexOf(">Carbs:"), indexOf(">Carbs:") + 15)
        mealData.carbs = carbs.getNumber().toFloat()
        Alert.log("Carbs", carbs + "    =    " + carbs.getNumber())
    }

    if (contains(">Fat:")) {
        val fat = substring(indexOf(">Fat:"), indexOf(">Fat:") + 15)
        mealData.fat = fat.getNumber().toFloat()
        Alert.log("Fat", fat + "    =    " + fat.getNumber())
    }

    if (contains(">Protein:")) {
        val protein = substring(indexOf(">Protein:"), indexOf(">Protein:") + 15)
        mealData.protein = protein.getNumber().toFloat()
        Alert.log("Protein", protein + "    =    " + protein.getNumber())
    }
    return mealData
}

fun String.getNumber(): String {
//  return this.replace("[^0-9]".toRegex(), "")
    return this.filter { it.isDigit() }
}
