package com.example.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment :  Fragment() {
    //val pref = requireContext().dataStore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root: View = inflater.inflate(R.layout.fragment_home , container ,false)
        val boton1 = root.findViewById<Button>(R.id.button4)

        boton1.setOnClickListener {
            val intent = Intent (activity, Mostrar_Comida::class.java)
            startActivity(intent)
        }

        val boton2 = root.findViewById<Button>(R.id.button8)

        boton2.setOnClickListener {
            val intent = Intent (activity, Mostrar_Bebida::class.java)
            startActivity(intent)
        }
        /*val imageBoton = root.findViewById<ImageButton>(R.id.imageButton)
        imageBoton.setOnClickListener{
            Toast.makeText(activity, "False", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO){
                saveValues("Jorge Ledezma",true)
            }
        }
        val imageBoton1 = root.findViewById<ImageButton>(R.id.imageButton1)
        imageBoton1.setOnClickListener{
            Toast.makeText(activity, "False", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO){
                saveValues("Pedro Baltazar",false)


            }
            val intent = Intent(activity, InicioActivity::class.java)
            startActivity(intent)
        }*/







        return root




    }
    /*private suspend fun saveValues(name:String,checked:Boolean){
        val pref = requireContext().dataStore
        pref.edit { preferences ->
            preferences[stringPreferencesKey("name")] = name
            preferences[booleanPreferencesKey("vip")] = checked
        }

    }*/





}