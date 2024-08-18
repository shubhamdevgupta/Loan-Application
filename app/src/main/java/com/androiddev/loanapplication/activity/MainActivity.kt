package com.androiddev.loanapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androiddev.loanapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val intent =Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignin.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }
    }
}