package com.samplevault.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.samplevault.databinding.ActivityProcessingdetailsBinding

class ProcessingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProcessingdetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProcessingdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitProc.setOnClickListener {
            // In a real app, this is where we would send the 4 text fields to the FastAPI backend!

            Toast.makeText(this, "Processing details securely saved!", Toast.LENGTH_SHORT).show()

            // Navigate to the next step in the flowchart
            startActivity(Intent(this, UploadResultsActivity::class.java))
            finish()
        }
    }
}