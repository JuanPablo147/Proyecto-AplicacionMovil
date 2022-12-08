package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var negocioArrayList: ArrayList<Negocio>
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
        val negocio1: Negocio = Negocio("Descripcion1", "email", "nombre1", "ruta", "ubicacion")
        val negocio2: Negocio = Negocio("Descripcion2", "email", "nombre2", "ruta", "ubicacion")
        val negocio3: Negocio = Negocio("Descripcion3", "email", "nombre3", "ruta", "ubicacion")
        val negocio4: Negocio = Negocio("Descripcion4", "email", "nombre4", "ruta", "ubicacion")
        negocioArrayList = arrayListOf()

        myadapter = ImageAdapter(negocioArrayList,root.context)
        recyclerView.adapter = myadapter
        EventChangeListener()
        /*negocioArrayList.add(negocio1)
        negocioArrayList.add(negocio2)
        negocioArrayList.add(negocio3)
        negocioArrayList.add(negocio4)*/




        return root
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("negocios").addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                    negocioArrayList.add(dc.document.toObject(Negocio::class.java))

                    }
                }
                myadapter.notifyDataSetChanged()
            }
        }
        )

    }

    private fun cargarFirebase()  {

        db = Firebase.firestore

        val docRef = db.collection("negocios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                   // Log.d(TAG, "${document.id} => ${document.data}")
                    val miProducto = document.toObject<Negocio>()

                    negocioArrayList.add(miProducto)
                    Log.d(ContentValues.TAG, "$miProducto =>")
                }


            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }


    }

}

