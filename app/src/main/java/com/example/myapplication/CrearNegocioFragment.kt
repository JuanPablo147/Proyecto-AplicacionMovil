package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.myapplication.databinding.FragmentCrearNegocioBinding
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


@Suppress("DEPRECATION")
class CrearNegocioFragment : Fragment() {


    lateinit var imageView: ImageView
    lateinit var button: Button
    lateinit var buttonRegistrarseN : Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var _binding: FragmentCrearNegocioBinding? = null
    private var uriContent: Uri? = null
    private var rutaimagen: String =""
    private var ubicacionImagen: Uri? = null
    private val binding get() = _binding!!
    lateinit var emailRegistrado:String
    private lateinit var negocioArrayList: ArrayList<Negocio>
    private lateinit var negocioArrayList1: ArrayList<String>
    private val cropImage = this.registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {

            uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(context!!)
            imageView.setImageURI(uriContent)
            ubicacionImagen= subirAFireBase()
        } else {

            val exception = result.error
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrearNegocioBinding.inflate(inflater,container,false)

        val root: View = binding.root
        var email= root.findViewById<EditText>(R.id.EntradaEmailNegocio)
        negocioArrayList = arrayListOf()
        negocioArrayList1 = arrayListOf()
        EventChangeListenerNombre()
        lifecycleScope.launch(Dispatchers.IO){
            getUserProfile().collect{
                withContext(Dispatchers.Main){

                    try {
                        email.setText(it.name)
                        emailRegistrado= it.name
                        EventChangeListener(emailRegistrado)

                    }catch (_:Exception){

                    }

                }
            }
        }




        val db = Firebase.firestore
        val intent = Intent (activity, InicioActivity::class.java)
        val entradaNombreNegocio = root.findViewById<TextInputEditText>(R.id.EntradaNombreNegocio)
        val entradaUbicacion = root.findViewById<TextInputEditText>(R.id.EntradaUbicacion)
        val entradaEmailNegocio = root.findViewById<TextInputEditText>(R.id.EntradaEmailNegocio)
        val entradaDescripcionNegocio = root.findViewById<TextInputEditText>(R.id.EntradaDescripcionNegocio)
        button = root.findViewById(R.id.buttonCargarImagen)
        buttonRegistrarseN = root.findViewById(R.id.buttonRegistrarseN)
        imageView = root.findViewById(R.id.imageView1)
        button.setOnClickListener {

            cropImage.launch(
                options(uri = imageUri) {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setActivityTitle("Cargar Imagen")
                    setAspectRatio(1, 1)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)


                }

            )

            imageView.setImageURI(imageUri)


        }
        buttonRegistrarseN.setOnClickListener {

            if (validarCampos(entradaNombreNegocio, entradaUbicacion, entradaEmailNegocio, entradaDescripcionNegocio)){
                //Toast.makeText(activity, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                val negocio = hashMapOf(
                    "Nombre_Negocio" to entradaNombreNegocio.editableText.toString(),
                    "Ubicacion" to entradaUbicacion.editableText.toString(),
                    "Email_Negocio" to entradaEmailNegocio.editableText.toString(),
                    "Descripcion_Negocio" to entradaDescripcionNegocio.editableText.toString(),
                    "Ruta_imagen" to rutaimagen
                    )
                //Toast.makeText(activity, "REGISTRADO CORRECTAMENTE ${negocio.toString()}", Toast.LENGTH_LONG).show()
                db.collection("negocios")
                    .add(negocio)
                    .addOnSuccessListener { documentReference ->
                        //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        //Toast.makeText(activity,"Negocio creado con el ID: ${documentReference.id}",Toast.LENGTH_LONG).show()
                        startActivity(intent)

                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
                Toast.makeText(activity, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                val intent = Intent(activity, InicioActivity::class.java)
                intent.putExtra("negocio", entradaNombreNegocio.text.toString())
                intent.putExtra("email", entradaEmailNegocio.text.toString())
                entradaNombreNegocio.setText("")
                entradaUbicacion.setText("")
                entradaEmailNegocio.setText("")
                entradaDescripcionNegocio.setText("")
                ubicacionImagen= null
                rutaimagen= ""

                startActivity(intent)
                lifecycleScope.launch(Dispatchers.IO){
                    saveValues(emailRegistrado,entradaNombreNegocio.text.toString(), checked = true, checked2 = false)
                }
                }
        }








        return root
    }
    private fun getUserProfile()= requireContext().dataStore.data.map { preferences->
        UserProfile(
            name =  preferences[stringPreferencesKey("name")].orEmpty(),
            negocioName =  preferences[stringPreferencesKey("negocio")].orEmpty(),
            negocio = preferences[booleanPreferencesKey("vip")] ?: false,
            producto = preferences[booleanPreferencesKey("producto")] ?: false
        )

    }
    private suspend fun saveValues(name:String,negocio:String,checked:Boolean,checked2:Boolean){
        requireContext().dataStore.edit { preferences ->
            preferences[stringPreferencesKey("name")] = name
            preferences[stringPreferencesKey("negocio")] = negocio
            preferences[booleanPreferencesKey("vip")] = checked
            preferences[booleanPreferencesKey("producto")] = checked2
        }

    }
    fun comprobarNegocio(email:String):Boolean{

        var flag = false
        if(negocioArrayList.isNotEmpty()){
        for (negocio in negocioArrayList){

            if(negocio.email_Negocio==email){
                //Toast.makeText(activity, " ${negocio.email_Negocio} igual a ${emailRegistrado}", Toast.LENGTH_LONG).show()
                flag=true
                return true

            }
        }}
        return flag
    }
    private fun EventChangeListener(email:String){
        val db = Firebase.firestore

        db.collection("negocios").whereEqualTo("Email_Negocio", email).addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        negocioArrayList.add(dc.document.toObject(Negocio::class.java))

                    }
                }

            }
        }
        )

    }
    fun comprobarNombre(nombre:String):Boolean{

        var flag = false
        if(negocioArrayList1.isNotEmpty()){
            for (negocio in negocioArrayList1){

                if(negocio.trim().equals(nombre.trim(), ignoreCase = true)){
                    //Toast.makeText(activity, " $negocio igual a ${nombre}", Toast.LENGTH_LONG).show()

                    return true

                }
            }}
        return flag
    }
    private fun EventChangeListenerNombre(){
        val db = Firebase.firestore
        db.clearPersistence()

        db.collection("negocios").addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        negocioArrayList1.add(dc.document.toObject(Negocio::class.java).nombre_Negocio)


                    }
                }
                //Toast.makeText(activity, " Ya existe este nombre, debe ingresar otro", Toast.LENGTH_LONG).show()
                //entradaNombreNegocio.setText("")
            }
        }
        )

    }
    private fun validarCampos(entradaNombreNegocio: TextInputEditText, entradaUbicacion: TextInputEditText,
                              entradaEmailNegocio: TextInputEditText, entradaDescripcionNegocio: TextInputEditText):Boolean{

        var flag = true
        if (entradaEmailNegocio.editableText.toString() != emailRegistrado) {

            entradaEmailNegocio.error = "Ingrese el email vinculado a esta cuenta"
            flag = false
        }
        if(comprobarNombre(entradaNombreNegocio.editableText.toString() )){
            entradaNombreNegocio.error = "Este nombre ya esta asociado a otro negocio"
            flag = false
        }
        if (comprobarNegocio(entradaEmailNegocio.editableText.toString() )) {

            entradaEmailNegocio.error = "Este email ya esta asociado a otro negocio"
            flag = false
        }
        if (entradaNombreNegocio.editableText.toString().isEmpty()) {

            entradaNombreNegocio.error = "llenar campo"
            flag = false
        }
        if (entradaUbicacion.editableText.toString().isEmpty()) {

            entradaUbicacion.error = "llenar campo"
            flag =false
        }
        if (entradaEmailNegocio.editableText.toString().isEmpty()) {

            entradaEmailNegocio.error = "Debe llenar campo"
            flag = false
        }
        if (entradaDescripcionNegocio.editableText.toString().isEmpty()) {

            entradaDescripcionNegocio.error = "Debe llenar campo"
            flag = false
        }
        if (entradaNombreNegocio.editableText.toString().length > 30 || entradaNombreNegocio.editableText.toString().length < 2) {

            entradaNombreNegocio.error = "Debe tener 3 a 30 caracteres"
            flag = false
        }
        if (entradaUbicacion.editableText.toString().length > 30 || entradaUbicacion.editableText.toString().length < 2) {

            entradaUbicacion.error = "Debe tener 3 a 30 caracteres"
            flag = false
        }
        if (entradaEmailNegocio.editableText.toString().length > 50 || entradaEmailNegocio.editableText.toString().length < 2) {

            entradaEmailNegocio.error = "Debe tener 3 a 50 caracteres"
            flag = false
        }
        if (entradaDescripcionNegocio.editableText.toString().length > 50 || entradaDescripcionNegocio.editableText.toString().length < 2) {

            entradaDescripcionNegocio.error = "Debe tener 3 a 50 caracteres"
            flag = false
        }
        /*if(rutaimagen.isEmpty()){
            Toast.makeText(activity, "Seleccione una imagen ${rutaimagen}", Toast.LENGTH_LONG).show()
            flag = false
        }*/



        return flag

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
    private fun subirAFireBase() : Uri?{
        Toast.makeText(activity, "Por favor, espere un mientras se carga la imagen", Toast.LENGTH_LONG).show()
        buttonRegistrarseN.isClickable = false
        buttonRegistrarseN.isEnabled = false
        var downloadUri: Uri? = null
        val storage = Firebase.storage("gs://fastfood-a4739.appspot.com")
        var storageRef = storage.reference

        val sd = getFileName(requireContext(), uriContent!!)
        var file = Uri.fromFile(File(sd))
        val riversRef = storageRef.child("images/${file.lastPathSegment}")

        val mountainsRef = storageRef.child(file.lastPathSegment!!)
        val mountainImagesRef = storageRef.child("images/${file.lastPathSegment}")

        mountainsRef.name == mountainImagesRef.name // true
        mountainsRef.path == mountainImagesRef.path // false

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)


        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri = task.result
                rutaimagen=downloadUri.toString()
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


}