package com.samplevault.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.samplevault.databinding.ActivityUploadresultsBinding

class UploadResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadresultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadresultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUploadResults.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/pdf"
            startActivityForResult(Intent.createChooser(intent, "Select Results PDF"), 1)
        }

        binding.btnUploadRaw.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"

            // Allow Word and Excel files
            val mimetypes = arrayOf(
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            )
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)

            startActivityForResult(Intent.createChooser(intent, "Select Raw Data File"), 2)
        }

        binding.btnSubmitFinal.setOnClickListener {
            Toast.makeText(this, "Sample Submitted for Approval!", Toast.LENGTH_LONG).show()

            // Go back to the Dashboard after submitting!
            val intent = Intent(this, ScholarDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clears the back history
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "File Attached Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}