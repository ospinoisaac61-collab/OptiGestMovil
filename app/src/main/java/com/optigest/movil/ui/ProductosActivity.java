package com.optigest.movil.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.optigest.movil.R;
import com.optigest.movil.datos.ProductoDAO;
import com.optigest.movil.modelo.Producto;
import com.optigest.movil.util.Permisos;
import com.optigest.movil.util.Sesion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Módulo Producto: listar, crear, editar y eliminar. */
public class ProductosActivity extends AppCompatActivity {

    private ProductoDAO productoDAO;
    private ListView lista;
    private List<Producto> productos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Sesion.puedeAcceder(Permisos.PRODUCTOS)) {
            Toast.makeText(this, "No tienes permiso para este módulo.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_lista);
        productoDAO = new ProductoDAO(this);

        ((TextView) findViewById(R.id.tituloLista)).setText("Productos");
        lista = findViewById(R.id.lista);
        Button botonAgregar = findViewById(R.id.botonAgregar);
        botonAgregar.setText("Agregar producto");
        botonAgregar.setOnClickListener(v -> mostrarDialogo(null));

        lista.setOnItemClickListener((padre, vista, posicion, id) ->
                mostrarDialogo(productos.get(posicion)));
        lista.setOnItemLongClickListener((padre, vista, posicion, id) -> {
            confirmarEliminar(productos.get(posicion));
            return true;
        });

        cargar();
    }

    private void cargar() {
        productos = productoDAO.listar();
        List<String> textos = new ArrayList<>();
        for (Producto p : productos) {
            textos.add(p.getNombre() + "\n" + p.getTipo() + " · "
                    + String.format(Locale.getDefault(), "$ %,.0f", p.getPrecioUnitario())
                    + " · Stock: " + p.getStockActual());
        }
        lista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textos));
    }

    private void mostrarDialogo(final Producto existente) {
        View vista = getLayoutInflater().inflate(R.layout.dialog_producto, null);
        final EditText campoNombre = vista.findViewById(R.id.campoNombre);
        final EditText campoTipo = vista.findViewById(R.id.campoTipo);
        final EditText campoPrecio = vista.findViewById(R.id.campoPrecio);
        final EditText campoStock = vista.findViewById(R.id.campoStock);

        if (existente != null) {
            campoNombre.setText(existente.getNombre());
            campoTipo.setText(existente.getTipo());
            campoPrecio.setText(String.valueOf(existente.getPrecioUnitario()));
            campoStock.setText(String.valueOf(existente.getStockActual()));
        }

        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle(existente == null ? "Nuevo producto" : "Editar producto")
                .setView(vista)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();
        dialogo.show();

        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nombre = campoNombre.getText().toString().trim();
            String tipo = campoTipo.getText().toString().trim();
            if (nombre.isEmpty()) {
                campoNombre.setError("Obligatorio");
                return;
            }
            if (tipo.isEmpty()) {
                campoTipo.setError("Obligatorio");
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(campoPrecio.getText().toString().trim());
            } catch (NumberFormatException excepcion) {
                campoPrecio.setError("Precio inválido");
                return;
            }
            if (precio < 0) {
                campoPrecio.setError("No puede ser negativo");
                return;
            }

            int stock;
            try {
                stock = Integer.parseInt(campoStock.getText().toString().trim());
            } catch (NumberFormatException excepcion) {
                campoStock.setError("Stock inválido");
                return;
            }
            if (stock < 0) {
                campoStock.setError("No puede ser negativo");
                return;
            }

            if (existente == null) {
                productoDAO.insertar(new Producto(0, nombre, tipo, precio, stock));
            } else {
                productoDAO.actualizar(new Producto(existente.getId(), nombre, tipo, precio, stock));
            }
            dialogo.dismiss();
            cargar();
        });
    }

    private void confirmarEliminar(final Producto producto) {
        new AlertDialog.Builder(this)
                .setMessage("¿Eliminar el producto \"" + producto.getNombre() + "\"?")
                .setPositiveButton("Eliminar", (dialogo, boton) -> {
                    productoDAO.eliminar(producto.getId());
                    cargar();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
