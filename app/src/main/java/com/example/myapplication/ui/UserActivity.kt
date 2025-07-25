package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.adapter.UserProductAdapter
import com.example.myapplication.databinding.ActivityUserBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.model.ProductModel
import com.example.myapplication.util.SharedPref
import com.example.myapplication.util.adjustFullScreen
import com.example.myapplication.util.showToast

class UserActivity : AppCompatActivity(), UserProductAdapter.AddToCart {

    lateinit var binding: ActivityUserBinding
    private val dbHelper = DatabaseHelper(this)
    private var productAdapter = UserProductAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        binding.productList.adapter = productAdapter
        manageClick()
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    private fun manageClick() {
        with(binding) {
            checkoutButton.setOnClickListener {
                startActivity(Intent(this@UserActivity, CheckoutActivity::class.java))
            }

            logout.setOnClickListener {
                SharedPref(this@UserActivity).clearPreferences()
                startActivity(Intent(this@UserActivity, SignInActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun setupView() {
        with(binding) {
            val getProduct = dbHelper.getAllProducts()
            Log.e("TAG", "setUpView: $getProduct")
            productAdapter.setData(ArrayList(getProduct))

            if (getProduct.isEmpty()) {
                noData.visibility = View.VISIBLE
            } else {
                noData.visibility = View.GONE
            }
        }
    }

    override fun addOnCartClick(data: ProductModel, position: Int) {
        if (data.addToCart < 1) {
            val addToCart = dbHelper.addToCart(SharedPref(this).userID, data.id)
            if (addToCart) {
                productAdapter.updateData(position)
                showToast("Product added successfully in your cart.")
            } else {
                showToast("Something want wrong. Please try again later.")
            }
        }
    }
}