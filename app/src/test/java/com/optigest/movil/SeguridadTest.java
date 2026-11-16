package com.optigest.movil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.optigest.movil.util.Seguridad;

import org.junit.Test;

public class SeguridadTest {

    @Test
    public void hashDeAdmin123CoincideConElDeLaBaseDeDatos() {
        assertEquals("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9",
                Seguridad.hashearClave("admin123"));
    }

    @Test
    public void clavesDistintasGeneranHashDistinto() {
        assertNotEquals(Seguridad.hashearClave("lm123"), Seguridad.hashearClave("ch123"));
    }

    @Test
    public void elHashTiene64CaracteresHexadecimales() {
        assertEquals(64, Seguridad.hashearClave("cualquier").length());
    }
}
