package com.androiddev.loanapplication.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddev.loanapplication.data.LoanHistoryAdapter
import com.androiddev.loanapplication.databinding.ActivityLoanHistoryBinding
import com.androiddev.loanapplication.viewmodel.LoanViewmodel

class LoanHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoanHistoryBinding
    lateinit var viewmodel: LoanViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewmodel = LoanViewmodel()

        subscribe()
        binding.btnLoanHistory.setOnClickListener {
            if (binding.etMobile.text.isNullOrBlank()) {
                Toast.makeText(this, "Null or empty is not allowed", Toast.LENGTH_SHORT).show()
            } else {
                viewmodel.mobile = binding.etMobile.text.toString()
                viewmodel.loanHistory()
            }
        }
    }

    private fun subscribe() {
        viewmodel.isLoading.observe(this) { isLoading ->

        }
        viewmodel.isError.observe(this) { isError ->

        }
        viewmodel.loanHistoryData.observe(this) { loanData ->
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val adapter = LoanHistoryAdapter(loanData.loans)
            binding.recyclerView.adapter = adapter
        }


    }
}