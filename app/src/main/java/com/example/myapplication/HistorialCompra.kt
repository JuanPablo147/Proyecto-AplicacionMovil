package com.example.myapplication

import java.io.Serializable

class HistorialCompra : Serializable {

    var nombre_Producto_Ultimo:String =""
    var fecha_ultima_compra:String=""
    var id_negocio:String=""
    var correo_cliente:String=""
    var precio_ultima_compra:Double=0.0
    var numero_producto_comprado: Int =0
    var puntuacion:Float = (0).toFloat()
    var id_historial:String=""



    constructor() {}

    constructor(
        nombre_Producto_Ultimo: String,
        fecha_ultima_compra: String,
        id_negocio: String,
        precio_ultima_compra: Double,
        numero_producto_comprado: Int,
        correo_cliente :String,
        puntuacion:Float,
        id_historial:String
    ) {
        this.nombre_Producto_Ultimo = nombre_Producto_Ultimo
        this.fecha_ultima_compra = fecha_ultima_compra
        this.id_negocio = id_negocio
        this.numero_producto_comprado = numero_producto_comprado
        this.precio_ultima_compra = precio_ultima_compra
        this.correo_cliente=correo_cliente
        this.puntuacion=puntuacion
        this.id_historial=id_historial

    }

}