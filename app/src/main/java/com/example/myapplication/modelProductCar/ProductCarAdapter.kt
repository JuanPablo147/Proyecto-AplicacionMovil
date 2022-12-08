package com.example.myapplication.modelProductCar

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.myapplication.R

class ProductCarAdapter (context: Context, items: ArrayList<ProductCar>) : BaseAdapter() {

    //Passing Values to Local Variables

    lateinit var actionAdd : (position : Int) -> Unit
    lateinit var actionRemove : (position : Int) -> Unit
    lateinit var actionComprar : (position : Int) -> Unit
    var context = context

    var name = items


    //Auto Generated Method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myView = convertView
        var holder: ViewHolder

        if (myView == null) {

            val mInflater = (context as Activity).layoutInflater
            myView = mInflater.inflate(R.layout.layout_list_product_car, parent, false)
            holder =  ViewHolder()

            holder.mImageView = myView!!.findViewById(R.id.imageView123) as ImageView
            holder.textName = myView!!.findViewById(R.id.text_product_car_name) as TextView
            holder.textCost = myView!!.findViewById<View>(R.id.text_product_car_cost)as TextView
            holder.btnAdd = myView!!.findViewById(R.id.btn_add)
            holder.btnRemove = myView!!.findViewById(R.id.btn_remove)
            holder.textCant= myView!!.findViewById(R.id.text_product_car_cant)
                    holder.enviar= myView.findViewById(R.id.btnW)

            myView.setTag(holder)
        } else {
            holder = myView.getTag() as ViewHolder

        }
        val currentItem = getItem(position) as ProductCar

            //Toast.makeText(context, currentItem.description, Toast.LENGTH_SHORT).show()
         //   holder.mImageView?.let { Glide.with(context).load(currentItem.ruta_imagen).into(it) }

           // holder.mImageView!!.setImageResource(R.drawable.ic_baseline_fastfood_24)

        //holder.mImageView?.let { Glide.with(context).load(currentItem.ruta_imagen).into(it) }
        holder.mImageView?.let { Glide.with(context).load(currentItem.ruta_imagen).error(R.drawable.ic_baseline_fastfood_24) .into(it)}
        holder.textName!!.setText(currentItem.nombre_producto)
        holder.textCost!!.setText("Bs "+(currentItem.costo*currentItem.cant).toString())
        holder.textCant!!.setText(currentItem.cant.toString())
        holder.btnAdd!!.setOnClickListener {
            actionAdd.invoke(position)
        }
        //Agregamos funcionalidades al boton para eliminar
        holder.btnRemove!!.setOnClickListener {
            actionRemove.invoke(position)
        }
        //Agregamos funcionalidades al boton para enviar
        holder.enviar!!.setOnClickListener {
            actionComprar.invoke(position)
        }


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
        var textName: TextView? = null
        var textCost: TextView? = null
        var btnAdd : ImageView ?= null
        var btnRemove : ImageView ?= null
        var textCant : TextView ?= null
        var enviar : Button?= null
/*
        var mImageView: ImageView? = view.findViewById<View>(R.id.imageView) as ImageView
        var mTextView: TextView? = view.findViewById<View>(R.id.text_view_item_name) as TextView
        var mitemDescription: TextView? = view.findViewById<View>(R.id.text_view_item_description) as TextView*/
    }

}
