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
import androidx.core.view.isNotEmpty
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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCE_NAME")
class InicioActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityInicioBinding
    private lateinit var easyEmail:String

    private lateinit var negocioArrayList: ArrayList<Negocio>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        negocioArrayList = arrayListOf()

        try{
            val intent = intent
            val easyPuzzle = intent.extras!!.getString("epuzzle")
            if (easyPuzzle != null) {
                if(easyPuzzle.isNotBlank()){
                    //Toast.makeText(this, easyPuzzle, Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch(Dispatchers.IO){
                        saveValues(easyPuzzle,"", checked = true, checked2 = false)
                    }
                    eventChangeListener(easyPuzzle)
                    /*val negocioNOmbre = verificar(easyPuzzle)
                    Toast.makeText(this,"${easyPuzzle} y tambien ${negocioNOmbre}", Toast.LENGTH_SHORT).show()
                    if(negocioNOmbre.isNotEmpty()){
                        lifecycleScope.launch(Dispatchers.IO){
                            saveValues(easyPuzzle,negocioNOmbre, checked = false, checked2 = true)
                        }
                    }else{
                        lifecycleScope.launch(Dispatchers.IO){
                            saveValues(easyPuzzle,"", checked = true, checked2 = false)
                        }
                    }*/
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
                    //Toast.makeText(this, easyPuzzle, Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch(Dispatchers.IO){
                        saveValues(easyPuzzle1,easyPuzzle, checked = false, checked2 = true)
                    }}
            }
        }catch (_:Exception){

        }




        setSupportActionBar(binding.appBarInicio.toolbar)

        //binding.appBarInicio.fab.setOnClickListener { view ->
        //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //       .setAction("Action", null).show()
        //}
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_inicio)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val navMenu: Menu = navView.menu

        val header  = navView.getHeaderView(0)
        val bienvenido = header.findViewById<TextView>(R.id.textNombreBienvenido)
        val textoNegocio = header.findViewById<TextView>(R.id.textNegocio)
        val nombreNegocio = header.findViewById<TextView>(R.id.textNombreNegocio)
        //bienvenido.text="Invitado"

        lifecycleScope.launch(Dispatchers.IO){
            getUserProfile().collect{
                withContext(Dispatchers.Main){

                    try {
                        //Toast.makeText(this@InicioActivity, " ${it.name},${it.negocio},${it.negocioName},${it.producto},", Toast.LENGTH_LONG).show()
                        easyEmail=it.name
                        bienvenido.text= it.name
                        navMenu.findItem(R.id.nav_crear_negocio).isVisible = it.negocio
                        navMenu.findItem(R.id.nav_slideshow).isVisible = it.producto

                        if(it.producto){
                            textoNegocio.text="Negocio: "
                            if(it.negocioName .isNotEmpty() && it.negocioName.isNotBlank()){
                                nombreNegocio.text=it.negocioName
                                navMenu.findItem(R.id.nav_editar_negocio).isVisible = false
                                navMenu.findItem(R.id.nav_crear_negocio).isVisible = false}
                        }else{
                            navMenu.findItem(R.id.nav_editar_negocio).isVisible = false
                        }

                    }catch (_:Exception){

                    }

                }
            }
        }

        /*navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_slideshow -> {
                    Toast.makeText(this, easyEmail, Toast.LENGTH_SHORT).show()
                    var email:TextView=findViewById(R.id.EntradaEmailNegocio)
                    email.text= easyEmail
                    true
                }
                else -> false
            }
        }*/
        //navView.setNavigationItemSelectedListener(this)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_logout,R.id.nav_catalog,R.id.nav_negocio,R.id.nav_crear_negocio
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

    private fun eventChangeListener(email:String){
        val db = Firebase.firestore
        db.clearPersistence()


        db.collection("negocios").whereEqualTo("Email_Negocio", email).addSnapshotListener(object :
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
                        val minegocio:Negocio = dc.document.toObject(Negocio::class.java)

                        negocioArrayList.add(minegocio)
                        if(minegocio.email_Negocio.isNotEmpty()){
                            lifecycleScope.launch(Dispatchers.IO){
                                saveValues(minegocio.email_Negocio,minegocio.nombre_Negocio, checked = false, checked2 = true)



                            }
                        }
                        //Toast.makeText(this@InicioActivity, minegocio.toString(), Toast.LENGTH_LONG).show()
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


    /*
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_slideshow -> {
                Toast.makeText(this, easyEmail, Toast.LENGTH_SHORT).show()
                var email:TextView=findViewById(R.id.EntradaEmailNegocio)
                email.text= easyEmail
                true
            }

        }

        //drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }*/
    /*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present.
        // menuInflater.inflate(R.id.app_bar_switch)
        val switch_id  :Switch= findViewById(R.id.switchItem)

        switch_id.let {
            it.isChecked = true;

            it.setOnCheckedChangeListener { _, b ->

                if (b){
                    Toast.makeText(this, "True", Toast.LENGTH_SHORT).show()
                    println("true")
                    openItem()
                } else {
                    Toast.makeText(this, "False", Toast.LENGTH_SHORT).show()
                    println("false")
                    hideItem()
                }
            }
        }



        return true
    }
    private fun openItem() {
        val navigationView: NavigationView = findViewById(R.id.nav_view) as NavigationView
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_slideshow).isVisible = true
        nav_Menu.findItem(R.id.nav_crear_negocio).isVisible = true
    }
        private fun hideItem() {
        val navigationView: NavigationView = findViewById(R.id.nav_view) as NavigationView
        val nav_Menu: Menu = navigationView.menu
            nav_Menu.findItem(R.id.nav_slideshow).isVisible = false
            nav_Menu.findItem(R.id.nav_crear_negocio).isVisible = false
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_slideshow -> {
                Toast.makeText(this, easyEmail, Toast.LENGTH_SHORT).show()
                var email:TextView=findViewById(R.id.EntradaEmailNegocio)
                email.text= easyEmail
                true
            }

        }
        return true
    }*/

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
                finishAffinity();
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
    /*
    fun comprobarNegocio(email:String){
        db.collection("negocio")
            .whereEqualTo("capital", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }*/


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_inicio)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
/*
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_slideshow -> {
                Toast.makeText(this, easyEmail, Toast.LENGTH_SHORT).show()
                //val email:TextView=findViewById(R.id.EntradaEmailNegocio)
                //email.text= easyEmail

            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }*/
}