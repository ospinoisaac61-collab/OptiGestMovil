package com.optigest.movil.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Crea y administra la base de datos SQLite local con las mismas tablas
 * del sistema web OptiGest (usuario, producto, proveedor y compra).
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "optigest.db";
    private static final int VERSION_BD = 1;
    private static DbHelper instancia;

    private DbHelper(Context contexto) {
        super(contexto, NOMBRE_BD, null, VERSION_BD);
    }

    public static synchronized DbHelper obtener(Context contexto) {
        if (instancia == null) {
            instancia = new DbHelper(contexto.getApplicationContext());
        }
        return instancia;
    }

    @Override
    public void onConfigure(SQLiteDatabase bd) {
        bd.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE usuario ("
                + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "rol TEXT NOT NULL, "
                + "estado INTEGER NOT NULL DEFAULT 1, "
                + "fecha_creacion TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP)");

        bd.execSQL("CREATE TABLE producto ("
                + "id_producto INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "tipo TEXT NOT NULL, "
                + "precio_unitario REAL NOT NULL, "
                + "stock_actual INTEGER NOT NULL DEFAULT 0)");

        bd.execSQL("CREATE TABLE proveedor ("
                + "id_proveedor INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "telefono TEXT, "
                + "email TEXT UNIQUE, "
                + "direccion TEXT, "
                + "fecha_registro TEXT NOT NULL DEFAULT CURRENT_DATE)");

        bd.execSQL("CREATE TABLE compra ("
                + "id_compra INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "fecha_compra TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "total REAL NOT NULL, "
                + "estado TEXT NOT NULL DEFAULT 'PENDIENTE', "
                + "id_proveedor INTEGER NOT NULL REFERENCES proveedor (id_proveedor) ON DELETE RESTRICT, "
                + "id_usuario INTEGER NOT NULL REFERENCES usuario (id_usuario) ON DELETE RESTRICT)");

        insertarDatosEjemplo(bd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int versionAnterior, int versionNueva) {
        bd.execSQL("DROP TABLE IF EXISTS compra");
        bd.execSQL("DROP TABLE IF EXISTS proveedor");
        bd.execSQL("DROP TABLE IF EXISTS producto");
        bd.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(bd);
    }

    /** Mismos datos de ejemplo del sistema web (la contraseña se guarda con hash SHA-256). */
    private void insertarDatosEjemplo(SQLiteDatabase bd) {
        bd.execSQL("INSERT INTO usuario (nombre, email, password, rol, estado) VALUES "
                + "('Isaac Ospino', 'admin@optigest.co', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Administrador', 1), "
                + "('Laura Martinez', 'lmartinez@optigest.co', '89f47494d297f9d32d560ed935ab7628ce85107354c58f48fe3208edd663cb3b', 'Vendedor', 1), "
                + "('Carlos Herrera', 'cherrera@optigest.co', '67d4c265e628c2223f2c5687d9a9d7008251449de410cf9c8970bb1a7bb0eb72', 'Jefe de taller', 1), "
                + "('Pedro Salcedo', 'psalcedo@optigest.co', 'd82cff8c766e6f52e11516f3c732c467e3370590097876e311086c3b839b3b5f', 'Jefe de compras', 0), "
                + "('Diana Ruiz', 'druiz@optigest.co', '05dd70aadc6f102a1b10a067f22bbe88d6ac2f4838fdccc789a21e8ac88e434f', 'Bodega', 1), "
                + "('Ana Gomez', 'agomez@optigest.co', '3a5974451e543318073bd4779677068dce9ef4746b4bd3ed40a6edc2989b58e7', 'Contador', 1)");

        bd.execSQL("INSERT INTO producto (nombre, tipo, precio_unitario, stock_actual) VALUES "
                + "('Perfil de aluminio 10x5 cm', 'Perfil', 85000, 120), "
                + "('Vidrio templado 6 mm', 'Vidrio', 120000, 60), "
                + "('Bisagra reforzada', 'Herraje', 15000, 300)");

        bd.execSQL("INSERT INTO proveedor (nombre, telefono, email, direccion) VALUES "
                + "('Aluminios del Caribe S.A.', '3016789234', 'jtorres@alucaribe.co', 'Cra 45 #12-30, Barranquilla'), "
                + "('Vidrex Colombia Ltda.', '3124567890', 'scano@vidrex.co', 'Calle 8 #22-15, Cartagena'), "
                + "('Herrajes & Accesorios SAS', '3209876543', 'mrincon@herrajes.co', 'Av. Circunvalar #5-40, Santa Marta')");
    }
}
