package com.elpastora.app.ui.catalog.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.elpastora.app.R
import com.elpastora.app.data.model.Categoria

class CategoriesCatalogAdapter(
    var categories: List<Categoria> = emptyList(),
    private val context: Context
) : BaseAdapter() {
    override fun getCount() = categories.size

    override fun getItem(position: Int) = categories[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        val holder:CategoriesCatalogViewHolder

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_drowdowm, parent,false)
            holder = CategoriesCatalogViewHolder()
            holder.text = view.findViewById(R.id.ddItem)
            view.tag = holder
        }else{
            view = convertView
            holder = convertView.tag as CategoriesCatalogViewHolder
        }

        val item = categories[position]
        holder.text.text = item.nombre

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    private class CategoriesCatalogViewHolder{
        lateinit  var text: TextView
    }

}