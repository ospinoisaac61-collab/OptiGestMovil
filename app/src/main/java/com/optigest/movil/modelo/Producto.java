package com.optigest.movil.modelo;

public class Producto {
    private final int id;
    private final String nombre;
    private final String tipo;
    private final double precioUnitario;
    private final int stockActual;

    public Producto(int id, String nombre, String tipo, double precioUnitario, int stockActual) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioUnitario = precioUnitario;
        this.stockActual = stockActual;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getStockActual() { return stockActual; }
}
