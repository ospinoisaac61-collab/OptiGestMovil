package com.optigest.movil.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.optigest.movil.modelo.Proveedor;

import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    private final DbHelper ayudante;

    public ProveedorDAO(Context contexto) {
        this.ayudante = DbHelper.obtener(contexto);
    }

    public List<Proveedor> listar() {
        List<Proveedor> proveedores = new ArrayList<>();
        SQLiteDatabase bd = ayudante.getReadableDatabase();
        try (Cursor c = bd.rawQuery("SELECT id_proveedor, nombre, telefono, email, direccion "
                + "FROM proveedor ORDER BY nombre", null)) {
            while (c.moveToNext()) {
                proveedores.add(new Proveedor(c.getInt(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getString(4)));
            }
        }
        return proveedores;
    }

    /** Lanza SQLiteConstraintException si el correo ya existe. */
    public long insertar(Proveedor proveedor) {
        return ayudante.getWritableDatabase().insertOrThrow("proveedor", null, valores(proveedor));
    }

    /** Lanza SQLiteConstraintException si el correo ya existe. */
    public int actualizar(Proveedor proveedor) {
        return ayudante.getWritableDatabase().updateWithOnConflict("proveedor", valores(proveedor),
                "id_proveedor = ?", new String[]{String.valueOf(proveedor.getId())},
                SQLiteDatabase.CONFLICT_ABORT);
    }

    /** Lanza SQLiteConstraintException si el proveedor tiene compras registradas. */
    public int eliminar(int id) {
        return ayudante.getWritableDatabase().delete("proveedor",
                "id_proveedor = ?", new String[]{String.valueOf(id)});
    }

    private ContentValues valores(Proveedor proveedor) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", proveedor.getNombre());
        valores.put("telefono", proveedor.getTelefono());
        valores.put("email", proveedor.getEmail());
        valores.put("direccion", proveedor.getDireccion());
        return valores;
    }
}
