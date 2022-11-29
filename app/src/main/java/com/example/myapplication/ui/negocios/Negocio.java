package com.example.myapplication.ui.negocios;

public class Negocio {
    private Long id_negocio;
    private String Nombre_Negocio;
    private String Descripcion_Negocio;
    private String id;
    private String Ubicacion;
    private String Email_Negocio;
    private String Ruta_imagen;

    public Negocio() {

    }

    public Negocio(String Descripcion_Negocio, String Email_Negocio, String Nombre_Negocio, String Ruta_imagen, String Ubicacion) {
        this.Descripcion_Negocio = Descripcion_Negocio;
        this.Email_Negocio = Email_Negocio;
        this.Nombre_Negocio = Nombre_Negocio;
        this.Ruta_imagen = Ruta_imagen;
        this.Ubicacion = Ubicacion;

    }

    @Override
    public String toString() {
        return "Negocio{" +
                "Nombre_Negocio='" + Nombre_Negocio + '\'' +
                ", Descripcion_Negocio='" + Descripcion_Negocio + '\'' +
                ", Ubicacion='" + Ubicacion + '\'' +
                ", Email_Negocio='" + Email_Negocio + '\'' +
                ", Ruta_imagen='" + Ruta_imagen + '\'' +
                '}';
    }

    public Long getId_negocio() {
        return id_negocio;
    }

    public void setId_negocio(Long id_negocio) {
        this.id_negocio = id_negocio;
    }

    public String getNombre_Negocio() {
        return Nombre_Negocio;
    }

    public void setNombre_Negocio(String nombre_Negocio) {
        Nombre_Negocio = nombre_Negocio;
    }

    public String getDescripcion_Negocio() {
        return Descripcion_Negocio;
    }

    public void setDescripcion_Negocio(String descripcion_Negocio) {
        Descripcion_Negocio = descripcion_Negocio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getEmail_Negocio() {
        return Email_Negocio;
    }

    public void setEmail_Negocio(String email_Negocio) {
        Email_Negocio = email_Negocio;
    }

    public String getRuta_imagen() {
        return Ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        Ruta_imagen = ruta_imagen;
    }
}
