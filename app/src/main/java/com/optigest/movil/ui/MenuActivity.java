package com.optigest.movil.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.optigest.movil.R;
import com.optigest.movil.modelo.Usuario;
import com.optigest.movil.util.Permisos;
import com.optigest.movil.util.Sesion;

/** Menú principal: muestra solo los módulos que el rol del usuario puede usar. */
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Sesion.hayUsuario()) {
            irAlLogin();
            return;
        }

        setContentView(R.layout.activity_menu);
        Usuario usuario = Sesion.getUsuario();

        TextView textoBienvenida = findViewById(R.id.textoBienvenida);
        textoBienvenida.setText("Hola, " + usuario.getNombre() + "\nRol: " + usuario.getRol());

        configurarBoton(R.id.botonProductos, Permisos.PRODUCTOS, ProductosActivity.class);
        configurarBoton(R.id.botonProveedores, Permisos.PROVEEDORES, ProveedoresActivity.class);
        configurarBoton(R.id.botonCompras, Permisos.COMPRAS, ComprasActivity.class);

        Button botonSalir = findViewById(R.id.botonSalir);
        botonSalir.setOnClickListener(v -> {
            Sesion.cerrar();
            irAlLogin();
        });
    }

    private void configurarBoton(int idBoton, String modulo, Class<?> actividad) {
        Button boton = findViewById(idBoton);
        if (Sesion.puedeAcceder(modulo)) {
            boton.setOnClickListener(v -> startActivity(new Intent(this, actividad)));
        } else {
            boton.setVisibility(View.GONE);
        }
    }

    private void irAlLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
