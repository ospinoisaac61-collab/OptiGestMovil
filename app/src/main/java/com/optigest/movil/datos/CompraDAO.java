package com.optigest.movil.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.optigest.movil.modelo.Compra;

import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    private static final String SQL_LISTAR =
            "SELECT c.id_compra, c.fecha_compra, c.total, c.estado, c.id_proveedor, "
                    + "c.id_usuario, p.nombre, u.nombre "
                    + "FROM compra c "
                    + "JOIN proveedor p ON p.id_proveedor = c.id_proveedor "
                    + "JOIN usuario u ON u.id_usuario = c.id_usuario "
                    + "ORDER BY c.id_compra";

    private final DbHelper ayudante;

    public CompraDAO(Context contexto) {
        this.ayudante = DbHelper.obtener(contexto);
    }

    public List<Compra> listar() {
        List<Compra> compras = new ArrayList<>();
        SQLiteDatabase bd = ayudante.getReadableDatabase();
        try (Cursor c = bd.rawQuery(SQL_LISTAR, null)) {
            while (c.moveToNext()) {
                compras.add(new Compra(c.getInt(0), c.getString(1), c.getDouble(2),
                        c.getString(3), c.getInt(4), c.getInt(5), c.getString(6),
                        c.getString(7)));
            }
        }
        return compras;
    }

    /** La fecha la asigna la base de datos (CURRENT_TIMESTAMP). */
    public long insertar(double total, String estado, int idProveedor, int idUsuario) {
        ContentValues valores = new ContentValues();
        valores.put("total", total);
        valores.put("estado", estado);
        valores.put("id_proveedor", idProveedor);
        valores.put("id_usuario", idUsuario);
        return ayudante.getWritableDatabase().insert("compra", null, valores);
    }

    public int actualizar(int id, double total, String estado, int idProveedor) {
        ContentValues valores = new ContentValues();
        valores.put("total", total);
        valores.put("estado", estado);
        valores.put("id_proveedor", idProveedor);
        return ayudante.getWritableDatabase().update("compra", valores,
                "id_compra = ?", new String[]{String.valueOf(id)});
    }

    public int eliminar(int id) {
        return ayudante.getWritableDatabase().delete("compra",
                "id_compra = ?", new String[]{String.valueOf(id)});
    }
}
