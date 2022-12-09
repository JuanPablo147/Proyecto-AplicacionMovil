package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.negocio.Negocios
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HistorialFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var texto : TextView
    private lateinit var negocioArrayList: ArrayList<Negocios>
    private lateinit var myadapter: HistorialAdapter

    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_historial, container, false)
        recyclerView = root.findViewById(R.id.gridd_view_items_hist)
        recyclerView.layoutManager = LinearLayoutManager(activity )
        recyclerView.setHasFixedSize(true)
        negocioArrayList = arrayListOf()
        texto = root.findViewById(R.id.titulo_hist)
        texto.text = "Usted no cuenta con compras registradas"

        myadapter = HistorialAdapter(negocioArrayList,root.context)
        recyclerView.adapter = myadapter
        lifecycleScope.launch(Dispatchers.IO){
            getUserProfile().collect{
                withContext(Dispatchers.Main){
                    obtenerNegocios(it.name)
                }
            }
        }

        myadapter.onItemClick = { contact ->

            // do something with your item
            Toast.makeText(activity, contact.nombre_Negocio, Toast.LENGTH_LONG).show()
        }
        return root
    }
    private fun getUserProfile()= requireContext().dataStore.data.map { preferences->

        UserProfile(
            name =  preferences[stringPreferencesKey("name")].orEmpty(),
            negocioName =  preferences[stringPreferencesKey("negocio")].orEmpty(),
            negocio = preferences[booleanPreferencesKey("vip")] ?: false,
            producto = preferences[booleanPreferencesKey("producto")] ?: false
        )

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
            HistorialDialog.newInstance(
                contact.nombre_Negocio,
                contact.email_Negocio,
                contact.descripcion_Negocio,
                contact.ubicacion,
                contact.id_negocio,
                promedio

            ).show(this.childFragmentManager, HistorialDialog.TAG)



        }
    }


    private fun obtenerNegocios(correo_cliente: String){

        db.collection("Historial").whereEqualTo("correo_cliente",correo_cliente).addSnapshotListener(object :
            EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        val miHistorialCompra : HistorialCompra =dc.document.toObject(HistorialCompra::class.java)
                        miHistorialCompra.id_historial=dc.document.id
                        db.collection("Historial").document(miHistorialCompra.id_historial).set(miHistorialCompra, SetOptions.merge())
                        eventChangeListener(miHistorialCompra)
                    }
                }
            }
        }
        )

    }


    private fun eventChangeListener(miHistorialCompra: HistorialCompra,) {
        texto.text = "Negocios"
        db.clearPersistence()
        db.collection("Business").whereEqualTo("id_negocio",miHistorialCompra.id_negocio).addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        val miNegocio :Negocios =dc.document.toObject(Negocios::class.java)
                        miNegocio.suma_Promedio= miHistorialCompra.puntuacion.toDouble()
                        miNegocio.numero_N= 1
                        miNegocio.descripcion_Negocio=miHistorialCompra.nombre_Producto_Ultimo
                        miNegocio.ubicacion=miHistorialCompra.fecha_ultima_compra
                        miNegocio.email_Negocio=miHistorialCompra.numero_producto_comprado.toString()
                        negocioArrayList.add(miNegocio)

                    }
                }
                myadapter.notifyDataSetChanged()
            }
        }
        )

    }


}