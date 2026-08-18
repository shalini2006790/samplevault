package com.samplevault.activities
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.samplevault.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roles = arrayOf("Research Scholar", "Lab Head")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        binding.roleSpinner.adapter = adapter

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val selectedRole = binding.roleSpinner.selectedItem.toString()
            if (selectedRole == "Lab Head") {
                startActivity(Intent(this, LabHeadDashboardActivity::class.java))
            } else {
                startActivity(Intent(this, ScholarDashboardActivity::class.java))
            }
        }
    }
}