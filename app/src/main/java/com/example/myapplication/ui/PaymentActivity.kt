package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPaymentBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.util.SharedPref
import com.example.myapplication.util.adjustFullScreen
import com.example.myapplication.util.checkEmptyString
import com.example.myapplication.util.showToast

class PaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentBinding
    val dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        manageClick()
    }

    private fun manageClick() {
        with(binding) {
            saveButton.setOnClickListener {
                if (!isValid()) return@setOnClickListener
                val isRemoved =
                    dbHelper.removeAllProductFromCart(SharedPref(this@PaymentActivity).userID)

                if (isRemoved) {
                    showToast("Your order place successfully.")
                    startActivity(Intent(this@PaymentActivity, UserActivity::class.java))
                    finishAffinity()
                } else {
                    showToast("Something want wrong. Please try again later.")
                }
            }
        }
    }

    private fun isValid(): Boolean {
        if (binding.cardNumber.text.checkEmptyString()) {
            showToast(getString(R.string.enter_debit_credit_card_number))
            return false
        }
        if (binding.monthET.text.checkEmptyString()) {
            showToast(getString(R.string.enter_card_expiry_month))
            return false
        }
        if (binding.yearET.text.checkEmptyString()) {
            showToast(getString(R.string.enter_card_expiry_year))
            return false
        }
        if (binding.cvvET.text.checkEmptyString()) {
            showToast(getString(R.string.cvv))
            return false
        }
        if (binding.nameET.text.checkEmptyString()) {
            showToast(getString(R.string.enter_card_holder_name))
            return false
        }
        return true
    }
}