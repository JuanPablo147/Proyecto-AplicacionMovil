package com.example.myapplication.ui.negocios

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ImageAdapter
import com.example.myapplication.Negocio
import com.example.myapplication.R
import com.google.firebase.firestore.*

class NegocioFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var negocioArrayList: ArrayList<Negocio>
    private lateinit var myAdapter: ImageAdapter
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

        myAdapter = ImageAdapter(negocioArrayList,root.context)
        recyclerView.adapter = myAdapter
        EventChangeListener()
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
                myAdapter.notifyDataSetChanged()
            }
        }
        )

    }


}