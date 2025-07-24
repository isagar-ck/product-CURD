package com.example.myapplication.util

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



var USERDATA = "userData"

var USERID = "userId"
var PRODUCT_ID = "productId"
var IS_EDIT_PRODUCT = "isEditProduct"

/**
 * This Extension has been using for set padding for safe area
 */
fun adjustFullScreen(view: View) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { v: View, insets: WindowInsetsCompat ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}

/**
 * This Extension has been using for showing Toast messages
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * This Extension has been using for hide keyboard
 */
fun Activity.hideKeyboard(view: View) {
    val inputMethodManager =
        this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * This method is used to check if a string is empty or null.
 */
fun Any?.checkEmptyString(): Boolean {
    return if (this == null) true
    else TextUtils.isEmpty(this.toString().trim())
}

/**
 * This method is used for validating email id
 */
fun Any?.checkEmailPattern(): Boolean {
    if (this == null) return false
    return !Patterns.EMAIL_ADDRESS.matcher(this.toString().trim()).matches()
}
