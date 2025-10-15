package com.example.practicoinmobiliariaandroid.data.model;

public class Propietario {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private String clave;
    private String token;

    public Propietario(String email, String clave) {
        this.email = email;
        this.clave = clave;
    }

    // Getters y setters
    public String getToken() { return token; }
    public String getEmail() { return email; }
}
