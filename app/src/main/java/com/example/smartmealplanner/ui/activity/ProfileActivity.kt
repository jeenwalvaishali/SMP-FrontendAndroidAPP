package com.example.smartmealplanner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.api.TokenManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tokenManager = TokenManager(this)

        setupHeader()
        setupProfileInfo()
        setupSettings()
        setupLogout()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }
        findViewById<TextView>(R.id.titleText).text = "Profile"
    }

    private fun setupProfileInfo() {
        // Load actual user data from TokenManager
        val userName = tokenManager.getUserName() ?: "User"
        val userEmail = tokenManager.getUserEmail() ?: "user@example.com"

        findViewById<TextView>(R.id.userName).text = userName
        findViewById<TextView>(R.id.userEmail).text = userEmail
        
        findViewById<MaterialButton>(R.id.editProfileButton).setOnClickListener {
            // TODO: Implement Edit Profile
        }
    }

    private fun setupSettings() {
        // Accessing included layouts
        val dietTypeView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.settingDietType)
        dietTypeView.findViewById<TextView>(R.id.settingTitle).text = "My Weekly Plan"
        dietTypeView.findViewById<ImageView>(R.id.settingIcon).setImageResource(R.drawable.ic_restaurant)

        // Open SavedMealPlanActivity when "My Weekly Plan" is clicked
        dietTypeView.setOnClickListener {
            val intent = Intent(this, SavedMealPlanActivity::class.java)
            startActivity(intent)
        }

        val passwordView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.settingChangePassword)
        passwordView.findViewById<TextView>(R.id.settingTitle).text = "Change Password"
        passwordView.findViewById<ImageView>(R.id.settingIcon).setImageResource(R.drawable.ic_settings)

        val notificationsView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.settingNotifications)
        notificationsView.findViewById<TextView>(R.id.settingTitle).text = "Notifications"
        notificationsView.findViewById<ImageView>(R.id.settingIcon).setImageResource(R.drawable.ic_help)
    }

    private fun setupLogout() {
        findViewById<MaterialButton>(R.id.logoutButton).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            tokenManager.clearToken()
            
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
