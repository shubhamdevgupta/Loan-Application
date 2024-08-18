package com.androiddev.loanapplication.activity

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androiddev.loanapplication.databinding.ActivitySignupBinding
import com.androiddev.loanapplication.viewmodel.SigninViewmodel

class SignupActivity : AppCompatActivity() {

    private lateinit var signinViewmodel: SigninViewmodel
    private lateinit var binding: ActivitySignupBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()

        signinViewmodel = SigninViewmodel()
        subscribe()

        binding.btnSignup.setOnClickListener {
            if (binding.etUsernaem.text.isNullOrBlank() or
                binding.etPassword.text.isNullOrBlank() or
                binding.etMobileno.text.isNullOrBlank()
            ) {
                binding.etUsernaem.error = "Null or empty is not allowed"
            } else {
                signinViewmodel.username = binding.etUsernaem.text.toString()
                signinViewmodel.password = binding.etPassword.text.toString()
                signinViewmodel.mobile = binding.etMobileno.text.toString()
                signinViewmodel.signup()

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestPermissions() {
        val requiredPermissions = arrayOf(
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.READ_MEDIA_IMAGES
        )
        val permissionsToRequest = mutableListOf<String>()

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // All permissions are granted, proceed with the functionality
            Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
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
            } else if (!isLoading) {
                progressDialog.dismiss()
            }
        }

        signinViewmodel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, signinViewmodel.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        signinViewmodel.signinData.observe(this) { signinData ->
            if (signinData.message.equals("User already exists")) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, SigninActivity::class.java))
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val permissionResults = permissions.zip(grantResults.toTypedArray()).toMap()

            for ((permission, result) in permissionResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "$permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "$permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
