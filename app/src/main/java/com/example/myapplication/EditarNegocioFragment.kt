package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.myapplication.ui.negocio.Negocios
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditarNegocioFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var negocioArrayList: ArrayList<Negocios>
    private var uriContent: Uri? = null
    private lateinit var image: ImageView
    private  lateinit var buttonRegistrarseN : Button
    private var ubicacionImagen: Uri? = null
    private var imageUri: Uri? = null
    private var rutaImagen: String =""
    private var miNombreNegocio: String =""
    private var id_negocios: String =""
    private var suma_Promedio:Double =0.0
    private var numero_N : Int=0
    private var telefono : Int=0
    private lateinit var negocioArrayList1: ArrayList<String>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val root: View = inflater.inflate(R.layout.fragment_editar_negocio, container, false)
        negocioArrayList = arrayListOf()
        negocioArrayList1 = arrayListOf()

        val entradaNombreNegocio : TextInputEditText = root.findViewById(R.id.EntradaNombreNegocio123)
        val entradaUbicacion : TextInputEditText = root.findViewById(R.id.EntradaUbicacion123)
        val entradaEmailNegocio  : TextInputEditText = root.findViewById(R.id.EntradaEmailNegocio123)
        val entradaDescripcionNegocio : TextInputEditText = root.findViewById(R.id.EntradaDescripcionNegocio123)
        val entradatelefono = root.findViewById<TextInputEditText>(R.id.telefonos)
        image= root.findViewById(R.id.imageView1)
        lifecycleScope.launch(Dispatchers.IO){
            getBusinessProfile().collect{
                withContext(Dispatchers.Main){
                    eventChangeListener(it.id_business, entradaNombreNegocio, entradaUbicacion, entradaEmailNegocio, entradaDescripcionNegocio,entradatelefono,image)


                }
            }
        }

        return root
    }

    private fun eventChangeListener(id_negocio: String
                                    ,entradaNombreNegocio :  TextInputEditText
                                    ,entradaUbicacion :  TextInputEditText
                                    ,entradaEmailNegocio :  TextInputEditText
                                    ,entradaDescripcionNegocio :  TextInputEditText
                                    ,entradatelefono: TextInputEditText
                                    ,image: ImageView ){


        db = FirebaseFirestore.getInstance()
        //db.clearPersistence()
        db.collection("Business").whereEqualTo("id_negocio",id_negocio).addSnapshotListener(object :
            EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        val miNegocio : Negocios =dc.document.toObject(Negocios::class.java)
                        //Toast.makeText(context,miNegocio.nombre_Negocio,Toast.LENGTH_LONG).show()
                        entradaNombreNegocio.setText(miNegocio.nombre_Negocio)
                        entradaUbicacion.setText(miNegocio.ubicacion)
                        entradaEmailNegocio.setText(miNegocio.email_Negocio)
                        entradaEmailNegocio.isFocusable = false
                        entradaEmailNegocio.isClickable = false
                        id_negocios= miNegocio.id_negocio
                        rutaImagen = miNegocio.ruta_imagen
                        suma_Promedio= miNegocio.suma_Promedio
                        numero_N=miNegocio.numero_N
                        telefono= miNegocio.telefono!!
                        miNombreNegocio = miNegocio.nombre_Negocio
                        entradaDescripcionNegocio.setText(miNegocio.descripcion_Negocio)
                        entradatelefono.setText(miNegocio.telefono.toString())
                        image.let { context?.let { it1 -> Glide.with(it1).load(miNegocio.ruta_imagen).error(R.drawable.ic_baseline_storefront_24) .into(it) } }
                        negocioArrayList.add(miNegocio)

                    }
                }

            }
        }
        )
    }
    private fun validarCampos(entradaNombreNegocio: TextInputEditText, entradaUbicacion: TextInputEditText,
                              entradaEmailNegocio: TextInputEditText, entradaDescripcionNegocio: TextInputEditText,entradatelefono: TextInputEditText):Boolean{

        var flag = true

        if(comprobarNombre(entradaNombreNegocio.editableText.toString() )){
            entradaNombreNegocio.error = "Este nombre ya esta asociado a otro negocio"
            return false
        }
        if (entradaNombreNegocio.editableText.toString().isEmpty()) {

            entradaNombreNegocio.error = "llenar campo"
            return false
        }
        if (entradaUbicacion.editableText.toString().isEmpty()) {

            entradaUbicacion.error = "llenar campo"
            return false
        }

        if (entradaDescripcionNegocio.editableText.toString().isEmpty()) {

            entradaDescripcionNegocio.error = "Debe llenar campo"
            return false
        }
        if (entradaNombreNegocio.editableText.toString().length > 30 || entradaNombreNegocio.editableText.toString().length < 2) {

            entradaNombreNegocio.error = "Debe tener 3 a 30 caracteres"
            return false
        }
        if (entradaUbicacion.editableText.toString().length > 30 || entradaUbicacion.editableText.toString().length < 2) {

            entradaUbicacion.error = "Debe tener 3 a 30 caracteres"
            return false
        }

        if (entradaDescripcionNegocio.editableText.toString().length > 50 || entradaDescripcionNegocio.editableText.toString().length < 2) {

            entradaDescripcionNegocio.error = "Debe tener 3 a 50 caracteres"
            return false
        }
        if (entradatelefono.editableText.toString().length != 8) {

            entradatelefono.error = "Debe ingresar un numero de telefono valido de 8 digitos"
            return false
        }

        return flag

    }
    private fun getBusinessProfile()= requireContext().dataStoreBusiness.data.map { preferences->
        BusinessProfile(
            id_business = preferences[stringPreferencesKey("business_id")].orEmpty(),
            flag = preferences[booleanPreferencesKey("vip_b")]?: false
        )


    }

}