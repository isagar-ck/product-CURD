package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.model.UserModel
import com.example.myapplication.util.adjustFullScreen
import com.example.myapplication.util.checkEmailPattern
import com.example.myapplication.util.checkEmptyString
import com.example.myapplication.util.showToast

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        manageClick()
    }

    private fun manageClick() {
        with(binding) {
            signUpButton.setOnClickListener {
                if (!isValid()) return@setOnClickListener

                if (dbHelper.checkEmailExists(emailEditText.text.toString())) {
                    showToast("This email already register. Please use another email")
                    return@setOnClickListener
                }

                val userModel = UserModel(
                    name = nameEditText.text?.trim().toString(),
                    email = emailEditText.text?.trim().toString(),
                    password = passwordEditText.text?.trim().toString(),
                    userType = if (adminRadio.isChecked) "admin" else "user"
                )
                val insertUser = dbHelper.registerUser(userModel)
                if (insertUser) {
                    showToast("User register successfully.")
                    finish()
                } else {
                    showToast("Something want wrong. Please try again later.")
                }
            }
        }
    }

    private fun isValid(): Boolean {
        if (binding.nameEditText.text.checkEmptyString()) {
            showToast(getString(R.string.enter_username))
            return false
        }
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
        if (binding.cPasswordEditText.text.checkEmptyString()) {
            showToast(getString(R.string.enter_confirm_password))
            return false
        }
        if (binding.cPasswordEditText.text?.trim()
                .toString() != binding.passwordEditText.text?.trim().toString()
        ) {
            showToast(getString(R.string.password_not_match))
            return false
        }
        return true
    }
}