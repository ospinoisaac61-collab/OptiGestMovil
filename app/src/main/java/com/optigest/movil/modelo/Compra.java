package com.optigest.movil.modelo;

public class Compra {
    private final int id;
    private final String fecha;
    private final double total;
    private final String estado;
    private final int idProveedor;
    private final int idUsuario;
    private final String nombreProveedor;
    private final String nombreUsuario;

    public Compra(int id, String fecha, double total, String estado, int idProveedor,
                  int idUsuario, String nombreProveedor, String nombreUsuario) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.idProveedor = idProveedor;
        this.idUsuario = idUsuario;
        this.nombreProveedor = nombreProveedor;
        this.nombreUsuario = nombreUsuario;
    }

    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    public int getIdProveedor() { return idProveedor; }
    public int getIdUsuario() { return idUsuario; }
    public String getNombreProveedor() { return nombreProveedor; }
    public String getNombreUsuario() { return nombreUsuario; }
}
