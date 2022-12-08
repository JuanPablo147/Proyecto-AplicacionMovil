package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.modelProductCar.ProductCar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Mostrar_Bebida : AppCompatActivity() {
    private var adapter: CustomAdapter? = null
    private lateinit var arrayListProducto: ArrayList<Producto>
    private lateinit var db: FirebaseFirestore
    private lateinit var itemsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_bebida)


        itemsListView = findViewById(R.id.Comi1)
        arrayListProducto= arrayListOf()


        adapter = CustomAdapter(this@Mostrar_Bebida, arrayListProducto)
        itemsListView.adapter = adapter
        EventChangeListener()
    }/*
    override fun onResume() {
        super.onResume()

        if(itemsListView!=null){
            try {

                itemsListView.setOnItemClickListener { _, _, position, _ ->

                    SimpleDialog.newInstance(
                        arrayListProducto[position].nombre_Producto.toString(),
                        arrayListProducto[position].negocio.toString(),
                        arrayListProducto[position].costo.toString(),
                        arrayListProducto[position].tipo_comida.toString(),
                        arrayListProducto[position].descripcion.toString(),
                        arrayListProducto[position].estado.toString()
                    ).show( supportFragmentManager , SimpleDialog.TAG)
                }
            } catch (_: Exception) {

            }
        }

    }*/


    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("Producto").whereEqualTo("tipo_comida","Bebida").addSnapshotListener(object :
            EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        arrayListProducto.add(dc.document.toObject(Producto::class.java))

                    }
                }
                adapter!!.notifyDataSetChanged()
            }
        }
        )

    }
    fun addProductToCar(position: Int) {
        var listProductCar: ArrayList<ProductCar> = getProductCar()
        var productSelected = arrayListProducto[position]
        //Toast.makeText(context, productSelected.ruta_imagen, Toast.LENGTH_SHORT).show()
        // busca un producto con el mismo id_product y si no existe devuelve null
        if(listProductCar.firstOrNull { it.id_product == productSelected.id_product } == null) {

            listProductCar.add(
                ProductCar(
                    costo = productSelected.costo,
                    description = productSelected.descripcion,
                    ruta_imagen = productSelected.ruta_imagen,
                    nombre_producto = productSelected.nombre_Producto,
                    id_product = productSelected.id_product,

                    cant = 1
                )
            )
            saveProductCar(listProductCar)
        } else {
            // si ya exite el producto en el carrito le aumentamos la cantidad
            var index = listProductCar.indexOfFirst { it.id_product == productSelected.id_product }
            listProductCar[index].cant++
            saveProductCar(listProductCar)
        }

    }

    private fun saveProductCar(list: ArrayList<ProductCar>) {
        //for(productSelected in list){
        //Toast.makeText(context, productSelected.ruta_imagen, Toast.LENGTH_SHORT).show()}
        val email = getEmailCurrentUser()
        var json = Gson().toJson(list)
        var sharedPref = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("product_car${email.replace("@","")}", json)
        editor.commit()
        Toast.makeText(this@Mostrar_Bebida, "Producto Agregado al Carrito", Toast.LENGTH_SHORT).show()
    }

    private fun getProductCar(): ArrayList<ProductCar> {
        val email = getEmailCurrentUser()
        val sharedPref = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        var json = sharedPref.getString("product_car${email.replace("@","")}", "")
        var list: ArrayList<ProductCar>? =
            Gson().fromJson(json, object : TypeToken<MutableList<ProductCar>>() {}.type)
        return list ?: arrayListOf()
    }

    fun getEmailCurrentUser() : String {
        val sharedPref = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", "")
        return email ?: ""
    }


}