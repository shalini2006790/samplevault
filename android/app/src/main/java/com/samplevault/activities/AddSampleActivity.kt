package com.samplevault.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.samplevault.databinding.ActivityAddsampleBinding
import com.samplevault.models.SampleCreate
import com.samplevault.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddSampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddsampleBinding
    private val PICK_FILE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddsampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Upload PDF Button
        binding.btnUploadPdf.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/pdf"
            startActivityForResult(intent, PICK_FILE_REQUEST)
        }

        // Manual Form Submit Button
        binding.btnSubmitManual.setOnClickListener {

            // 1. Gather all the data you typed into the boxes!
            val sampleData = SampleCreate(
                sample_type = binding.etType.text.toString(),
                volume = binding.etVolume.text.toString().toDoubleOrNull() ?: 0.0,
                number_of_samples = binding.etCount.text.toString().toIntOrNull() ?: 1,
                depositor_name = binding.etName.text.toString(),
                institution = binding.etInst.text.toString(),
                contact_number = binding.etContact.text.toString(),
                email_id = binding.etEmail.text.toString(),
                date_of_deposit = binding.etDate.text.toString(),
                storage_options = binding.etStorage.text.toString(),
                experiment_details = binding.etExp.text.toString()
            )

            // 2. Send it to the Python Database!
            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        ApiClient.api.createSample(sampleData)
                    }
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddSampleActivity, "Saved to Database!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddSampleActivity, ProcessingDetailsActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@AddSampleActivity, "Backend Error. Did you turn off Auth?", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AddSampleActivity, "Network Error! Is Python running?", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "Document Uploaded Successfully!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ProcessingDetailsActivity::class.java))
            finish()
        }
    }
}