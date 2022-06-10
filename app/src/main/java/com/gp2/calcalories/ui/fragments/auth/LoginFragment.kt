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
import com.gp2.calcalories.databinding.FragmentAuthLoginBinding
import com.gp2.calcalories.remote.model.user.request.AuthRequest

class LoginFragment : BaseFragment() {
    private var _binding: FragmentAuthLoginBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            checkDataAndLogin()
        }
        binding.btnRegister.setOnClickListener {
            // go to register page
            findNavController().navigate(LoginFragmentDirections.navigateToRegisterFragment())
        }

//        if (BuildConfig.DEBUG) {
//            binding.txtEmail.editText?.setText("test@email.com")
//            binding.txtPassword.editText?.setText("123456")
//        }
    }

    private val validator = Validator.getInstance()
    private fun checkDataAndLogin() {
        if (validator.isEmpty(binding.txtEmail, getString(R.string.please_enter_your_email)) ||
            validator.emailValid(binding.txtEmail, getString(R.string.please_enter_your_email)) ||
            validator.isEmpty(binding.txtPassword, getString(R.string.please_enter_your_password))
        ) {
            return
        } else {

            val request = AuthRequest(
                email = binding.txtEmail.editText?.text.toString(),
                password = binding.txtPassword.editText?.text.toString(),
            )

            repository?.authLogin(request) {
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