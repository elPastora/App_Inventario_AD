package com.elpastora.app.ui.product.viewholder

import android.R
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elpastora.app.data.model.Producto
import com.elpastora.app.databinding.ItemProductListBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemProductListBinding.bind(view)
    private val URL:String = "http://192.168.0.103:3001"
    fun bind(product: Producto, OnItemSelected:(String) -> Unit){
        binding.tvProductId.text =  product.codigoProducto
        binding.tvProductName.text = product.nombre + " " + product.nombreMarca
        binding.tvCategory.text = product.nombreCategoria
        binding.tvQuantity.text = product.cantidad.toString()
        binding.tvPrice.text = product.precioVenta.toString()
        val img = URL+product.imagen.toString()
        Picasso.get().load(img).into(binding.ivProduct)
        binding.root.setOnClickListener { OnItemSelected(product.codigoProducto) }

    }
}