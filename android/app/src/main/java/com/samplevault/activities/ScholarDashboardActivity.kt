package com.samplevault.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.samplevault.databinding.ActivityScholardashboardBinding
import com.samplevault.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScholarDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScholardashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScholardashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddSample.setOnClickListener {
            startActivity(Intent(this, AddSampleActivity::class.java))
        }
    }

    // Every time this screen opens, it asks the backend for the latest data!
    override fun onResume() {
        super.onResume()
        loadSamplesFromBackend()
    }

    private fun loadSamplesFromBackend() {
        // Show loading state (hide empty state temporarily)
        binding.tvEmptyState.text = "Loading data from server..."

        lifecycleScope.launch {
            try {
                // Call the Python FastAPI Server!
                val response = withContext(Dispatchers.IO) { ApiClient.api.getSamples() }


                if (response.isSuccessful) {
                    val allSamples = response.body() ?: emptyList()

                    if (allSamples.isEmpty()) {
                        binding.tvEmptyState.text = "No samples submitted yet.\nClick above to create one."
                        binding.tvEmptyState.visibility = View.VISIBLE
                    } else {
                        // We have data! Hide the empty message and draw the list
                        binding.tvEmptyState.visibility = View.GONE
                        binding.samplesContainer.removeAllViews() // Clear old list

                        allSamples.forEach { sample ->
                            val tv = TextView(this@ScholarDashboardActivity)
                            tv.text = "ID: ${sample.sample_id} | Type: ${sample.sample_type}\nStatus: ${sample.status}"
                            tv.setTextColor(Color.WHITE)
                            tv.setPadding(30, 30, 30, 30)
                            tv.textSize = 16f
                            tv.setBackgroundColor(Color.parseColor("#1c2541")) // Dark blue card

                            // Add a margin between items
                            val params = android.widget.LinearLayout.LayoutParams(
                                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(0, 0, 0, 20)
                            tv.layoutParams = params

                            // Add it to the screen
                            binding.samplesContainer.addView(tv)
                        }
                    }
                }
            } catch (e: Exception) {
                binding.tvEmptyState.text = "Error connecting to Python backend.\nIs the server running?"
            }
        }
    }
}