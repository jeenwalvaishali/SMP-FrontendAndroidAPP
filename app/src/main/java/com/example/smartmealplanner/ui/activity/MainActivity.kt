package com.example.smartmealplanner.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.api.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(this)

        lifecycleScope.launch {
            val token = tokenManager.tokenFlow.first()

            if (token != null) {
                // User is logged in, navigate to HomeActivity
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // User is NOT logged in, show Login (via activity_main layout which hosts NavHostFragment)
                setContentView(R.layout.activity_main)
            }
        }
    }
}