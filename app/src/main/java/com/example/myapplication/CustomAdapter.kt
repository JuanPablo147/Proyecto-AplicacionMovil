package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide


class CustomAdapter(context: Context,  items: ArrayList<Producto>) : BaseAdapter() {

    //Passing Values to Local Variables

    var context = context

    var name = items


    //Auto Generated Method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myView = convertView
        var holder: ViewHolder

        if (myView == null) {

            val mInflater = (context as Activity).layoutInflater
            myView = mInflater.inflate(R.layout.layout_list_view_row_items, parent, false)
            holder =  ViewHolder()

            holder.mImageView = myView!!.findViewById(R.id.imageView) as ImageView
            holder.mTextView = myView!!.findViewById(R.id.text_view_item_name) as TextView
            holder.mitemDescription = myView!!.findViewById<View>(R.id.text_view_item_description)as TextView
            holder.mEstado = myView!!.findViewById<View>(R.id.textEstadoView)as TextView
            myView.setTag(holder)
        } else {
            holder = myView.getTag() as ViewHolder

        }
        val currentItem = getItem(position) as Producto
        holder.mImageView?.let { Glide.with(context).load(currentItem.ruta_imagen).into(it) }
        //holder.mImageView!!.setImageResource(R.drawable.ic_baseline_fastfood_24)
        holder.mTextView!!.setText(currentItem.nombre_Producto)
        holder.mEstado!!.setText(currentItem.estado)
        if(currentItem.estado == "Disponible"){
            holder.mEstado!!.setTextColor(ContextCompat.getColor(context, R.color.green))

        }
        if(currentItem.estado == "No disponible"){
            holder.mEstado!!.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        if(currentItem.estado == "Descuento especial"){
            holder.mEstado!!.setTextColor(ContextCompat.getColor(context, R.color.orange))
        }
        holder.mitemDescription!!.setText(currentItem.negocio)

        return myView

    }

    override fun getItem(p0: Int): Any {
        return name.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return name.size
    }
    class ViewHolder() {
        var mImageView: ImageView? = null
        var mTextView: TextView? = null
        var mEstado: TextView? = null
        var mitemDescription: TextView? = null

/*
        var mImageView: ImageView? = view.findViewById<View>(R.id.imageView) as ImageView
        var mTextView: TextView? = view.findViewById<View>(R.id.text_view_item_name) as TextView
        var mitemDescription: TextView? = view.findViewById<View>(R.id.text_view_item_description) as TextView*/
    }

}
