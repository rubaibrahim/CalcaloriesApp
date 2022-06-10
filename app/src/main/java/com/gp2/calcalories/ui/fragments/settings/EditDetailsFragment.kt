package com.gp2.calcalories.ui.fragments.settings

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.Validator
import com.gp2.calcalories.databinding.FragmentSettingEditDetailsBinding
import com.gp2.calcalories.remote.model.user.request.PostUserDetailsRequest
import java.text.SimpleDateFormat
import java.util.*

class EditDetailsFragment : BaseFragment() {
    private var _binding: FragmentSettingEditDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSettingEditDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session?.getUser()?.let {
           userDetails = PostUserDetailsRequest(
                date_of_birth = it.details?.date_of_birth ?: "",
                gender = it.details?.gender ?: "F",
                height = it.details?.height ?: 0f,
                weight = it.details?.weight ?: 0f,
                vitamin_protein = it.details?.vitamin_protein ?: 0f,
                vitamin_iron = it.details?.vitamin_iron ?: 0f,
                vitamin_a = it.details?.vitamin_a ?: 0f,
                update_vitamin = true
            )

            binding.txtBirthDate.text = it.details?.date_of_birth.toString()
            if (it.details?.gender.equals("M"))
                binding.radioMale.isChecked = true
            else
                binding.radioFemale.isChecked = true

            if (it.details?.height!! >= 1) binding.txtHeight.setText(it.details.height.toString())
            if (it.details.weight >= 1) binding.txtWeight.setText(it.details.weight.toString())
        }

        binding.btnSave.setOnClickListener {
            saveData()
        }
        binding.txtBirthDate.setOnClickListener {
            selectBirthDate()
        }
        binding.radioMale.setOnClickListener {
           userDetails.gender = "M"
        }
        binding.radioFemale.setOnClickListener {
           userDetails.gender = "F"
        }
    }

    private var userDetails: PostUserDetailsRequest = PostUserDetailsRequest()
    private val validator = Validator.getInstance()

    private fun saveData() {
        if (validator.isEmpty(binding.txtHeight, getString(R.string.height)) ||
            validator.isEmpty(binding.txtWeight, getString(R.string.weight))
        ) {
            return
        } else {
           userDetails.date_of_birth = binding.txtBirthDate.text.toString()
           userDetails.height = binding.txtHeight.text.toString().toFloat()
           userDetails.weight = binding.txtWeight.text.toString().toFloat()

            repository?.updateUserDetails(userDetails){
                when (it.status) {
                    HTTPCode.Success -> {
                        Alert.toast(requireContext(), R.string.saved_successfully)
                        // Save new user data to session
                        session?.saveUser(it.user)
                        // close page
                        findNavController().popBackStack()
                    }
                    HTTPCode.ValidatorError -> {
                        checkValidation(it.errors)
                    }
                }
            }
        }
    }

    private fun checkValidation(errors: HashMap<String, Any>) {
        if (errors.size > 0) {
            validator.hasError(binding.txtHeight, "height", errors)
            validator.hasError(binding.txtWeight, "weight", errors)
        }
    }

    // ================================= Birth Date
    private fun selectBirthDate() {
        try {
            val c = Calendar.getInstance()

            val minCal: Calendar = GregorianCalendar(c[Calendar.YEAR] - 50,
                c[Calendar.MONTH],
                c[Calendar.DAY_OF_MONTH])
            val maxCal: Calendar = GregorianCalendar(c[Calendar.YEAR] - 18,
                c[Calendar.MONTH],
                c[Calendar.DAY_OF_MONTH])

            // Set BirthDate
            val currentBirthDate: String = binding.txtBirthDate.text.toString()
            if (currentBirthDate != "") {
                if (currentBirthDate.contains("-")) {
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    c.time = format.parse(currentBirthDate)!!
                } else {
                    val format = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                    c.time = format.parse(currentBirthDate)!!
                }
            }

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    binding.txtBirthDate.text =
                        String.format(Locale.ENGLISH, "%d-%d-%d", year, month + 1, dayOfMonth)
                },
                c[Calendar.YEAR],
                c[Calendar.MONTH],
                c[Calendar.DAY_OF_MONTH]
            )

            //Minimum & Maximum date
            datePickerDialog.datePicker.minDate = minCal.timeInMillis
            datePickerDialog.datePicker.maxDate = maxCal.timeInMillis

            datePickerDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}