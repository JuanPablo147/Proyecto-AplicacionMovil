package com.example.myapplication
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityInicioBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var negocioArrayList: ArrayList<Negocio>
    private lateinit var myadapter: Myadapter
    //private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_negocio)
        recyclerView = findViewById(R.id.gridd_view_items)
        recyclerView.layoutManager = LinearLayoutManager(this )
        recyclerView.setHasFixedSize(true)

        negocioArrayList = arrayListOf()
        EventChangeListener()
        myadapter = Myadapter(negocioArrayList)
        recyclerView.adapter = myadapter
        myadapter!!.notifyDataSetChanged()

        /*val buttonClick = findViewById<Button>(R.id.button41)
        buttonClick.setOnClickListener {
            myadapter = Myadapter(negocioArrayList)
            recyclerView.adapter = myadapter
            myadapter!!.notifyDataSetChanged()
        }*/





    }

    private fun EventChangeListener()  {

        var db = Firebase.firestore

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


