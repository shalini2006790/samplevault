package com.samplevault.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samplevault.databinding.ActivitySamplelistBinding
class SampleListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySamplelistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySamplelistBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}