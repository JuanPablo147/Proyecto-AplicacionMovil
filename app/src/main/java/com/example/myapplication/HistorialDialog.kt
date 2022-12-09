package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.negocio.Negocios
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.historial_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorialDialog : DialogFragment() {
    private lateinit var negocioArrayList: ArrayList<Negocios>
    private lateinit var db: FirebaseFirestore
    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private const val SUB_SUBTITLE1 = "SUB_SUBTITLE1"
        private const val KEY_SUBTITLE2 = "KEY_SUBTITLE2"
        private const val KEY_SUBTITLE3 = "KEY_SUBTITLE3"
        private const val KEY_SUBTITLE4 = "KEY_SUBTITLE4"


        fun newInstance(title: String, subTitle: String, subTitle1: String, subTitle2: String, subTitle3: String, promedio : Float): HistorialDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putString(SUB_SUBTITLE1, subTitle1)
            args.putString(KEY_SUBTITLE2, subTitle2)
            args.putString(KEY_SUBTITLE3, subTitle3)
            args.putFloat(KEY_SUBTITLE4, promedio)
            val fragment = HistorialDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.historial_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
    private fun getUserProfile()= requireContext().dataStore.data.map { preferences->
        UserProfile(
            name =  preferences[stringPreferencesKey("name")].orEmpty(),
            negocioName =  preferences[stringPreferencesKey("negocio")].orEmpty(),
            negocio = preferences[booleanPreferencesKey("vip")] ?: false,
            producto = preferences[booleanPreferencesKey("producto")] ?: false
        )

    }

    private fun setupView(view: View) {
        view.tvTitle123.text = arguments?.getString(KEY_TITLE)
        view.tvSubTitle125.text = arguments?.getString(SUB_SUBTITLE1)
        view.tvSubTitle126.text = arguments?.getString(KEY_SUBTITLE2)
        view.ratingBar127.rating = arguments?.getFloat(KEY_SUBTITLE4)!!
    }
    private fun eventChangeListener(idNegocio : String,rating : Float){
        db = FirebaseFirestore.getInstance()
        db.collection("Business").whereEqualTo("id_negocio",idNegocio).addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        val miNegocio : Negocios =dc.document.toObject(Negocios::class.java)
                        miNegocio.suma_Promedio+=rating
                        miNegocio.numero_N++
                        db.collection("Business").document(miNegocio.id_negocio).set(miNegocio, SetOptions.merge())
                    }
                }
            }
        }
        )

    }
    private fun eventChangeAntiguoListener(idNegocio : String,rating : Float,antiguo:Float){
        db = FirebaseFirestore.getInstance()
        db.collection("Business").whereEqualTo("id_negocio",idNegocio).addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        val miNegocio : Negocios =dc.document.toObject(Negocios::class.java)
                        miNegocio.suma_Promedio=miNegocio.suma_Promedio+rating-antiguo

                        db.collection("Business").document(miNegocio.id_negocio).set(miNegocio, SetOptions.merge())
                    }
                }
            }
        }
        )

    }
    private fun obtenerNegocios(correo_cliente: String,id_negocio :String,rating: Float){

        val dbs = FirebaseFirestore.getInstance()
        dbs.collection("Historial").whereEqualTo("id_negocio", id_negocio).addSnapshotListener(object :
            EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error",error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        val miHistorialCompra : HistorialCompra =dc.document.toObject(HistorialCompra::class.java)


                        if(correo_cliente == miHistorialCompra.correo_cliente){
                            miHistorialCompra.correo_cliente= correo_cliente
                            miHistorialCompra.puntuacion= rating
                            obtenerCorreo(miHistorialCompra)
                        }
                    }
                }
            }
        }
        )

    }
    private fun obtenerCorreo(miHistorialCompra: HistorialCompra){
        db.collection("Historial").document(miHistorialCompra.id_historial).set(miHistorialCompra, SetOptions.merge())


    }
    private fun setupClickListeners(view: View) {
        var antiguaPuntuacion = view.ratingBar127.rating
        var bandera = false
        if(antiguaPuntuacion == (0).toFloat()){
            bandera=true
        }
        view.btnNegative127.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
        view.btnPositive127.setOnClickListener {
            // TODO: Do some task here
            lifecycleScope.launch(Dispatchers.IO){
                getUserProfile().collect{
                    withContext(Dispatchers.Main){
                        obtenerNegocios(it.name,arguments?.getString(KEY_SUBTITLE3).toString(),view.ratingBar127.rating)
                    }
                }
            }
            if(bandera){
                eventChangeListener(arguments?.getString(KEY_SUBTITLE3).toString(),view.ratingBar127.rating)
            }else{
                eventChangeAntiguoListener(arguments?.getString(KEY_SUBTITLE3).toString(),view.ratingBar127.rating,antiguaPuntuacion)
            }


            dismiss()
        }
    }

}
