package com.samplevault.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samplevault.databinding.ActivityNotificationsBinding
class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}