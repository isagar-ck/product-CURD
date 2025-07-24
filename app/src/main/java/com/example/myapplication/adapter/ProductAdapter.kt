package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemListProductBinding
import com.example.myapplication.model.ProductModel

class ProductAdapter(private val productAction: ProductAction) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var productList = ArrayList<ProductModel>()

    fun setData(newProductList: ArrayList<ProductModel>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(productList[position], productAction)
    }

    class ViewHolder(private val binding: ItemListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: ProductModel, productAction: ProductAction) {
            with(binding) {
                data.let {
                    name.text = it.name
                    description.text = it.description
                    price.text = "$ ${it.price}"

                    edit.setOnClickListener {
                        productAction.update(data)
                    }
                    delete.setOnClickListener {
                        productAction.delete(data)
                    }
                }
            }
        }
    }

    interface ProductAction {
        fun update(data: ProductModel)
        fun delete(data: ProductModel)
    }
}