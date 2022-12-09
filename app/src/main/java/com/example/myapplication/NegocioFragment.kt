package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.negocio.Negocios
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"





class NegocioFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var negocioArrayList: ArrayList<Negocios>
    private lateinit var myadapter: ImageAdapter
    private lateinit var db: FirebaseFirestore




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root: View = inflater.inflate(R.layout.fragment_negocio , container ,false)
        recyclerView = root.findViewById(R.id.gridd_view_items)
        recyclerView.layoutManager = LinearLayoutManager(activity )
        recyclerView.setHasFixedSize(true)
        negocioArrayList = arrayListOf()

        myadapter = ImageAdapter(negocioArrayList,root.context)
        recyclerView.adapter = myadapter
        eventChangeListener()

        myadapter.onItemClick = { contact ->

            // do something with your item
            Toast.makeText(activity, contact.nombre_Negocio, Toast.LENGTH_LONG).show()
        }


        return root
    }
    override fun onResume() {
        super.onResume()
        myadapter.onItemClick = { contact ->
            val promedio: Float = if(contact.numero_N !=0){
                (contact.suma_Promedio/contact.numero_N).toFloat()
            }else{
                (0).toFloat()
            }
            // do something with your item
            //Toast.makeText(activity, contact.nombre_Negocio, Toast.LENGTH_LONG).show()
            //db.collection("BusinessV2").document(contact.id).set(contact, SetOptions.merge())
            NegocioDialog.newInstance(
                contact.nombre_Negocio,
                contact.email_Negocio,
                contact.descripcion_Negocio,
                contact.ubicacion,
                contact.telefono.toString(),
                promedio

            ).show(this.childFragmentManager, NegocioDialog.TAG)



        }
    }


    private fun eventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("Business").addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        val miNegocio :Negocios =dc.document.toObject(Negocios::class.java)
                        //miNegocio.id= dc.document.id
                        //miNegocio.sumaPromedio =0.0
                        //miNegocio.numeroN=0
                        negocioArrayList.add(miNegocio)

                    }
                }
                myadapter.notifyDataSetChanged()
            }
        }
        )

    }


}

