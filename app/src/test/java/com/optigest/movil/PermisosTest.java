package com.optigest.movil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.optigest.movil.util.Permisos;

import org.junit.Test;

public class PermisosTest {

    @Test
    public void administradorAccedeATodosLosModulos() {
        assertTrue(Permisos.puedeAcceder("Administrador", Permisos.PRODUCTOS));
        assertTrue(Permisos.puedeAcceder("Administrador", Permisos.PROVEEDORES));
        assertTrue(Permisos.puedeAcceder("Administrador", Permisos.COMPRAS));
    }

    @Test
    public void vendedorSoloAccedeAProductos() {
        assertTrue(Permisos.puedeAcceder("Vendedor", Permisos.PRODUCTOS));
        assertFalse(Permisos.puedeAcceder("Vendedor", Permisos.COMPRAS));
        assertFalse(Permisos.puedeAcceder("Vendedor", Permisos.PROVEEDORES));
    }

    @Test
    public void jefeDeComprasAccedeAComprasYProveedores() {
        assertTrue(Permisos.puedeAcceder("Jefe de compras", Permisos.COMPRAS));
        assertTrue(Permisos.puedeAcceder("Jefe de compras", Permisos.PROVEEDORES));
        assertFalse(Permisos.puedeAcceder("Jefe de compras", Permisos.PRODUCTOS));
    }

    @Test
    public void unRolDesconocidoNoTieneAcceso() {
        assertFalse(Permisos.puedeAcceder("Invitado", Permisos.COMPRAS));
    }
}
