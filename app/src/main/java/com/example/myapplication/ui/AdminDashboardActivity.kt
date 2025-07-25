package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.databinding.ActivityAdminDashboardBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.model.ProductModel
import com.example.myapplication.util.Constants.Companion.IS_EDIT_PRODUCT
import com.example.myapplication.util.Constants.Companion.PRODUCT_ID
import com.example.myapplication.util.SharedPref
import com.example.myapplication.util.adjustFullScreen
import com.example.myapplication.util.showToast

class AdminDashboardActivity : AppCompatActivity(), ProductAdapter.ProductAction {

    private lateinit var binding: ActivityAdminDashboardBinding
    private val dbHelper = DatabaseHelper(this)
    private var productAdapter = ProductAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        binding.productList.adapter = productAdapter
//        setUpView()
        manageClick()
    }

    override fun onResume() {
        super.onResume()
        setUpView()
    }

    private fun setUpView() {
        with(binding) {
            val getProduct =
                dbHelper.getUserProducts(SharedPref(this@AdminDashboardActivity).userID)
            Log.e("TAG", "setUpView: $getProduct")
            productAdapter.setData(ArrayList(getProduct))

            if (getProduct.isEmpty()) {
                noData.visibility = View.VISIBLE
            } else {
                noData.visibility = View.GONE
            }
        }
    }

    private fun manageClick() {
        with(binding) {
            addProductButton.setOnClickListener {
                startActivity(Intent(this@AdminDashboardActivity, AddProductActivity::class.java))
            }

            logout.setOnClickListener {
                SharedPref(this@AdminDashboardActivity).clearPreferences()
                startActivity(Intent(this@AdminDashboardActivity, SignInActivity::class.java))
                finishAffinity()
            }
        }
    }

    override fun update(data: ProductModel) {
        val intent = Intent(this, AddProductActivity::class.java).apply {
            putExtra(PRODUCT_ID, data.id)
            putExtra(IS_EDIT_PRODUCT, true)
        }
        startActivity(intent)
    }

    override fun delete(data: ProductModel) {
        val deleteProduct = dbHelper.deleteProduct(data.id)
        if (deleteProduct) {
            showToast("Product deleted successfully")
            setUpView()
        } else {
            showToast("Something want wrong. Please try again later.")
        }
    }
}