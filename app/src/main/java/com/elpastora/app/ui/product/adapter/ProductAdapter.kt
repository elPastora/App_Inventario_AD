package com.elpastora.app.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elpastora.app.R
import com.elpastora.app.data.model.Producto
import com.elpastora.app.ui.product.viewholder.ProductViewHolder

class ProductAdapter(
    var products: List<Producto> = emptyList(),
    private val OnItemSelected:(String) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ProductViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_product_list, parent, false))

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], OnItemSelected)
    }

    fun updateList(products: List<Producto>){
        this.products = products
        notifyDataSetChanged()
    }

}