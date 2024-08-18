package com.androiddev.loanapplication.network

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



