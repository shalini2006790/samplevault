package com.samplevault.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.samplevault.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the Role Dropdown
        val roles = arrayOf("Research Scholar", "Lab Head")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        binding.roleRegSpinner.adapter = adapter

        // Go back to Login when clicked
        binding.tvBackToLogin.setOnClickListener {
            finish() // This safely closes the Registration screen and reveals the Login screen behind it
        }

        // Register button logic (for now just goes to login)
        binding.btnRegister.setOnClickListener {
            finish()
        }
    }
}