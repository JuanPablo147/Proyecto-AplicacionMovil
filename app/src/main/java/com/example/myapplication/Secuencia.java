package com.example.myapplication;

public class Secuencia {
    private Long seq_tabla;
    private String id;
    private String nombre_tabla;

    public Secuencia(Long id, String nombreTabla) {
        this.seq_tabla = id;
        this.nombre_tabla = nombreTabla;
    }
    public Secuencia() {

    }

    public Secuencia(Long seq_tabla, String id, String nombre_tabla) {
        this.seq_tabla = seq_tabla;
        this.id = id;
        this.nombre_tabla = nombre_tabla;
    }

    @Override
    public String toString() {
        return "Sequencia{" +
                "seq_tabla=" + seq_tabla +
                ", id='" + id + '\'' +
                ", nombre_tabla='" + nombre_tabla + '\'' +
                '}';
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Long getSeq_tabla() {
        return seq_tabla;
    }

    public void setSeq_tabla(Long seq_tabla) {
        this.seq_tabla = seq_tabla;
    }

    public String getNombre_tabla() {
        return nombre_tabla;
    }

    public void setNombre_tabla(String nombre_tabla) {
        this.nombre_tabla = nombre_tabla;
    }
}

