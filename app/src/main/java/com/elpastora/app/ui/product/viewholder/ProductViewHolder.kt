package com.elpastora.app.ui.product.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elpastora.app.data.model.Producto
import com.elpastora.app.databinding.ItemProductListBinding

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemProductListBinding.bind(view)
    fun bind(product: Producto, OnItemSelected:(String) -> Unit){
        binding.tvProductId.text =  product.codigoProducto
        binding.tvProductName.text = product.nombre + " " + product.nombreMarca
        binding.tvCategory.text = product.nombreCategoria
        binding.tvQuantity.text = product.cantidad.toString()
        binding.tvPrice.text = product.precioVenta.toString()
        binding.root.setOnClickListener { OnItemSelected(product.codigoProducto) }

    }
}