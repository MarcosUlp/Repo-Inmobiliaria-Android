package com.example.practicoinmobiliariaandroid.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Propietario implements Serializable {

    @SerializedName("idPropietario") // 👈 esto lo alinea con el backend
    private int id;

    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private String clave;

    public Propietario() {}

    public Propietario(String email, String clave) {
        this.email = email;
        this.clave = clave;
    }

    public Propietario(int id, String nombre, String apellido, String dni, String telefono, String email, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.clave = clave;
    }

    public Propietario(int id, String nombre, String apellido, String dni, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getClave() { return clave; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(String dni) { this.dni = dni; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setClave(String clave) { this.clave = clave; }
}
