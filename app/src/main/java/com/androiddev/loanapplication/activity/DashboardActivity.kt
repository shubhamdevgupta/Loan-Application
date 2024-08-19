package com.androiddev.loanapplication.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androiddev.loanapplication.databinding.ActivityDashboardBinding
import com.androiddev.loanapplication.viewmodel.LoanViewmodel


class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    lateinit var viewmodel: LoanViewmodel
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel = LoanViewmodel()
        subscribe()

        binding.btnLoan.setOnClickListener {
            binding.cardUpdate.visibility = View.GONE
            startActivity(Intent(this, LoanActivity::class.java))
        }

        binding.imgHistory.setOnClickListener {
            startActivity(Intent(this, LoanHistoryActivity::class.java))
        }

        binding.imgProfile.setOnClickListener {
            binding.cardUpdate.visibility = View.VISIBLE
        }
        binding.btnUpdateProfile.setOnClickListener {
            if (binding.etMobile.text.isNullOrBlank() or
                binding.etUpdatName.text.isNullOrBlank() or
                binding.etUpdateMobile.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Null or empty is not allowed", Toast.LENGTH_SHORT).show()
            } else {
                viewmodel.username = binding.etUpdatName.text.toString()
                viewmodel.mobile = binding.etMobile.text.toString()
                viewmodel.new_mobile = binding.etUpdateMobile.text.toString()
                viewmodel.updateProfile()
            }
        }
    }

    private fun subscribe() {
        viewmodel.isLoading.observe(this) { isLoading ->
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
        viewmodel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, viewmodel.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        viewmodel.loanData.observe(this) { historyData ->
            Toast.makeText(this, historyData.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to exit ??")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, i ->
                run {
                    finishAffinity()
                    super.onBackPressed()
                }
            }
            .setNegativeButton(
                "No"
            ) { dialog, i -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

}