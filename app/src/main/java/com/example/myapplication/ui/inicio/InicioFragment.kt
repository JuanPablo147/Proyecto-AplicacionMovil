package com.example.myapplication.ui.inicio

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.modelProductCar.ProductCar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class InicioFragment : Fragment() {

    private var adapter: CustomAdapter? = null
    private lateinit var arrayListProducto: ArrayList<Producto>
    private var arrayListComplete : ArrayList<Producto> = arrayListOf() // lista original de productos
    private lateinit var db: FirebaseFirestore
    private lateinit var itemsListView: ListView
    private lateinit var searchView : SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root: View = inflater.inflate(R.layout.fragment_iniciar , container ,false)
        itemsListView = root.findViewById(R.id.list_view_items)
        searchView = root.findViewById(R.id.search_products)
        arrayListProducto= arrayListOf()


        adapter = CustomAdapter(activity!!, arrayListProducto)
        itemsListView.adapter = adapter
        EventChangeListener()
        return root
    }
    override fun onResume() {
        super.onResume()

        if(itemsListView!=null){
        try {

            itemsListView.setOnItemClickListener { _, _, position, _ ->
                if(arrayListProducto[position].estado == "No disponible"){
                    Toast.makeText(context, "El producto seleccionado se encuentra agotado", Toast.LENGTH_SHORT).show()
            }else{

                    var dialog= SimpleDialog.newInstance(
                        arrayListProducto[position].nombre_Producto.toString(),
                        arrayListProducto[position].negocio.toString(),
                        arrayListProducto[position].costo.toString(),
                        arrayListProducto[position].tipo_comida.toString(),
                        arrayListProducto[position].descripcion.toString(),
                        arrayListProducto[position].estado.toString()
                    )
                    dialog.show(this.childFragmentManager, SimpleDialog.TAG)
                    dialog.actionCarrito = {
                        addProductToCar(position)
                        dialog.dismiss()
                    }

                }
            }
        } catch(_: Exception) {

        }


    }}


    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("Producto").addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        var productFirebase = dc.document.toObject(Producto::class.java)
                        productFirebase.id_product = dc.document.id
                        arrayListProducto.add(productFirebase)

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
        var sharedPref = requireContext().getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("product_car${email.replace("@","")}", json)
        editor.commit()
        Toast.makeText(requireContext(), "Producto Agregado al Carrito", Toast.LENGTH_SHORT).show()
    }

    private fun getProductCar(): ArrayList<ProductCar> {
        val email = getEmailCurrentUser()
        val sharedPref = requireContext().getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        var json = sharedPref.getString("product_car${email.replace("@","")}", "")
        var list: ArrayList<ProductCar>? =
            Gson().fromJson(json, object : TypeToken<MutableList<ProductCar>>() {}.type)
        return list ?: arrayListOf()
    }

    fun getEmailCurrentUser() : String {
        val sharedPref = requireContext().getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", "")
        return email ?: ""
    }



}