package com.example.myapplication;

import androidx.annotation.NonNull;

public class Producto  {
    private Double Costo;
    private String descripcion;
    private String Nombre_Producto;
    private String Negocio;
    private String tipo_comida;
    private String ruta_imagen;
    private String estado;
    private String id_product;

    public Producto() {

    }

    @NonNull
    @Override
    public String toString() {
        return "Producto{" +
                "Costo=" + Costo +
                ", descripcion='" + descripcion + '\'' +
                ", Nombre_Producto='" + Nombre_Producto + '\'' +
                ", Negocio='" + Negocio + '\'' +
                ", tipo_comida='" + tipo_comida + '\'' +
                ", ruta_imagen='" + ruta_imagen + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }

    public Producto(Double costo, String descripcion, String nombre_Producto, String negocio, String tipo_comida, String ruta_imagen, String estado) {
        Costo = costo;
        this.descripcion = descripcion;
        Nombre_Producto = nombre_Producto;
        Negocio = negocio;
        this.tipo_comida = tipo_comida;
        this.ruta_imagen = ruta_imagen;
        this.estado = estado;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public Double getCosto() {
        return Costo;
    }

    public void setCosto(Double costo) {
        Costo = costo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre_Producto() {
        return Nombre_Producto;
    }

    public void setNombre_Producto(String nombre_Producto) {
        Nombre_Producto = nombre_Producto;
    }

    public String getNegocio() {
        return Negocio;
    }

    public void setNegocio(String negocio) {
        Negocio = negocio;
    }

    public String getTipo_comida() {
        return tipo_comida;
    }

    public void setTipo_comida(String tipo_comida) {
        this.tipo_comida = tipo_comida;
    }

    public String getRuta_imagen() {
        return ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}