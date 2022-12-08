
package com.example.myapplication.ui.gallery

import android.app.Activity.RESULT_OK
import androidx.fragment.app.Fragment
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options

import com.example.myapplication.R
import com.example.myapplication.Secuencia
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File

@Suppress("DEPRECATION")
class GalleryFragment : Fragment() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var _binding: FragmentGalleryBinding? = null
    private var uriContent: Uri? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val cropImage = this.registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(context!!) // optional usage
            imageView.setImageURI(uriContent)
            subirAFireBase()
        } else {
            // An error occurred.
            val exception = result.error
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root



        //val root: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        val buttonClick = root.findViewById<Button>(R.id.button5)
        //var arrayListSequencia = ArrayList<Sequencia>()
        val db = Firebase.firestore


        val arrayListSecuencia = readFirebase()
        buttonClick.setOnClickListener {


            for (seq in arrayListSecuencia) {
                //Log.d(TAG, "================> ${seq.id}")
                //Toast.makeText(activity, "================> ${seq.toString()}", Toast.LENGTH_LONG).show()
                seq.seq_tabla+=1
                //Toast.makeText(activity, "================> $seq", Toast.LENGTH_LONG).show()
                db.collection("seq_id").document(seq.id).update("seq_tabla", seq.seq_tabla)

            }
        }
        button = root.findViewById(R.id.buttonLoadPicture)
        var cropImageView : CropImageView = root.findViewById(R.id.cropImageView)
        imageView = root.findViewById(R.id.imageView)
        button.setOnClickListener {
            //val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            //startActivityForResult(gallery, pickImage)
            //CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(activity);
            cropImage.launch(
                options(uri = imageUri) {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setActivityTitle("Cargar Imagen")
                    setAspectRatio(1, 1)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)


                }

            )

            imageView.setImageURI(imageUri)
            //startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            /*cropImageView.setImageUriAsync(uri)
            setCropImageView(binding.cropImageView)*/
            //Toast.makeText(activity, "================> $CropImage.CROP_IMAGE_EXTRA_SOURCE", Toast.LENGTH_LONG).show()

        }






        return root
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
    private fun subirAFireBase(){

        val storage = Firebase.storage("gs://fastfood-a4739.appspot.com")
        var storageRef = storage.reference

        val sd = getFileName(requireContext(), uriContent!!)
        var file = Uri.fromFile(File(sd))
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
        // Create a reference to 'images/mountains.jpg'

        val mountainsRef = storageRef.child(file.lastPathSegment!!)
        val mountainImagesRef = storageRef.child("images/${file.lastPathSegment}")
        //Toast.makeText(activity, "${file!!.toString()}================> $file", Toast.LENGTH_LONG).show()
        // While the file names are the same, the references point to different files
        mountainsRef.name == mountainImagesRef.name // true
        mountainsRef.path == mountainImagesRef.path // false
        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)



        val storage = Firebase.storage("gs://fastfood-a4739.appspot.com")
        var storageRef = storage.reference


        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            //Toast.makeText(activity, "================> Cargado con exito ${imageUri.toString()}", Toast.LENGTH_LONG).show()
            val sd = getFileName(requireContext(), imageUri!!)
            imageView.setImageURI(imageUri)
            // Create a reference to "mountains.jpg"
            //
            var file = Uri.fromFile(File(sd))
            val riversRef = storageRef.child("images/${file.lastPathSegment}")
            // Create a reference to 'images/mountains.jpg'

            val mountainsRef = storageRef.child(file.lastPathSegment!!)
            val mountainImagesRef = storageRef.child("images/${file.lastPathSegment}")
            //Toast.makeText(activity, "${file!!.toString()}================> $file", Toast.LENGTH_LONG).show()
            // While the file names are the same, the references point to different files
            mountainsRef.name == mountainImagesRef.name // true
            mountainsRef.path == mountainImagesRef.path // false
            // Get the data from an ImageView as bytes
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
            Toast.makeText(activity, "Subida con exito ${getFileName(requireContext(), imageUri!!).toString()}", Toast.LENGTH_LONG).show()
        }
    }

    private fun readFirebase(): ArrayList<Secuencia> {
        val arrayListSecuencia = ArrayList<Secuencia>()

        val db = Firebase.firestore
        db.collection("seq_id")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data} ==>${document.id::class}")
                    val miProducto = document.toObject<Secuencia>()
                    miProducto.id = document.id
                    arrayListSecuencia.add(miProducto)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return arrayListSecuencia
    }
}