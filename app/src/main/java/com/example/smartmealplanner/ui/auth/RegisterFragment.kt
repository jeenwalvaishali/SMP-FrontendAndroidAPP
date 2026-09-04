package com.example.smartmealplanner.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.api.AuthApi
import com.example.smartmealplanner.data.api.RetrofitClient
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.repository.AuthRepository
import com.example.smartmealplanner.ui.viewmodel.RegisterViewModel
import com.example.smartmealplanner.ui.viewmodel.RegisterViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var btnGoLogin: TextView

    private lateinit var viewModel: RegisterViewModel
    private lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupViewModel()
        setupListeners()
    }

    private fun initViews(view: View) {
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        btnGoLogin = view.findViewById(R.id.btnGoLogin)
    }

    private fun setupViewModel() {
        tokenManager = TokenManager(requireContext())
        val authApi = RetrofitClient.create(AuthApi::class.java)
        val repo = AuthRepository(authApi)
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(repo, tokenManager))
            .get(RegisterViewModel::class.java)

        viewModel.registerState.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Register Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }

            result.onFailure {
                Toast.makeText(requireContext(), "Register Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(name, email, password)
        }

        btnGoLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}