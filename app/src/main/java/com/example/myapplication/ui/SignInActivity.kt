package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.util.SharedPref
import com.example.myapplication.util.adjustFullScreen
import com.example.myapplication.util.checkEmailPattern
import com.example.myapplication.util.checkEmptyString
import com.example.myapplication.util.showToast

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dbHelper: DatabaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        adjustFullScreen(binding.root)

        manageClickEvent()
    }

    private fun manageClickEvent() {
        with(binding) {
            loginButton.setOnClickListener {
                if (!isValid()) return@setOnClickListener

                val checkUser = dbHelper.loginUser(
                    emailEditText.text?.trim().toString(), passwordEditText.text?.trim().toString()
                )

                if (checkUser != null) {
                    // User exist in database
                    Log.e("TAG", "manageLogin: LOGIN")
                    this@SignInActivity.showToast("Login Successfully")
                    SharedPref(this@SignInActivity).userID = checkUser.userId

                    if (checkUser.userType.lowercase() == "admin") {
                        val intent = Intent(this@SignInActivity, AdminDashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SignInActivity, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // User not found in DB. Show error message
                    Log.e("TAG", "manageLogin: ERROR")
                    this@SignInActivity.showToast(getString(R.string.login_error))
                }
            }

            signupText.setOnClickListener {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            }
        }
    }

    // Check validation
    private fun isValid(): Boolean {
        if (binding.emailEditText.text.checkEmptyString()) {
            showToast(getString(R.string.enter_email))
            return false
        }
        if (binding.emailEditText.text.checkEmailPattern()) {
            showToast(getString(R.string.enter_valid_email))
            return false
        }
        if (binding.passwordEditText.text.checkEmptyString()) {
            showToast(getString(R.string.enter_password))
            return false
        }
        return true
    }
}