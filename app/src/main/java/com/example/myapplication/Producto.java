package com.example.myapplication;

import androidx.annotation.NonNull;

public class Producto  {
    private Double Costo;
    private String descripcion;
    private String Nombre_Producto;
    private String negocio;
    private String tipo_comida;
    private String ruta_imagen;
    private String estado;
    private String id_product;
    private String id_negocio;
    private String nombre_negocio;
    private int telefono;
    private String id_telefono;
    public Producto() {

    }

    @NonNull
    @Override
    public String toString() {
        return "Producto{" +
                "Costo=" + Costo +
                ", descripcion='" + descripcion + '\'' +
                ", Nombre_Producto='" + Nombre_Producto + '\'' +
                ", Negocio='" + negocio + '\'' +
                ", tipo_comida='" + tipo_comida + '\'' +
                ", ruta_imagen='" + ruta_imagen + '\'' +
                ", estado='" + estado + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    public String getNombre_negocio() {
        return nombre_negocio;
    }

    public void setNombre_negocio(String nombre_negocio) {
        this.nombre_negocio = nombre_negocio;
    }

    public int getNombre_telefono() {
        return telefono;
    }

    public void setNombre_telefono(int telefono) {
        this.telefono = telefono;
    }

    public Producto(Double costo, String descripcion, String nombre_Producto, String negocio, String tipo_comida, String ruta_imagen, String estado) {
        Costo = costo;
        this.descripcion = descripcion;
        Nombre_Producto = nombre_Producto;
        this.negocio = negocio;
        this.tipo_comida = tipo_comida;
        this.ruta_imagen = ruta_imagen;
        this.estado = estado;
        //this.telefono = telefono;

    }

    public String getId_negocio() {
        return id_negocio;
    }

    public void setId_negocio(String id_negocio) {
        this.id_negocio = id_negocio;
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
        return negocio;
    }

    public void setNegocio(String negocio) {
        this.negocio = negocio;
    }

    public int gettelefono() {
        return telefono;
    }

    public void settelefono(int telefono) {
        this.telefono = telefono;
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