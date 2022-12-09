package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.ui.negocio.Negocios

class HistorialAdapter (
    private val negocioList : ArrayList<Negocios>,
    private val context: Context
) : RecyclerView.Adapter<HistorialAdapter.MyViewHolderHistorial>() {
    var onItemClick: ((Negocios) -> Unit)? = null
    var negocioLista: List<Negocios> = negocioList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :MyViewHolderHistorial{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listitem2,parent,false)
        return MyViewHolderHistorial(itemView)

    }
    override fun onBindViewHolder(holder: MyViewHolderHistorial, position: Int) {

        val negocio: Negocios = negocioList[position]
        holder.image.let { Glide.with(context).load(negocio.ruta_imagen).error(R.drawable.ic_baseline_storefront_24) .into(it)}
        holder.descripcionNegocio.text = negocio.descripcion_Negocio
        holder.nombreNegocio.text = negocio.nombre_Negocio
        holder.email.text = negocio.email_Negocio
        holder.telefono.text = negocio.ubicacion


    }
    override fun getItemCount(): Int {
        return negocioList.size
    }

    inner class MyViewHolderHistorial(itemView : View) : RecyclerView.ViewHolder(itemView){
        var descripcionNegocio : TextView = itemView.findViewById(R.id.campoDescripcion2 )

        val  nombreNegocio : TextView = itemView.findViewById(R.id.campoNegocio1 )
        val  email : TextView = itemView.findViewById(R.id.campoEmail4 )
        val  telefono : TextView = itemView.findViewById(R.id.campotelefono3 )
        val image: ImageView = itemView.findViewById(R.id.negocioImagen)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(negocioLista[adapterPosition])
            }
        }
    }
}