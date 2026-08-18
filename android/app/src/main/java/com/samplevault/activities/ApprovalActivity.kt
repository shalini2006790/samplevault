package com.samplevault.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samplevault.databinding.ActivityApprovalBinding
class ApprovalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApprovalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}