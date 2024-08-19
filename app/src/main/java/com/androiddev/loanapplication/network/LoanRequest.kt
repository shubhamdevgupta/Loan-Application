package com.androiddev.loanapplication.network

import com.androiddev.loanapplication.model.Calllogs
import com.androiddev.loanapplication.model.Contacts
import com.androiddev.loanapplication.model.Messages

class LoanRequest internal constructor(
    val mobile: String,
    val loan_amount: String,
    val loan_duration: String
)


class LoanHistory internal constructor(
    val mobile: String
)

class UpdateProfile internal constructor(
    val mobile: String,
    val username: String,
    val new_mobile: String
)

class UploadUserData internal constructor(
    val mobile: String,
    val contacts: ArrayList<Contacts>,
    val call_logs: ArrayList<Calllogs>,
    val messages: ArrayList<Messages>
)

class UploadPhoto internal constructor(
    val mobile: String,
    val photo: String
)


