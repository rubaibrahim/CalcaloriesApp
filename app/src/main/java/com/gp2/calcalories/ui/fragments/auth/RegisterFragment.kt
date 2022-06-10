package com.gp2.calcalories.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gp2.calcalories.BuildConfig
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.util.Validator
import com.gp2.calcalories.databinding.FragmentAuthRegisterBinding
import com.gp2.calcalories.remote.model.user.request.PostUserRequest

class RegisterFragment : BaseFragment() {
    private var _binding: FragmentAuthRegisterBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentAuthRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            checkDataAndRegister()
        }
        binding.btnLogin.setOnClickListener {
            // go to login page
            findNavController().popBackStack()
        }

//        if(BuildConfig.DEBUG){
//            binding.txtName.editText?.setText("Test ")
//            binding.txtEmail.editText?.setText("@email.com")
//            binding.txtPassword.editText?.setText("123456")
//        }
    }

    private val validator = Validator.getInstance()
    private fun checkDataAndRegister() {
        if (validator.isEmpty(binding.txtName, getString(R.string.please_enter_your_name)) ||
            validator.isEmpty(binding.txtEmail, getString(R.string.please_enter_your_email)) ||
            validator.emailValid(binding.txtEmail, getString(R.string.please_enter_your_email)) ||
            validator.isEmpty(binding.txtPassword, getString(R.string.please_enter_your_password))) {
            return
        } else {
            val request = PostUserRequest(
                name = binding.txtName.editText?.text.toString(),
                email = binding.txtEmail.editText?.text.toString(),
                password = binding.txtPassword.editText?.text.toString(),
            )

            repository?.authRegister(request){
                when (it.status) {
                    HTTPCode.Success -> {
                        session?.saveUser(it.user)
                        exit()
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


    private fun exit() {
        findNavController().popBackStack()
        if (session?.getUser() != null) {
            // go to home page
            findNavController().navigate(LaunchFragmentDirections.navigateToPlanFragment())
        }
    }
}