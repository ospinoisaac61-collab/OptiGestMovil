package com.optigest.movil.datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.optigest.movil.modelo.Usuario;

public class UsuarioDAO {

    private final DbHelper ayudante;

    public UsuarioDAO(Context contexto) {
        this.ayudante = DbHelper.obtener(contexto);
    }

    /** Devuelve el usuario activo con ese correo y hash de contraseña, o null si no existe. */
    public Usuario autenticar(String email, String hashClave) {
        SQLiteDatabase bd = ayudante.getReadableDatabase();
        try (Cursor cursor = bd.rawQuery(
                "SELECT id_usuario, nombre, email, rol FROM usuario "
                        + "WHERE email = ? AND password = ? AND estado = 1",
                new String[]{email, hashClave})) {
            if (cursor.moveToFirst()) {
                return new Usuario(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
            }
            return null;
        }
    }
}
