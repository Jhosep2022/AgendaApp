package com.example.agenda_online;

import java.io.Serializable;

public class Contacto implements Serializable {

    private String uidContacto;
    private String nombre;
    private String apellido;
    private String email;
    private String genero;
    private String uidUsuario;
    private String telefono; // Nuevo atributo

    public Contacto() {
        // Constructor vacío requerido para Firebase
    }

    public Contacto(String uidContacto, String nombre, String apellido, String email, String genero, String uidUsuario, String telefono) {
        this.uidContacto = uidContacto;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.genero = genero;
        this.uidUsuario = uidUsuario;
        this.telefono = telefono; // Inicialización del nuevo atributo
    }

    // Getters y setters

    public String getUidContacto() {
        return uidContacto;
    }

    public void setUidContacto(String uidContacto) {
        this.uidContacto = uidContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
