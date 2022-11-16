package com.example.myapplication.ui.slideshow

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.InicioActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSlideshowBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SlideshowFragment : Fragment() {


    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View {

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val EntradaNombreProducto = root.findViewById<TextInputEditText>(R.id.EntradaNombreProducto)
        val EntradaCosto = root.findViewById<TextInputEditText>(R.id.EntradaCosto)
        val EntradaDescripcion = root.findViewById<TextInputEditText>(R.id.EntradaDescripcion)
        val db = Firebase.firestore
        val intent = Intent (activity, InicioActivity::class.java)

        val buttonClick = root.findViewById<Button>(R.id.button3)





                buttonClick.setOnClickListener {
                    //Toast.makeText(activity, "Click on : " + texto.editableText.toString(), Toast.LENGTH_LONG).show()
                    var costoNum=0


                        try {
                            costoNum=EntradaCosto.editableText.toString().toInt()
                        }catch (e: Exception){

                        }
                    if(noEsVacia(EntradaNombreProducto, EntradaCosto, EntradaDescripcion)){
                        val producto = hashMapOf(
                            "Nombre_Producto" to EntradaNombreProducto.editableText.toString(),
                            "Costo" to costoNum,
                            "Descripcion" to EntradaDescripcion.editableText.toString()
                        )
                        db.collection("productos")
                            .add(producto)
                            .addOnSuccessListener { documentReference ->
                                //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                Toast.makeText(activity,"Producto agregado con el ID: ${documentReference.id}",Toast.LENGTH_LONG).show()
                                startActivity(intent)

                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                        EntradaNombreProducto.setText("")
                        EntradaCosto.setText("")
                        EntradaDescripcion.setText("")
                    }else{

                    }


                }




        //Toast.makeText(activity, "Click on : " +lista.toString() , Toast.LENGTH_LONG).show()
        //esVacia(EntradaNombreProducto, EntradaCosto, EntradaDescripcion, buttonClick)
        return root


    }


    fun noEsVacia(EntradaNombreProducto: TextInputEditText, EntradaCosto: TextInputEditText, EntradaDescripcion: TextInputEditText):Boolean {
        var flag = true


        if (EntradaNombreProducto.editableText.toString().isEmpty()) {
            Toast.makeText(activity, "Campo nombre vacio", Toast.LENGTH_LONG).show()
            EntradaNombreProducto.error = "llenar campo";
            flag = false
        }
        if (EntradaCosto.editableText.toString().isEmpty()) {
            Toast.makeText(activity, "Campo costo vacio", Toast.LENGTH_LONG).show()
            EntradaCosto.error = "llenar campo";
            flag =false
            }
        if (EntradaDescripcion.editableText.toString().isEmpty()) {
            Toast.makeText(activity, "Campo descripcion vacio", Toast.LENGTH_LONG).show()
            EntradaDescripcion.error = "llenar campo";
            flag = false
         }
        if (EntradaDescripcion.length() > 50 || EntradaDescripcion.length() < 3) {
            Toast.makeText(activity, "Descripcion no valida", Toast.LENGTH_SHORT).show()
            EntradaDescripcion.error = "debe tener 3 a 50 caracteres";
            flag = false
        }
       /* if (EntradaCosto.length() > 5)  {
            Toast.makeText(activity, "Costo demasiado elevado", Toast.LENGTH_SHORT).show()
            flag = false
        }*/
        if (EntradaNombreProducto.length() > 50 || EntradaNombreProducto.length() < 3) {
            Toast.makeText(activity, "Nombre de Producto no valido", Toast.LENGTH_SHORT).show()
            EntradaNombreProducto.error = "debe tener 3 a 50 caracteres";
            flag = false
        }





        return flag
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}