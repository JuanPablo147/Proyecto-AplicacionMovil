package com.example.myapplication

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CRUD {

    private val db = Firebase.firestore
    private  val arrayListSecuencia = ArrayList<Secuencia>()
    fun obtenerTablaSecuencia(valorCampo: String ) {

        db.collection("seq_id").whereEqualTo("nombre_tabla", valorCampo).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data} ==>${document.id::class}")
                    val secuenciaTabla = document.toObject<Secuencia>()
                    secuenciaTabla.id = document.id
                    arrayListSecuencia.add(secuenciaTabla)
                }
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
    fun actualizarTablaSecuencia() {
        for (seq in arrayListSecuencia) {
            //Log.d(TAG, "================> ${seq.id}")
            //Toast.makeText(activity, "================> ${seq.toString()}", Toast.LENGTH_LONG).show()
            seq.seq_tabla+=1
            //Toast.makeText(activity, "================> $seq", Toast.LENGTH_LONG).show()
            db.collection("seq_id").document(seq.id).update("seq_tabla", seq.seq_tabla)

        }
    }
}