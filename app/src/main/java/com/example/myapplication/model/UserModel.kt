package com.example.myapplication.model

data class UserModel(
    val userId: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val userType: String
)
