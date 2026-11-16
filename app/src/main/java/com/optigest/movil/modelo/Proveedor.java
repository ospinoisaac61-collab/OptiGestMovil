package com.optigest.movil.modelo;

public class Proveedor {
    private final int id;
    private final String nombre;
    private final String telefono;
    private final String email;
    private final String direccion;

    public Proveedor(int id, String nombre, String telefono, String email, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }

    /** Se usa en los Spinner para mostrar el nombre. */
    @Override
    public String toString() {
        return nombre;
    }
}
