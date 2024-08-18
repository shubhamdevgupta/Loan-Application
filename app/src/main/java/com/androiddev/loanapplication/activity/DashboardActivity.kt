package com.androiddev.loanapplication.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.CallLog
import android.provider.Telephony
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androiddev.loanapplication.databinding.ActivityDashboardBinding
import com.androiddev.loanapplication.viewmodel.LoanViewmodel
import java.lang.Long
import java.util.Date
import kotlin.Array
import kotlin.String
import kotlin.arrayOf
import kotlin.run


class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    lateinit var viewmodel: LoanViewmodel
    lateinit var progressDialog: ProgressDialog
    lateinit var callLogs: Array<String?>
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
            //getCallLogs()
            getAllSms(this)
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
            }

        }
        viewmodel.isError.observe(this) { isError ->
            if (isError) {
                progressDialog.dismiss()
                Toast.makeText(this, viewmodel.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        viewmodel.loanData.observe(this) { historyData ->
            Toast.makeText(this, historyData.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCallLogs() {
        val cr = baseContext.contentResolver
        val c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
        var totalCall = 1
        if (c != null) {
            totalCall = 2
            if (c.moveToLast()) {
                for (j in 0 until totalCall) {
                    val phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val dateFormat = Date(Long.valueOf(callDate))
                    val callDayTimes: String = java.lang.String.valueOf(dateFormat)
                    var direction: String? = null
                    when (c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)).toInt()) {
                        CallLog.Calls.OUTGOING_TYPE -> direction = "OUTGOING"
                        CallLog.Calls.INCOMING_TYPE -> direction = "INCOMING"
                        CallLog.Calls.MISSED_TYPE -> direction = "MISSED"
                        else -> {}
                    }
                    c.moveToPrevious()
                    callLogs = arrayOf(phNumber, callDuration, callDayTimes, direction)
                    callLogs.forEach { Log.d("MYTAG", "getCallLogs: $it") }
                }
            }
            c.close()
        }
    }

    fun getAllSms(context: Context) {
        val cr = context.contentResolver
        val c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
        var totalSMS = 0
        if (c != null) {
            totalSMS = c.count
            if (c.moveToFirst()) {
                for (j in 0 until totalSMS) {
                    val smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val dateFormat = Date(java.lang.Long.valueOf(smsDate))
                    var type: String
                    when (c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)).toInt()) {
                        Telephony.Sms.MESSAGE_TYPE_INBOX -> type = "inbox"
                        Telephony.Sms.MESSAGE_TYPE_SENT -> type = "sent"
                        Telephony.Sms.MESSAGE_TYPE_OUTBOX -> type = "outbox"
                        else -> {}
                    }
                    c.moveToNext()
                    Log.d(
                        "MYTAG",
                        "getAllSms: $smsDate  number--- $number   body----$body  dateformat---$dateFormat"
                    )
                }
            }
            c.close()
        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show()
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