package com.example.smartmealplanner.ui.auth


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import com.example.smartmealplanner.ui.activity.HomeActivity
import com.example.smartmealplanner.ui.viewmodel.LoginViewModel
import com.example.smartmealplanner.ui.viewmodel.LoginViewModelFactory

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoRegister: TextView

    private lateinit var viewModel: LoginViewModel
    private lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupViewModel()
        setupListeners()
    }

    private fun initViews(view: View) {
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnGoRegister = view.findViewById(R.id.btnGoRegister)
    }

    private fun setupViewModel() {
        tokenManager = TokenManager(requireContext())
        val authApi = RetrofitClient.create(AuthApi::class.java) // no token needed for login/register
        val repo = AuthRepository(authApi)
        //val repo = AuthRepository(RetrofitClient.create()) // assuming AuthApi has companion create()
        viewModel = ViewModelProvider(this, LoginViewModelFactory(repo, tokenManager))
            .get(LoginViewModel::class.java)

        viewModel.loginState.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            result.onFailure {
                Toast.makeText(requireContext(), "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }
}
