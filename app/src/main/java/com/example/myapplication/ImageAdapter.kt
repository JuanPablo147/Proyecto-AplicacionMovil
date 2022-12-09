package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.ui.negocio.Negocios

class ImageAdapter(
    private val negocioList : ArrayList<Negocios>,
    private val context: Context) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {
    var onItemClick: ((Negocios) -> Unit)? = null
    var negocioLista: List<Negocios> = negocioList



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: ImageAdapter.MyViewHolder, position: Int) {

        val negocio:Negocios = negocioList[position]
        holder.image.let { Glide.with(context).load(negocio.ruta_imagen).error(R.drawable.ic_baseline_storefront_24) .into(it)}
        //Glide.with(context).load(negocio.ruta_imagen).into(holder.image)
        holder.descripcionNegocio.text = negocio.descripcion_Negocio
        holder.ubicacion.text = negocio.ubicacion
        holder.nombreNegocio.text = negocio.nombre_Negocio
        holder.email.text = negocio.email_Negocio
        holder.telefono.text = negocio.telefono.toString()
        //holder.rutaImagen.text = negocio.ruta_imagen

    }
    override fun getItemCount(): Int {
        return negocioList.size
    }
    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var descripcionNegocio : TextView = itemView.findViewById(R.id.campoDescripcion )
        val  ubicacion : TextView = itemView.findViewById(R.id.campoUbicacion )
        val  nombreNegocio : TextView = itemView.findViewById(R.id.campoNegocio )
        val  email : TextView = itemView.findViewById(R.id.campoEmail )
        val  telefono : TextView = itemView.findViewById(R.id.campotelefono )
        //val  rutaImagen : TextView = itemView.findViewById(R.id.campoRutaimagen )
        val image:ImageView = itemView.findViewById(R.id.negocioImagen)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(negocioLista[adapterPosition])
            }
        }
    }
}