package com.example.agenda_online;

import java.io.Serializable;

public class Nota implements Serializable {

    private String uidTarea;
    private String fechaDeRegistro;
    private String fechaDeCulminacion;
    private String titulo;
    private String descripcion;
    private String uidUsuario;
    private String estado;
    private boolean importante; // Nuevo atributo

    public Nota() {
        // Constructor vacío requerido para Firebase
    }

    public Nota(String uidTarea, String fechaDeRegistro, String fechaDeCulminacion, String titulo, String descripcion, String uidUsuario, String estado, boolean importante) {
        this.uidTarea = uidTarea;
        this.fechaDeRegistro = fechaDeRegistro;
        this.fechaDeCulminacion = fechaDeCulminacion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.uidUsuario = uidUsuario;
        this.estado = estado;
        this.importante = importante; // Inicialización del nuevo atributo
    }

    // Getters y setters

    public String getUidTarea() {
        return uidTarea;
    }

    public void setUidTarea(String uidTarea) {
        this.uidTarea = uidTarea;
    }

    public String getFechaDeRegistro() {
        return fechaDeRegistro;
    }

    public void setFechaDeRegistro(String fechaDeRegistro) {
        this.fechaDeRegistro = fechaDeRegistro;
    }

    public String getFechaDeCulminacion() {
        return fechaDeCulminacion;
    }

    public void setFechaDeCulminacion(String fechaDeCulminacion) {
        this.fechaDeCulminacion = fechaDeCulminacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isImportante() {
        return importante;
    }

    public void setImportante(boolean importante) {
        this.importante = importante;
    }
}
