package com.example.myapplication.model

data class ProductModel(
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
)
