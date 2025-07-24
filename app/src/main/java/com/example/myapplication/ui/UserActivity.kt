package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.databinding.ActivityUserBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.model.ProductModel
import com.example.myapplication.util.adjustFullScreen

class UserActivity : AppCompatActivity(), ProductAdapter.ProductAction {

    lateinit var binding: ActivityUserBinding
    private val dbHelper = DatabaseHelper(this)
    private var productAdapter = ProductAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        binding.productList.adapter = productAdapter
        setupView()
    }

    private fun setupView() {
        with(binding) {
            val getProduct =
                dbHelper.getAllProducts()
            Log.e("TAG", "setUpView: $getProduct")
            productAdapter.setData(ArrayList(getProduct))

            if (getProduct.isEmpty()) {
                noData.visibility = View.VISIBLE
            } else {
                noData.visibility = View.GONE
            }
        }
    }

    override fun update(data: ProductModel) {

    }

    override fun delete(data: ProductModel) {

    }
}