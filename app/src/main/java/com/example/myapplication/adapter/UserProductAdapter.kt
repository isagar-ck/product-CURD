package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemListProductBinding
import com.example.myapplication.model.ProductModel

class UserProductAdapter(val addToCart: AddToCart) :
    RecyclerView.Adapter<UserProductAdapter.ViewHolder>() {

    val productList = ArrayList<ProductModel>()

    fun setData(newList: ArrayList<ProductModel>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateData(position: Int) {
        productList[position].addToCart = 1
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding =
            ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        holder.bindData(productList[position], addToCart)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(val binding: ItemListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        fun bindData(data: ProductModel, addToCart: AddToCart) {
            with(binding) {
                data.let {
                    name.text = it.name
                    description.text = it.description
                    price.text = "$ ${it.price}"

                    delete.visibility = View.GONE
                    edit.visibility = View.GONE
                    updateCart.visibility = View.VISIBLE

                    if (it.addToCart == 1) {
                    updateCart.text = "Added"
                    } else {
                        updateCart.text = context.getString(R.string.add_to_cart)
                    }

                    updateCart.setOnClickListener {
                        addToCart.addOnCartClick(data, adapterPosition)
                    }
                }
            }
        }
    }

    interface AddToCart {
        fun addOnCartClick(data: ProductModel, position: Int)
    }
}