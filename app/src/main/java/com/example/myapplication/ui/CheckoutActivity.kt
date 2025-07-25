package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.adapter.CheckOutAdapter
import com.example.myapplication.databinding.ActivityCheckoutBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.model.CartItemModel
import com.example.myapplication.util.SharedPref
import com.example.myapplication.util.adjustFullScreen
import com.example.myapplication.util.showToast

class CheckoutActivity : AppCompatActivity(), CheckOutAdapter.RemoveProduct {

    lateinit var binding: ActivityCheckoutBinding
    val dbHelper = DatabaseHelper(this)
    val adapter = CheckOutAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        binding.productList.adapter = adapter
        manageClick()
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    private fun manageClick() {
        with(binding) {
            paymentButton.setOnClickListener {
                startActivity(Intent(this@CheckoutActivity, PaymentActivity::class.java))
            }
        }
    }

    private fun setupView() {
        with(binding) {
            val getProduct = dbHelper.getCartItems(SharedPref(this@CheckoutActivity).userID)
            Log.e("TAG", "setUpView: $getProduct")
            adapter.setData(ArrayList(getProduct))

            if (getProduct.isEmpty()) {
                noData.visibility = View.VISIBLE
            } else {
                noData.visibility = View.GONE
            }
        }
    }

    override fun removeProduct(
        data: CartItemModel, position: Int
    ) {
        val removeProduct = dbHelper.removeFromCart(data.cartId)
        if (removeProduct) {
            showToast("Product remove successfully.")
            setupView()
        } else {
            showToast("Something want wrong. Please try again later.")
        }
    }
}