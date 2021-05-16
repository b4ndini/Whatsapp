package com.lfelipe.whatsapp.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lfelipe.whatsapp.databinding.ActivityPhoneRegisterBinding

class PhoneRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}