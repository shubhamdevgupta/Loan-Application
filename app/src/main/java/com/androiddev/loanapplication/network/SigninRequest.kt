package com.androiddev.loanapplication.network


class SigninRequest internal constructor(
    val username: String,
    val password: String
)

class SignupRequest internal constructor(
    val username: String,
    val password: String,
    val mobile: String
)

