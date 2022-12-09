package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityInicioBinding
import com.example.myapplication.ui.negocio.Negocios
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCE_NAME")
val Context.dataStoreBusiness by preferencesDataStore(name = "BUSINESS")

class InicioActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityInicioBinding
    private lateinit var easyEmail:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try{
            val intent = intent
            val easyPuzzle = intent.extras!!.getString("epuzzle")
            if (easyPuzzle != null) {
                if(easyPuzzle.isNotBlank()){
                    lifecycleScope.launch(Dispatchers.IO){
                        saveValues(easyPuzzle,"", checked = true, checked2 = false)
                    }
                    eventChangeListener(easyPuzzle)

                }
            }
        }catch (_:Exception){

        }
        try{
            val intent = intent
            val easyPuzzle = intent.extras!!.getString("negocio")
            val easyPuzzle1 = intent.extras!!.getString("email")
            if (easyPuzzle != null && easyPuzzle1 !=null) {
                if(easyPuzzle.isNotBlank()){

                    lifecycleScope.launch(Dispatchers.IO){
                        saveValues(easyPuzzle1,easyPuzzle, checked = false, checked2 = true)
                    }
                    eventChangeListener(easyPuzzle)
                }
            }
        }catch (_:Exception){

        }




        setSupportActionBar(binding.appBarInicio.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_inicio)

        val navMenu: Menu = navView.menu

        val header  = navView.getHeaderView(0)
        val bienvenido = header.findViewById<TextView>(R.id.textNombreBienvenido)
        val textoNegocio = header.findViewById<TextView>(R.id.textNegocio)
        val nombreNegocio = header.findViewById<TextView>(R.id.textNombreNegocio)


        lifecycleScope.launch(Dispatchers.IO){
            getUserProfile().collect{
                withContext(Dispatchers.Main){

                    try {

                        easyEmail=it.name
                        bienvenido.text= it.name
                        navMenu.findItem(R.id.nav_crear_negocio).isVisible = it.negocio
                        navMenu.findItem(R.id.nav_slideshow).isVisible = it.producto

                        if(it.producto){
                            textoNegocio.text="Negocio: "
                            if(it.negocioName .isNotEmpty() && it.negocioName.isNotBlank()){
                                nombreNegocio.text=it.negocioName
                                navMenu.findItem(R.id.nav_editar_negocio).isVisible = true

                                navMenu.findItem(R.id.nav_crear_negocio).isVisible = false}
                        }else{
                            navMenu.findItem(R.id.nav_editar_negocio).isVisible = false

                        }

                    }catch (_:Exception){

                    }

                }
            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_slideshow,R.id.nav_logout,R.id.nav_catalog,R.id.nav_negocio,R.id.nav_crear_negocio
            ), drawerLayout
        )




        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }
    private suspend fun saveValues(name:String,negocio:String,checked:Boolean,checked2:Boolean){
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("name")] = name
            preferences[stringPreferencesKey("negocio")] = negocio
            preferences[booleanPreferencesKey("vip")] = checked
            preferences[booleanPreferencesKey("producto")] = checked2
        }

    }


    private suspend fun saveValuesBusiness(business_id:String,checked:Boolean){
        dataStoreBusiness.edit { preferences ->
            preferences[stringPreferencesKey("business_id")] = business_id
            preferences[booleanPreferencesKey("vip")] = checked
        }

    }

    private fun eventChangeListener(email:String){
        val db = Firebase.firestore
        db.clearPersistence()


        db.collection("Business").whereEqualTo("email_Negocio", email).addSnapshotListener(object :
            EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())

                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        //Toast.makeText(this@InicioActivity, " iniciando", Toast.LENGTH_LONG).show()
                        val minegocio: Negocios = dc.document.toObject(Negocios::class.java)
                        if(minegocio.id_negocio.isNullOrEmpty() || minegocio.id_negocio.isBlank()){
                            minegocio.id_negocio = dc.document.id
                            db.collection("Business").document(minegocio.id_negocio).set(minegocio, SetOptions.merge())
                        }
                        if(minegocio.email_Negocio.isNotEmpty()){
                            lifecycleScope.launch(Dispatchers.IO){
                                saveValuesBusiness(minegocio.id_negocio,checked = true)
                                saveValues(minegocio.email_Negocio,minegocio.nombre_Negocio, checked = false, checked2 = true)

                            }
                        }

                    }

                }

            }

        }
        )
    }
    private fun getUserProfile()= dataStore.data.map { preferences->
        UserProfile(
            name =  preferences[stringPreferencesKey("name")].orEmpty(),
            negocioName =  preferences[stringPreferencesKey("negocio")].orEmpty(),
            negocio = preferences[booleanPreferencesKey("vip")] ?: false,
            producto = preferences[booleanPreferencesKey("producto")] ?: false
        )

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater.inflate(R.menu.main, menu)



        return true
    }
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)

        lifecycleScope.launch(Dispatchers.IO){
            getUserProfile().collect{
                withContext(Dispatchers.Main){

                    try {
                        //Toast.makeText(this@InicioActivity, " ${it.name},${it.negocio},${it.negocioName},${it.producto},", Toast.LENGTH_LONG).show()
                        val easyEmail=it.name
                        menu.findItem(R.id.action_settings).isVisible = easyEmail != "Invitado"
                    }catch (_:Exception){
                    }
                }
            }
        }

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                val fAuth  = FirebaseAuth.getInstance()
                fAuth.signOut()
                lifecycleScope.launch(Dispatchers.IO){
                    saveValues("Invitado","", checked = false, checked2 = false)
                    saveValuesBusiness("",checked = false)
                }
                item.isVisible = false
                Toast.makeText(this, "Cerrando Sesion", Toast.LENGTH_SHORT).show()
                saveEmailUser("")
                val intent = Intent(this@InicioActivity, InicioActivity::class.java)
                finish()
                startActivity(intent)

                true
            }
            R.id.action_logout -> {
                finishAffinity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveEmailUser(email: String) {
        val sharedPref = getSharedPreferences("shared_preferences", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("email", email)
        editor.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_inicio)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}