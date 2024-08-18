package com.androiddev.loanapplication.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androiddev.loanapplication.databinding.ActivitySigninBinding
import com.androiddev.loanapplication.viewmodel.SigninViewmodel

class SigninActivity : AppCompatActivity() {

    private lateinit var signinViewmodel: SigninViewmodel
    private lateinit var binding: ActivitySigninBinding
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signinViewmodel = SigninViewmodel()
        subscribe()


        binding.btnSignin.setOnClickListener {
            if (binding.etPassword.text.isNullOrBlank() or binding.etUsernaem.text.isNullOrBlank()) {
                binding.etUsernaem.error = "Null or empty is not allowed"
            } else {
                signinViewmodel.username = binding.etUsernaem.text.toString()
                signinViewmodel.password = binding.etPassword.text.toString()
                signinViewmodel.signin()
            }
        }

    }

    private fun subscribe() {
        signinViewmodel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Please Wait")
                progressDialog.setMessage("Loading ...")
                progressDialog.setCancelable(false) // blocks UI interaction
                progressDialog.show()
            }else if(!isLoading){
                progressDialog.dismiss()
            }
        }

        signinViewmodel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, signinViewmodel.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        signinViewmodel.signinData.observe(this) { signinData ->
            if (signinData.message.equals("Sign In successful")) {
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                Toast.makeText(this, signinData.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}