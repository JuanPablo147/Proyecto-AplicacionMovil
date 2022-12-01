package com.example.myapplication;

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.modelProductCar.ProductCar
import com.example.myapplication.modelProductCar.ProductCarAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Carrito : AppCompatActivity() {

    private var adapter : ProductCarAdapter ?= null
    private lateinit var itemListView : ListView
    private var listProductCar : ArrayList<ProductCar> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "Mi Carrito"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        itemListView = findViewById(R.id.ivCarrito)

        adapter = ProductCarAdapter(this, listProductCar)
        itemListView.adapter = adapter
        adapter!!.actionAdd = {
            addProductToCar(it)
        }

        adapter!!.actionRemove = {
            removeProductToCar(it)
        }

        getProducCarList()
    }

    private fun getProducCarList() {
        listProductCar.clear()
        listProductCar.addAll(getProductCar())
        adapter!!.notifyDataSetChanged()
    }


    private fun addProductToCar(position : Int) {
        listProductCar[position].cant++
        saveProductCar()
    }

    private fun removeProductToCar(position: Int) {
        if(listProductCar[position].cant-1 <= 0) {
            //si a restarlo es 0 o menor lo removemos
            listProductCar.removeAt(position)
            saveProductCar()
        } else {
            listProductCar[position].cant--
            saveProductCar()
        }
    }


    private fun saveProductCar() {
        val email = getEmailCurrentUser()
        var json = Gson().toJson(listProductCar)
        var sharedPref = this.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("product_car${email.replace("@","")}", json)
        editor.commit()
        getProducCarList()
    }


    private fun getProductCar(): ArrayList<ProductCar> {
        val email = getEmailCurrentUser()

        val sharedPref = this.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        var json = sharedPref.getString("product_car${email.replace("@","")}", "")
        var list: ArrayList<ProductCar>? =
            Gson().fromJson(json, object : TypeToken<MutableList<ProductCar>>() {}.type)
        return list ?: arrayListOf()
    }

    fun getEmailCurrentUser() : String {
        val sharedPref = this.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", "")
        return email ?: ""
    }
}
