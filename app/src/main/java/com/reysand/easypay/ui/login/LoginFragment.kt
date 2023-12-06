package com.reysand.easypay.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.reysand.easypay.databinding.FragmentLoginBinding
import com.reysand.easypay.ui.EasyPayViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val easyPayViewModel: EasyPayViewModel by activityViewModels { EasyPayViewModel.provideFactory() }

    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val usernameEditText: EditText = binding.usernameEditText
        val passwordEditText: EditText = binding.passwordEditText

        val loginButton: Button = binding.loginButton
        easyPayViewModel.currentLogin.observe(viewLifecycleOwner) { login ->
            loginButton.isEnabled = login == null
        }
        loginButton.setOnClickListener {
            loginViewModel.performLogin(
                EasyPayViewModel().easyPayService,
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        val logoutButton: Button = binding.logoutButton
        logoutButton.isEnabled = false
        easyPayViewModel.currentLogin.observe(viewLifecycleOwner) { login ->
            logoutButton.isEnabled = login != null
        }
        logoutButton.setOnClickListener {
            loginViewModel.logout()
            easyPayViewModel.resetCurrentLogin()
            usernameEditText.text.clear()
            passwordEditText.text.clear()
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Login success: ${result.token}",
                        Toast.LENGTH_SHORT
                    ).show()
                    token = result.token
                    easyPayViewModel.setCurrentLogin(usernameEditText.text.toString(), token ?: "")
                }

                is LoginResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Login failed: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}