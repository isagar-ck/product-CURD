package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemListProductBinding
import com.example.myapplication.model.CartItemModel

class CheckOutAdapter(val removeProduct: RemoveProduct) : RecyclerView.Adapter<CheckOutAdapter.ViewHolder>() {

    val productList = ArrayList<CartItemModel>()

    fun setData(newList: ArrayList<CartItemModel>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
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
        holder.bindData(productList[position], removeProduct)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(val binding: ItemListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        fun bindData(data: CartItemModel, removeProduct: RemoveProduct) {
            with(binding) {
                data.let {
                    name.text = it.productName
//                    description.text = it.description
//                    price.text = "$ ${it.price}"

                    delete.visibility = View.GONE
                    edit.visibility = View.GONE
                    updateCart.visibility = View.VISIBLE

                    updateCart.text = "Remove"


                    updateCart.setOnClickListener {
                        removeProduct.removeProduct(data, adapterPosition)
                    }
                }
            }
        }
    }

    interface RemoveProduct{
        fun removeProduct(data: CartItemModel, position: Int)
    }
}