package com.optigest.movil.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.optigest.movil.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private final DbHelper ayudante;

    public ProductoDAO(Context contexto) {
        this.ayudante = DbHelper.obtener(contexto);
    }

    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase bd = ayudante.getReadableDatabase();
        try (Cursor c = bd.rawQuery("SELECT id_producto, nombre, tipo, precio_unitario, "
                + "stock_actual FROM producto ORDER BY nombre", null)) {
            while (c.moveToNext()) {
                productos.add(new Producto(c.getInt(0), c.getString(1), c.getString(2),
                        c.getDouble(3), c.getInt(4)));
            }
        }
        return productos;
    }

    public long insertar(Producto producto) {
        return ayudante.getWritableDatabase().insert("producto", null, valores(producto));
    }

    public int actualizar(Producto producto) {
        return ayudante.getWritableDatabase().update("producto", valores(producto),
                "id_producto = ?", new String[]{String.valueOf(producto.getId())});
    }

    public int eliminar(int id) {
        return ayudante.getWritableDatabase().delete("producto",
                "id_producto = ?", new String[]{String.valueOf(id)});
    }

    private ContentValues valores(Producto producto) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", producto.getNombre());
        valores.put("tipo", producto.getTipo());
        valores.put("precio_unitario", producto.getPrecioUnitario());
        valores.put("stock_actual", producto.getStockActual());
        return valores;
    }
}
