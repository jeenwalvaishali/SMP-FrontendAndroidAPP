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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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
        // Here you would typically load user data from a ViewModel or TokenManager
        findViewById<TextView>(R.id.userName).text = "Vaishali"
        findViewById<TextView>(R.id.userEmail).text = "vaishali@example.com"
        
        findViewById<MaterialButton>(R.id.editProfileButton).setOnClickListener {
            // TODO: Implement Edit Profile
        }
    }

    private fun setupSettings() {
        // Accessing included layouts
        val dietTypeView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.settingDietType)
        dietTypeView.findViewById<TextView>(R.id.settingTitle).text = "Diet Type"
        dietTypeView.findViewById<ImageView>(R.id.settingIcon).setImageResource(R.drawable.ic_menu) // Replace with better icon if available

        val allergiesView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.settingAllergies)
        allergiesView.findViewById<TextView>(R.id.settingTitle).text = "Allergies"
        allergiesView.findViewById<ImageView>(R.id.settingIcon).setImageResource(R.drawable.ic_filter) // Replace with better icon if available

        val passwordView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.settingChangePassword)
        passwordView.findViewById<TextView>(R.id.settingTitle).text = "Change Password"
        passwordView.findViewById<ImageView>(R.id.settingIcon).setImageResource(R.drawable.ic_settings)

        val notificationsView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.settingNotifications)
        notificationsView.findViewById<TextView>(R.id.settingTitle).text = "Notifications"
        notificationsView.findViewById<ImageView>(R.id.settingIcon).setImageResource(R.drawable.ic_help) // Replace with better icon if available
    }

    private fun setupLogout() {
        findViewById<MaterialButton>(R.id.logoutButton).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            val tokenManager = TokenManager(this@ProfileActivity)
            tokenManager.clearToken()
            
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}