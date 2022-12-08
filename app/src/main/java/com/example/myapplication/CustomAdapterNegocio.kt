package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toIcon
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class CustomAdapterNegocio(context: Context, items: ArrayList<Negocio>) : BaseAdapter() {

    //Passing Values to Local Variables

    var context = context

    var name = items


    //Auto Generated Method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myView = convertView
        var holder: ViewHolder

        if (myView == null) {

            val mInflater = (context as Activity).layoutInflater
            myView = mInflater.inflate(R.layout.layout_list_view_row_negocio, parent, false)
            holder =  ViewHolder()

            holder.mImageView = myView!!.findViewById(R.id.imageView12) as ImageView
            holder.mTextView = myView!!.findViewById(R.id.text_view_item_name12) as TextView
            holder.mitemDescription = myView!!.findViewById<View>(R.id.text_view_item_description12)as TextView
            myView.setTag(holder)
        } else {
            holder = myView.getTag() as ViewHolder

        }
        val currentItem = getItem(position) as Negocio
        /*val httpsReference = Firebase.storage.getReferenceFromUrl(currentItem.ruta_imagen).downloadUrl.addOnSuccessListener {
                task ->
        }.addOnFailureListener {
            // Handle any errors

        }

        val dateRef: StorageReference = Firebase.storage.getReference("images/cropped-1492178719.png ")


        val urlTask = dateRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val downloadUri = task.result

                //URL
                val url = downloadUri.toIcon()
                holder.mImageView!!.setImageIcon(url)
            } else {
                //handle failure here
            }
        }*/
        var islandRef = Firebase.storage.reference.child("images/cropped446938692.png.jpg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
        }.addOnFailureListener {
            // Handle any errors
        }
        // [END download_to_memory]

        // [START download_to_local_file]
        //islandRef = storageRef.child("images/island.jpg")

        val localFile = File.createTempFile("images", "jpg")

        islandRef.getFile(localFile).addOnSuccessListener { result->
            //holder.mImageView!!.setImageURI(result.getvalue().toString)
            // Local temp file has been created
        }.addOnFailureListener {
            // Handle any errors
        }

        //holder.mImageView!!.setImageIcon(url.result)
        holder.mTextView!!.setText(currentItem.nombre_Negocio)
        holder.mitemDescription!!.setText(currentItem.email_Negocio)

        return myView

    }
    fun cargarImagen(url :String){


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
        var mitemDescription: TextView? = null
/*
        var mImageView: ImageView? = view.findViewById<View>(R.id.imageView) as ImageView
        var mTextView: TextView? = view.findViewById<View>(R.id.text_view_item_name) as TextView
        var mitemDescription: TextView? = view.findViewById<View>(R.id.text_view_item_description) as TextView*/
    }





}
