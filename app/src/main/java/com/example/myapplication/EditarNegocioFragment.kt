package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.myapplication.ui.negocio.Negocios
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File


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
    private val cropImage = this.registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {

            uriContent = result.uriContent
            result.getUriFilePath(context!!)
            image.setImageURI(uriContent)
            ubicacionImagen= subirAFireBase()
        } else {

            result.error
        }
    }


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
        entradaEmailNegocio.setOnClickListener {
            alPresionar("No puede cambiar de e-mail")
        }
        val button : Button = root.findViewById(R.id.buttonModificarImagen)
        buttonRegistrarseN  = root.findViewById(R.id.buttonModificarNegocio)
        button.setOnClickListener {

            cropImage.launch(
                options(uri = imageUri) {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setActivityTitle("Cargar Imagen")
                    setAspectRatio(1, 1)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)


                }

            )

            image.setImageURI(imageUri)


        }
        buttonRegistrarseN.setOnClickListener {
            if (validarCampos(entradaNombreNegocio, entradaUbicacion, entradaEmailNegocio, entradaDescripcionNegocio,entradatelefono)){
                val business = Negocios(
                    Nombre_Negocio= entradaNombreNegocio.editableText.toString(),
                    Descripcion_Negocio= entradaDescripcionNegocio.editableText.toString(),
                    id_negocio= id_negocios,
                    suma_Promedio=suma_Promedio,
                    numero_N =numero_N,
                    Ubicacion=entradaUbicacion.editableText.toString(),
                    Email_Negocio=entradaEmailNegocio.editableText.toString(),
                    Ruta_imagen=rutaImagen,
                    telefono = entradatelefono.editableText.toString().toInt()
                )
                //Toast.makeText(context, "Es ${business.nombre_Negocio},${business.id_negocio},${business.descripcion_Negocio},${business.ubicacion}" + ",${business.email_Negocio},${business.ruta_imagen},${business.numero_N}\",${business.suma_Promedio}\"", Toast.LENGTH_LONG).show()
                db.collection("Business").document(business.id_negocio).set(business, SetOptions.merge())


                val intent = Intent(activity, InicioActivity::class.java)
                intent.putExtra("negocio", entradaNombreNegocio.text.toString())
                intent.putExtra("email", entradaEmailNegocio.text.toString())
                startActivity(intent)
                lifecycleScope.launch(Dispatchers.IO){
                    saveValues(entradaEmailNegocio.text.toString(),entradaNombreNegocio.text.toString(), checked = true, checked2 = false)
                }

            }
        }
        return root
    }
    private fun alPresionar(texto :String) {
        Toast.makeText(activity, texto, Toast.LENGTH_SHORT).show()
    }
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }

        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
    private suspend fun saveValues(name:String,negocio:String,checked:Boolean,checked2:Boolean){
        requireContext().dataStore.edit { preferences ->
            preferences[stringPreferencesKey("name")] = name
            preferences[stringPreferencesKey("negocio")] = negocio
            preferences[booleanPreferencesKey("vip")] = checked
            preferences[booleanPreferencesKey("producto")] = checked2
        }

    }
    private fun subirAFireBase() : Uri?{
        Toast.makeText(activity, "Por favor, espere un mientras se carga la imagen", Toast.LENGTH_LONG).show()
        buttonRegistrarseN.isClickable = false
        buttonRegistrarseN.isEnabled = false
        var downloadUri: Uri? = null
        val storage = Firebase.storage("gs://fastfood-a4739.appspot.com")
        val storageRef = storage.reference
        val sd = getFileName(requireContext(), uriContent!!)
        val file = Uri.fromFile(File(sd.toString()))
        val mountainsRef = storageRef.child(file.lastPathSegment!!)
        val mountainImagesRef = storageRef.child("images/${file.lastPathSegment}")
        mountainsRef.name == mountainImagesRef.name // true
        mountainsRef.path == mountainImagesRef.path // false
        image.isDrawingCacheEnabled = true
        image.buildDrawingCache()
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri = task.result
                rutaImagen=downloadUri.toString()
                //Toast.makeText(activity, ">>>>>>>>> $downloadUri", Toast.LENGTH_LONG).show()
                buttonRegistrarseN.isClickable = true
                buttonRegistrarseN.isEnabled = true
            } else {
                // Handle failures
                // ...
            }
        }
        return downloadUri
    }
    private fun eventChangeListenerNombre(){
        val db = Firebase.firestore
        db.clearPersistence()

        db.collection("Business").addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        negocioArrayList1.add(dc.document.toObject(Negocios::class.java).nombre_Negocio)
                    }
                }
            }
        }
        )

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
        eventChangeListenerNombre()
    }
    private fun comprobarNombre(nombre:String):Boolean{

        var flag = false
        if(nombre == miNombreNegocio){
            return false
        }
        if(negocioArrayList1.isNotEmpty()){
            for (negocio in negocioArrayList1){


                if(negocio.trim().equals(nombre.trim(), ignoreCase = true)){
                    return true

                }
            }}
        return flag
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