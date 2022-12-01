package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(
    private val negocioList : ArrayList<Negocio>,
    private val context: Context) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: ImageAdapter.MyViewHolder, position: Int) {

        val negocio:Negocio = negocioList[position]
        holder.image.let { Glide.with(context).load(negocio.ruta_imagen).error(R.drawable.ic_baseline_storefront_24) .into(it)}
        //Glide.with(context).load(negocio.ruta_imagen).into(holder.image)
        holder.descripcionNegocio.text = negocio.descripcion_Negocio
        holder.ubicacion.text = negocio.ubicacion
        holder.nombreNegocio.text = negocio.nombre_Negocio
        holder.email.text = negocio.email_Negocio
        //holder.rutaImagen.text = negocio.ruta_imagen

    }
    override fun getItemCount(): Int {
        return negocioList.size
    }
    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var descripcionNegocio : TextView = itemView.findViewById(R.id.campoDescripcion )
        val  ubicacion : TextView = itemView.findViewById(R.id.campoUbicacion )
        val  nombreNegocio : TextView = itemView.findViewById(R.id.campoNegocio )
        val  email : TextView = itemView.findViewById(R.id.campoEmail )
        //val  rutaImagen : TextView = itemView.findViewById(R.id.campoRutaimagen )
        val image:ImageView = itemView.findViewById(R.id.negocioImagen)
    }
}