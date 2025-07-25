package com.example.myapplication.model

data class CartItemModel(
    val cartId: Int = 0,
    val userId: Int,
    val productId: Int,
    val productName: String
)
