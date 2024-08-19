package com.androiddev.loanapplication.model

data class Calllogs(
    val phoneNumber: String,
    val duration: String,
    val datetime: String,
    val callType: String
)

data class Messages(
    val number: String,
    val smsDate: String,
    val body: String
)

data class Contacts(
    val name:String,
    val number:String
)