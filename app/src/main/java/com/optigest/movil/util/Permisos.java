package com.optigest.movil.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Reglas de acceso por rol (las mismas del sistema web). */
public final class Permisos {

    public static final String PRODUCTOS = "productos";
    public static final String PROVEEDORES = "proveedores";
    public static final String COMPRAS = "compras";

    private static final Map<String, Set<String>> MODULOS_POR_ROL = new HashMap<>();

    static {
        MODULOS_POR_ROL.put("Administrador", conjunto(PRODUCTOS, PROVEEDORES, COMPRAS));
        MODULOS_POR_ROL.put("Jefe de compras", conjunto(COMPRAS, PROVEEDORES));
        MODULOS_POR_ROL.put("Contador", conjunto(COMPRAS));
        MODULOS_POR_ROL.put("Bodega", conjunto(PRODUCTOS));
        MODULOS_POR_ROL.put("Vendedor", conjunto(PRODUCTOS));
        MODULOS_POR_ROL.put("Jefe de taller", conjunto(PRODUCTOS));
    }

    private Permisos() {
        // Clase de utilidad: no debe instanciarse.
    }

    private static Set<String> conjunto(String... modulos) {
        return new HashSet<>(Arrays.asList(modulos));
    }

    public static boolean puedeAcceder(String rol, String modulo) {
        Set<String> permitidos = MODULOS_POR_ROL.get(rol);
        return permitidos != null && permitidos.contains(modulo);
    }
}
