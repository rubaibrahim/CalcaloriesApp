package com.gp2.calcalories.ui.fragments.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.common.util.Validator
import com.gp2.calcalories.databinding.FragmentSettingEditAccountBinding
import com.gp2.calcalories.remote.model.user.request.PostUserRequest

class EditAccountFragment : BaseFragment() {
    private var _binding: FragmentSettingEditAccountBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSettingEditAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session?.getUser()?.let {
            binding.txtName.editText?.setText(it.name)
            binding.txtEmail.editText?.setText(it.email)
        }

        binding.btnSave.setOnClickListener { saveData() }
        binding.btnLogout.setOnClickListener { logoutFromAPI() }
    }

    private val validator = Validator.getInstance()
    private fun saveData() {
        if (validator.isEmpty(binding.txtName, getString(R.string.please_enter_your_name)) ||
            validator.isEmpty(binding.txtEmail, getString(R.string.please_enter_your_email)) ||
            validator.emailValid(binding.txtEmail, getString(R.string.please_enter_your_email))
        //|| validator.isEmpty(binding.txtPassword, getString(R.string.please_enter_your_password))
        ) {
            return
        } else {
            val request = PostUserRequest(
                name = binding.txtName.editText?.text.toString(),
                email = binding.txtEmail.editText?.text.toString(),
                password = binding.txtPassword.editText?.text.toString(),
                fcm_token = session?.getFirebaseToken().toString()
            )

            repository?.updateUser(session?.getUser()?.id ?: 0,request){
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
            validator.hasError(binding.txtName, "name", errors)
            validator.hasError(binding.txtEmail, "email", errors)
            validator.hasError(binding.txtPassword, "password", errors)
        }
    }

    // ================================= logout
    private fun logoutFromAPI() {
        if (session?.getUser() == null) return

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout)
            .setMessage(R.string.do_you_want_to_logout_of_your_account)
            .setCancelable(false)
            .setPositiveButton(R.string.logout) { _: DialogInterface?, _: Int ->
                repository?.authLogout{
                    session?.saveUser(null)
                    //findNavController().popBackStack()
                    Utility.startAppAgain(requireContext())
                }
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }
}