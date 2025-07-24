package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddProductBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.model.ProductModel
import com.example.myapplication.util.IS_EDIT_PRODUCT
import com.example.myapplication.util.PRODUCT_ID
import com.example.myapplication.util.SharedPref
import com.example.myapplication.util.adjustFullScreen
import com.example.myapplication.util.checkEmptyString
import com.example.myapplication.util.showToast

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private val dbHelper = DatabaseHelper(this)
    private var isEditProduct: Boolean = false
    private var productID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        init()
        manageClick()
    }

    private fun init() {
        if (intent != null) {
            if (intent.hasExtra(IS_EDIT_PRODUCT)) {
                isEditProduct = intent.getBooleanExtra(IS_EDIT_PRODUCT, false)
                productID = intent.getIntExtra(PRODUCT_ID, 0)
                val getProduct = dbHelper.getProduct(productID)
                Log.e("TAG", "setUpView: $getProduct")
                getProduct?.let {
                    with(binding) {
                        name.setText(it.name)
                        description.setText(it.description)
                        quantity.setText(it.quantity.toString())
                        price.setText(it.price.toString())
                    }
                }
            }
        }
    }

    private fun manageClick() {
        with(binding) {
            saveButton.setOnClickListener {
                if (!isValid()) return@setOnClickListener
                if (isEditProduct) {
                    updateProduct()
                } else {
                    addNewProduct()
                }
            }
        }
    }

    private fun addNewProduct() {
        val productModel = ProductModel(
            userId = SharedPref(this@AddProductActivity).userID,
            name = binding.name.text.trim().toString(),
            description = binding.description.text.trim().toString(),
            quantity = binding.quantity.text.trim().toString().toInt(),
            price = binding.price.text.trim().toString().toDouble()
        )
        val insertProduct = dbHelper.insertProduct(productModel)

        if (insertProduct) {
            showToast("Product added successfully.")
            finish()
        } else {
            showToast("Something want wrong. Please try again later.")
        }
    }

    private fun updateProduct() {
        val productModel = ProductModel(
            id = productID,
            userId = SharedPref(this@AddProductActivity).userID,
            name = binding.name.text.trim().toString(),
            description = binding.description.text.trim().toString(),
            quantity = binding.quantity.text.trim().toString().toInt(),
            price = binding.price.text.trim().toString().toDouble()
        )
        val updateProduct = dbHelper.updateProduct(productModel)

        if (updateProduct) {
            showToast("Product updated successfully.")
            finish()
        } else {
            showToast("Something want wrong. Please try again later.")
        }
    }

    private fun isValid(): Boolean {
        if (binding.name.text.checkEmptyString()) {
            showToast(getString(R.string.enter_product_name))
            return false
        }
        if (binding.description.text.checkEmptyString()) {
            showToast(getString(R.string.enter_product_description))
            return false
        }
        if (binding.quantity.text.checkEmptyString()) {
            showToast(getString(R.string.enter_product_quantity))
            return false
        }
        if (binding.price.text.checkEmptyString()) {
            showToast(getString(R.string.enter_product_price))
            return false
        }
        return true
    }
}