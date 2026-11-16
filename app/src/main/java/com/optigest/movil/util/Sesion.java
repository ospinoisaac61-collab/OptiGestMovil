package com.optigest.movil.util;

import com.optigest.movil.modelo.Usuario;

/** Guarda el usuario que inició sesión mientras la aplicación está abierta. */
public final class Sesion {

    private static Usuario usuarioActual;

    private Sesion() {
        // Clase de utilidad: no debe instanciarse.
    }

    public static void iniciar(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static boolean hayUsuario() {
        return usuarioActual != null;
    }

    public static boolean puedeAcceder(String modulo) {
        return usuarioActual != null && Permisos.puedeAcceder(usuarioActual.getRol(), modulo);
    }

    public static void cerrar() {
        usuarioActual = null;
    }
}
