package com.optigest.movil.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Utilidades de seguridad: hash SHA-256 de contraseñas (igual al sistema web). */
public final class Seguridad {

    private Seguridad() {
        // Clase de utilidad: no debe instanciarse.
    }

    public static String hashearClave(String clave) {
        try {
            MessageDigest resumen = MessageDigest.getInstance("SHA-256");
            byte[] bytes = resumen.digest(clave.getBytes(StandardCharsets.UTF_8));
            StringBuilder texto = new StringBuilder();
            for (byte b : bytes) {
                texto.append(String.format("%02x", b));
            }
            return texto.toString();
        } catch (NoSuchAlgorithmException excepcion) {
            throw new IllegalStateException("SHA-256 no disponible", excepcion);
        }
    }
}
