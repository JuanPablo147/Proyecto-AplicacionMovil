package com.example.myapplication.ui.negocio

import java.io.Serializable

class Negocios : Serializable {

    public var nombre_Negocio:String =""
    public var descripcion_Negocio:String=""
    public var id_negocio:String=""
    public var suma_Promedio:Double=0.0
    public var numero_N: Int =0
    public var ubicacion:String=""
    public var email_Negocio:String=""
    public var ruta_imagen:String=""
    public var telefono: Int? = null
    constructor() {}

    constructor(Nombre_Negocio: String, Descripcion_Negocio: String, id_negocio: String, suma_Promedio: Double
                ,numero_N: Int, Ubicacion: String, Email_Negocio: String, Ruta_imagen: String,telefono: Int) {
        this.nombre_Negocio = Nombre_Negocio
        this.descripcion_Negocio = Descripcion_Negocio
        this.id_negocio = id_negocio
        this.suma_Promedio = suma_Promedio
        this.numero_N = numero_N
        this.ubicacion = Ubicacion
        this.email_Negocio = Email_Negocio
        this.ruta_imagen = Ruta_imagen
        this.telefono = telefono
    }

}
