package com.optigest.movil.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.optigest.movil.R;
import com.optigest.movil.datos.CompraDAO;
import com.optigest.movil.datos.ProveedorDAO;
import com.optigest.movil.modelo.Compra;
import com.optigest.movil.modelo.Proveedor;
import com.optigest.movil.util.Permisos;
import com.optigest.movil.util.Sesion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Módulo Compra: depende de Proveedor y Usuario (consulta con JOIN). */
public class ComprasActivity extends AppCompatActivity {

    private static final String[] ESTADOS = {"PENDIENTE", "COMPLETADA", "CANCELADA"};

    private CompraDAO compraDAO;
    private ProveedorDAO proveedorDAO;
    private ListView lista;
    private List<Compra> compras = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Sesion.puedeAcceder(Permisos.COMPRAS)) {
            Toast.makeText(this, "No tienes permiso para este módulo.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_lista);
        compraDAO = new CompraDAO(this);
        proveedorDAO = new ProveedorDAO(this);

        ((TextView) findViewById(R.id.tituloLista)).setText("Compras");
        lista = findViewById(R.id.lista);
        Button botonAgregar = findViewById(R.id.botonAgregar);
        botonAgregar.setText("Registrar compra");
        botonAgregar.setOnClickListener(v -> mostrarDialogo(null));

        lista.setOnItemClickListener((padre, vista, posicion, id) ->
                mostrarDialogo(compras.get(posicion)));
        lista.setOnItemLongClickListener((padre, vista, posicion, id) -> {
            confirmarEliminar(compras.get(posicion));
            return true;
        });

        cargar();
    }

    private void cargar() {
        compras = compraDAO.listar();
        List<String> textos = new ArrayList<>();
        for (Compra c : compras) {
            textos.add("Compra #" + c.getId() + " · " + c.getEstado()
                    + "\nProveedor: " + c.getNombreProveedor() + " · Registró: " + c.getNombreUsuario()
                    + "\nTotal: " + String.format(Locale.getDefault(), "$ %,.0f", c.getTotal())
                    + " · " + c.getFecha());
        }
        lista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textos));
    }

    private void mostrarDialogo(final Compra existente) {
        final List<Proveedor> proveedores = proveedorDAO.listar();
        if (proveedores.isEmpty()) {
            Toast.makeText(this, "Primero registra un proveedor.", Toast.LENGTH_LONG).show();
            return;
        }

        View vista = getLayoutInflater().inflate(R.layout.dialog_compra, null);
        final EditText campoTotal = vista.findViewById(R.id.campoTotal);
        final Spinner spinnerEstado = vista.findViewById(R.id.spinnerEstado);
        final Spinner spinnerProveedor = vista.findViewById(R.id.spinnerProveedor);

        spinnerEstado.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ESTADOS));
        spinnerProveedor.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, proveedores));

        if (existente != null) {
            campoTotal.setText(String.valueOf(existente.getTotal()));
            for (int i = 0; i < ESTADOS.length; i++) {
                if (ESTADOS[i].equals(existente.getEstado())) {
                    spinnerEstado.setSelection(i);
                }
            }
            for (int i = 0; i < proveedores.size(); i++) {
                if (proveedores.get(i).getId() == existente.getIdProveedor()) {
                    spinnerProveedor.setSelection(i);
                }
            }
        }

        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle(existente == null ? "Nueva compra" : "Editar compra")
                .setView(vista)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();
        dialogo.show();

        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            double total;
            try {
                total = Double.parseDouble(campoTotal.getText().toString().trim());
            } catch (NumberFormatException excepcion) {
                campoTotal.setError("Total inválido");
                return;
            }
            if (total <= 0) {
                campoTotal.setError("Debe ser mayor que cero");
                return;
            }

            String estado = (String) spinnerEstado.getSelectedItem();
            Proveedor proveedor = (Proveedor) spinnerProveedor.getSelectedItem();

            if (existente == null) {
                compraDAO.insertar(total, estado, proveedor.getId(), Sesion.getUsuario().getId());
            } else {
                compraDAO.actualizar(existente.getId(), total, estado, proveedor.getId());
            }
            dialogo.dismiss();
            cargar();
        });
    }

    private void confirmarEliminar(final Compra compra) {
        new AlertDialog.Builder(this)
                .setMessage("¿Eliminar la compra #" + compra.getId() + "?")
                .setPositiveButton("Eliminar", (dialogo, boton) -> {
                    compraDAO.eliminar(compra.getId());
                    cargar();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
