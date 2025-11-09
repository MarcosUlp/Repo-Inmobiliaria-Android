package com.example.practicoinmobiliariaandroid.data.model;

import java.io.Serializable;

public class Inmueble implements Serializable {
    private int idInmueble;
    private String direccion;
    private String tipo;
    private String uso;
    private int ambientes;
    private int superficie;
    private double latitud;
    private double longitud; // ✨ AÑADIDO
    private double valor;
    private String imagen;
    private Boolean disponible;
    private int idPropietario;
    private Propietario duenio;
    private boolean tieneContratoVigente; // ✨ AÑADIDO

    public Inmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }
    public Inmueble() {
        // Constructor vacío necesario para Retrofit / Gson / uso manual
    }

    // Constructor completo actualizado
    public Inmueble(int idInmueble, String direccion, String tipo, String uso, int ambientes, int superficie, double latitud, double longitud, double valor, String imagen, boolean disponible, int idPropietario, Propietario duenio, boolean tieneContratoVigente) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
        this.tipo = tipo;
        this.uso = uso;
        this.ambientes = ambientes;
        this.superficie = superficie;
        this.latitud = latitud;
        this.longitud = longitud; // Añadido
        this.valor = valor;
        this.imagen = imagen;
        this.disponible = disponible;
        this.idPropietario = idPropietario;
        this.duenio = duenio;
        this.tieneContratoVigente = tieneContratoVigente; // Añadido
    }

    // Constructor anterior (ajustado para compatibilidad si el código antiguo lo usa)
    public Inmueble(int idInmueble, String direccion, String tipo, String uso, int ambientes, int superficie, double latitud, double valor, String imagen, boolean disponible, int idPropietario, Propietario duenio) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
        this.tipo = tipo;
        this.uso = uso;
        this.ambientes = ambientes;
        this.superficie = superficie;
        this.latitud = latitud;
        this.valor = valor;
        this.imagen = imagen;
        this.disponible = disponible;
        this.idPropietario = idPropietario;
        this.duenio = duenio;
        // Asumimos valores por defecto para los campos nuevos en este constructor antiguo
        this.longitud = 0.0;
        this.tieneContratoVigente = false;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    // ✨ AÑADIDO: Getter y Setter para longitud
    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    // FIN AÑADIDO para longitud

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public Propietario getDuenio() {
        return duenio;
    }

    public void setDuenio(Propietario duenio) {
        this.duenio = duenio;
    }

    public boolean isTieneContratoVigente() {
        return tieneContratoVigente;
    }

    public void setTieneContratoVigente(boolean tieneContratoVigente) {
        this.tieneContratoVigente = tieneContratoVigente;
    }

}