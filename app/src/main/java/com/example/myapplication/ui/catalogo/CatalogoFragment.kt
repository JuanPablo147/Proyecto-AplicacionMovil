package com.example.myapplication.ui.catalogo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.myapplication.CustomAdapter
import com.example.myapplication.Producto
import com.example.myapplication.R
import com.example.myapplication.SimpleDialog
import com.google.firebase.firestore.*


class CatalogoFragment : Fragment() {

    private var adapter: CustomAdapter? = null
    private lateinit var arrayListProducto: ArrayList<Producto>
    private lateinit var db: FirebaseFirestore
    private lateinit var itemsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.fragment_catalogo , container ,false)
        itemsListView = root.findViewById(R.id.list_view_items)
        arrayListProducto= arrayListOf()


        adapter = CustomAdapter(requireActivity(), arrayListProducto)
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

                        val dialog= SimpleDialog.newInstance(
                            arrayListProducto[position].nombre_Producto.toString(),
                            arrayListProducto[position].negocio.toString(),
                            arrayListProducto[position].costo.toString(),
                            arrayListProducto[position].tipo_comida.toString(),
                            arrayListProducto[position].descripcion.toString(),
                            arrayListProducto[position].estado.toString()
                        )
                        dialog.show(this.childFragmentManager, SimpleDialog.TAG)
                        

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
                        val productFirebase = dc.document.toObject(Producto::class.java)
                        productFirebase.id_product = dc.document.id
                        arrayListProducto.add(productFirebase)

                    }
                }
                adapter!!.notifyDataSetChanged()
            }
        }
        )

    }

}