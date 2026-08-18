package com.samplevault.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.samplevault.databinding.ActivityLabheaddashboardBinding
import com.samplevault.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.samplevault.models.Sample

class LabHeadDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLabheaddashboardBinding
    private var currentTab = "APPROVALS"
    private var allSamplesList: List<Sample> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabheaddashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tab Click Listeners
        binding.btnTabApprovals.setOnClickListener {
            currentTab = "APPROVALS"
            binding.btnTabApprovals.setBackgroundColor(Color.parseColor("#0ea5e9"))
            binding.btnTabApprovals.setTextColor(Color.WHITE)
            binding.btnTabEmails.setBackgroundColor(Color.parseColor("#1c2541"))
            binding.btnTabEmails.setTextColor(Color.parseColor("#94a3b8"))
            binding.tvSectionTitle.text = "Pending Approvals Queue:"
            renderList()
        }

        binding.btnTabEmails.setOnClickListener {
            currentTab = "EMAILS"
            binding.btnTabEmails.setBackgroundColor(Color.parseColor("#0ea5e9"))
            binding.btnTabEmails.setTextColor(Color.WHITE)
            binding.btnTabApprovals.setBackgroundColor(Color.parseColor("#1c2541"))
            binding.btnTabApprovals.setTextColor(Color.parseColor("#94a3b8"))
            binding.tvSectionTitle.text = "Approved Samples (Needs Email):"
            renderList()
        }
    }

    override fun onResume() {
        super.onResume()
        loadLabHeadDashboard()
    }

    private fun loadLabHeadDashboard() {
        lifecycleScope.launch {
            try {
                val analyticsRes = withContext(Dispatchers.IO) { ApiClient.api.getAnalytics() }
                if (analyticsRes.isSuccessful) {
                    val stats = analyticsRes.body()
                    binding.tvTotal.text = "Total:\n${stats?.total_samples ?: 0}"
                    binding.tvApproved.text = "Approved:\n${stats?.approved_samples ?: 0}"
                    binding.tvPending.text = "Pending:\n${stats?.pending_samples ?: 0}"
                }

                val samplesRes = withContext(Dispatchers.IO) { ApiClient.api.getSamples() }
                if (samplesRes.isSuccessful) {
                    allSamplesList = samplesRes.body() ?: emptyList()
                    renderList()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LabHeadDashboardActivity, "Error connecting to server!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderList() {
        binding.labSamplesContainer.removeAllViews()

        val filteredList = if (currentTab == "APPROVALS") {
            allSamplesList.filter { it.status == "PROCESSING" || it.status == "UNDER_REVIEW" || it.status == "DRAFT" || it.status == "SUBMITTED"}
        } else {
            allSamplesList.filter { it.status == "APPROVED" || it.status == "RESULT_SENT" }
        }

        if (filteredList.isEmpty()) {
            val tvEmpty = TextView(this)
            tvEmpty.text = "No samples in this queue."
            tvEmpty.setTextColor(Color.parseColor("#94a3b8"))
            binding.labSamplesContainer.addView(tvEmpty)
            return
        }

        filteredList.forEach { sample ->
            val rowLayout = LinearLayout(this)
            rowLayout.orientation = LinearLayout.HORIZONTAL
            rowLayout.setPadding(20, 30, 20, 30)
            rowLayout.setBackgroundColor(Color.parseColor("#1c2541"))

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 20)
            rowLayout.layoutParams = params

            val tvInfo = TextView(this)
            tvInfo.text = "ID: ${sample.sample_id}\nType: ${sample.sample_type}\nDepositor: ${sample.depositor_name}"
            tvInfo.setTextColor(Color.WHITE)
            tvInfo.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)

            val btnAction = Button(this)

            if (currentTab == "APPROVALS") {
                btnAction.text = "APPROVE"
                btnAction.setBackgroundColor(Color.parseColor("#10b981"))
                btnAction.setOnClickListener { approveSampleInBackend(sample.id) }
            } else {
                if (sample.status == "RESULT_SENT") {
                    btnAction.text = "EMAIL SENT"
                    btnAction.isEnabled = false
                    btnAction.setBackgroundColor(Color.parseColor("#64748b"))
                } else {
                    btnAction.text = "SEND EMAIL"
                    btnAction.setBackgroundColor(Color.parseColor("#0ea5e9"))
                    btnAction.setOnClickListener { emailClientInBackend(sample.id) }
                }
            }

            rowLayout.addView(tvInfo)
            rowLayout.addView(btnAction)
            binding.labSamplesContainer.addView(rowLayout)
        }
    }

    private fun approveSampleInBackend(sampleId: Int) {
        lifecycleScope.launch {
            try {
                val res = withContext(Dispatchers.IO) { ApiClient.api.approveSample(sampleId) }
                if (res.isSuccessful) {
                    Toast.makeText(this@LabHeadDashboardActivity, "Sample Approved!", Toast.LENGTH_SHORT).show()
                    loadLabHeadDashboard()
                }
            } catch (e: Exception) {}
        }
    }

    private fun emailClientInBackend(sampleId: Int) {
        lifecycleScope.launch {
            try {
                val res = withContext(Dispatchers.IO) { ApiClient.api.sendEmailToClient(sampleId) }
                if (res.isSuccessful) {
                    Toast.makeText(this@LabHeadDashboardActivity, "Results emailed to client!", Toast.LENGTH_LONG).show()
                    loadLabHeadDashboard()
                }
            } catch (e: Exception) {}
        }
    }
}