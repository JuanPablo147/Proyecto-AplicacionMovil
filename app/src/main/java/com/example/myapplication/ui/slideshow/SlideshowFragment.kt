package com.example.myapplication.ui.slideshow

import android.content.ContentValues.TAG
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
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.myapplication.*
import com.example.myapplication.databinding.FragmentSlideshowBinding
import com.google.android.material.textfield.TextInputEditText
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
class SlideshowFragment : Fragment() {


    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var negocioNombre :String
    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var imageView: ImageView
    lateinit var button: Button
    private lateinit var buttonRegistrarseN : Button
    //private val pickImage = 100
    private var imageUri: Uri? = null
    private var uriContent: Uri? = null
    private var rutaimagen: String =""
    private var ubicacionImagen: Uri? = null

    //lateinit var emailRegistrado:String
    //private lateinit var negocioArrayList: ArrayList<Negocio>







    private val binding get() = _binding!!
    private val cropImage = this.registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {

            uriContent = result.uriContent
            result.getUriFilePath(context!!)
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
    ):
            View {

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val entradaNombreProducto = root.findViewById<TextInputEditText>(R.id.EntradaNombreProducto)
        var negocioNom = ""
        lifecycleScope.launch(Dispatchers.IO){
            getUserProfile().collect{
                withContext(Dispatchers.Main){

                    try {
                        negocioNom= it.negocioName

                    }catch (_:Exception){

                    }

                }
            }
        }
        negocioNombre= negocioNom
        val entradaCosto = root.findViewById<TextInputEditText>(R.id.EntradaCosto)
        val entradaDescripcion = root.findViewById<TextInputEditText>(R.id.EntradaDescripcion)
        val db = Firebase.firestore
        val intent = Intent (activity, InicioActivity::class.java)
        val sliderTipo = root.findViewById<Spinner>(R.id.spinnerProductos)
        val sliderEstado = root.findViewById<Spinner>(R.id.spinnerEstado)
        //sliderTipo.selectedItem.toString()
        //sliderEstado.selectedItem.toString()
        buttonRegistrarseN = root.findViewById<Button>(R.id.button3)
        button = root.findViewById(R.id.buttonCargarImagenProd)

        imageView = root.findViewById(R.id.imageViewProd)
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
                    //Toast.makeText(activity, "Click on : " + texto.editableText.toString(), Toast.LENGTH_LONG).show()
                    var costoNum =0.0
                        try {
                            costoNum=entradaCosto.editableText.toString().toDouble()
                        }catch (e: Exception){
                            Log.w(TAG, "Error adding document", e)
                        }
                    //Toast.makeText(activity,"ID: ${sliderTipo.selectedItem}",Toast.LENGTH_LONG).show()
                    if(noEsVacia(entradaNombreProducto, entradaCosto, entradaDescripcion)){

                        lifecycleScope.launch(Dispatchers.IO){
                            getUserProfile().collect{
                                withContext(Dispatchers.Main){
                                    negocioNom= it.negocioName
                                    //Toast.makeText(activity,"ID: $negocioNom",Toast.LENGTH_LONG).show()

                                }
                            }
                        }
                        val producto = hashMapOf(
                            "Nombre_Producto" to entradaNombreProducto.editableText.toString(),
                            "Costo" to costoNum,
                            "Negocio" to negocioNom,
                            "descripcion" to entradaDescripcion.editableText.toString(),
                            "tipo_comida" to sliderTipo.selectedItem.toString(),
                            "estado" to sliderEstado.selectedItem.toString(),
                            "ruta_imagen" to rutaimagen
                        )
                        db.collection("Producto")
                            .add(producto)
                            .addOnSuccessListener { documentReference ->
                                //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                //Toast.makeText(activity,"Producto agregado con el ID: ${documentReference.id}",Toast.LENGTH_LONG).show()
                                Toast.makeText(activity,"Producto agregado correctamente",Toast.LENGTH_LONG).show()
                                startActivity(intent)

                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                        entradaNombreProducto.setText("")
                        entradaCosto.setText("")
                        entradaDescripcion.setText("")
                        ubicacionImagen= null
                        rutaimagen= ""
                    }


                }




        //Toast.makeText(activity, "Click on : " +lista.toString() , Toast.LENGTH_LONG).show()
        //esVacia(EntradaNombreProducto, EntradaCosto, EntradaDescripcion, buttonClick)
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


    private fun noEsVacia(EntradaNombreProducto: TextInputEditText, EntradaCosto: TextInputEditText, EntradaDescripcion: TextInputEditText):Boolean {
        val flag = true


        if (EntradaNombreProducto.editableText.toString().isEmpty()) {
            //Toast.makeText(activity, "Campo nombre vacio", Toast.LENGTH_LONG).show()
            EntradaNombreProducto.error = "llenar campo"
            return false
        }
        if (EntradaCosto.editableText.toString().isEmpty()) {
            //Toast.makeText(activity, "Campo costo vacio", Toast.LENGTH_LONG).show()
            EntradaCosto.error = "llenar campo"
            return false
            }
        if (EntradaDescripcion.editableText.toString().isEmpty()) {
            //Toast.makeText(activity, "Campo descripcion vacio", Toast.LENGTH_LONG).show()
            EntradaDescripcion.error = "llenar campo"
            return false
         }
        if (EntradaDescripcion.editableText.toString().length > 50 || EntradaDescripcion.editableText.toString().length < 3) {
            //Toast.makeText(activity, "Descripcion no valida", Toast.LENGTH_SHORT).show()
            EntradaDescripcion.error = "debe tener 3 a 50 caracteres"
            return false
        }
       /* if (EntradaCosto.length() > 5)  {
            Toast.makeText(activity, "Costo demasiado elevado", Toast.LENGTH_SHORT).show()
            flag = false
        }*/
        if (EntradaNombreProducto.editableText.toString().length > 50 || EntradaNombreProducto.editableText.toString().length < 3) {
            //Toast.makeText(activity, "Nombre de Producto no valido", Toast.LENGTH_SHORT).show()
            EntradaNombreProducto.error = "debe tener 3 a 50 caracteres"
            return false
        }





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
        buttonRegistrarseN.isClickable = false
        buttonRegistrarseN.isEnabled = false
        var downloadUri: Uri? = null
        val storage = Firebase.storage("gs://fastfood-a4739.appspot.com")
        val storageRef = storage.reference

        val sd = getFileName(requireContext(), uriContent!!)
        var file = Uri.fromFile(File(sd.toString()))
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}