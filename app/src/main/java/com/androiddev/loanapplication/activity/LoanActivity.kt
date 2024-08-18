package com.androiddev.loanapplication.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androiddev.loanapplication.R
import com.androiddev.loanapplication.databinding.ActivityLoanBinding
import com.androiddev.loanapplication.viewmodel.LoanViewmodel

class LoanActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoanBinding
    lateinit var loanViewmodel: LoanViewmodel
    lateinit var progressDialog: ProgressDialog

    lateinit var loanAmount: String
    lateinit var loanDur: String
    lateinit var mobileNo: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)

        binding = ActivityLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loanViewmodel = LoanViewmodel()
        subscribe()

        binding.btnApply.setOnClickListener {
            loanAmount = binding.etLoanAmt.text.toString()
            loanDur = binding.etLoanDur.text.toString()
            mobileNo = binding.etMobileNo.text.toString()

            if (loanAmount.isBlank() or loanDur.isBlank() or mobileNo.isBlank()) {
                Toast.makeText(this, "Null or Blank is not allowed", Toast.LENGTH_SHORT).show()
            } else {
                loanViewmodel.mobile = mobileNo
                loanViewmodel.loan_amount = loanAmount
                loanViewmodel.loan_duration = loanDur
                loanViewmodel.applyLoan()
            }
        }
    }

    private fun subscribe() {
        loanViewmodel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Please Wait")
                progressDialog.setMessage("Loading ...")
                progressDialog.setCancelable(false) // blocks UI interaction
                progressDialog.show()
            }
        }

        loanViewmodel.isError.observe(this) { isError ->
            if (isError) Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        }

        loanViewmodel.loanData.observe(this) { response ->
            if (response.message.equals("Loan application submitted")) {
                Toast.makeText(this, "Loan Applied Succesfully", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                finish()
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
