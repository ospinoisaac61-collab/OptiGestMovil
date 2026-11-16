package com.optigest.movil.ui;

import android.database.sqlite.SQLiteConstraintException;
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
import com.optigest.movil.datos.ProveedorDAO;
import com.optigest.movil.modelo.Proveedor;
import com.optigest.movil.util.Permisos;
import com.optigest.movil.util.Sesion;

import java.util.ArrayList;
import java.util.List;

/** Módulo Proveedor: listar, crear, editar y eliminar. */
public class ProveedoresActivity extends AppCompatActivity {

    private ProveedorDAO proveedorDAO;
    private ListView lista;
    private List<Proveedor> proveedores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Sesion.puedeAcceder(Permisos.PROVEEDORES)) {
            Toast.makeText(this, "No tienes permiso para este módulo.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_lista);
        proveedorDAO = new ProveedorDAO(this);

        ((TextView) findViewById(R.id.tituloLista)).setText("Proveedores");
        lista = findViewById(R.id.lista);
        Button botonAgregar = findViewById(R.id.botonAgregar);
        botonAgregar.setText("Agregar proveedor");
        botonAgregar.setOnClickListener(v -> mostrarDialogo(null));

        lista.setOnItemClickListener((padre, vista, posicion, id) ->
                mostrarDialogo(proveedores.get(posicion)));
        lista.setOnItemLongClickListener((padre, vista, posicion, id) -> {
            confirmarEliminar(proveedores.get(posicion));
            return true;
        });

        cargar();
    }

    private void cargar() {
        proveedores = proveedorDAO.listar();
        List<String> textos = new ArrayList<>();
        for (Proveedor p : proveedores) {
            textos.add(p.getNombre() + "\n" + valor(p.getTelefono()) + " · " + valor(p.getEmail())
                    + "\n" + valor(p.getDireccion()));
        }
        lista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textos));
    }

    private String valor(String texto) {
        return texto == null ? "-" : texto;
    }

    /** Devuelve null si el campo está vacío (así el correo vacío no choca con la restricción UNIQUE). */
    private String textoOpcional(EditText campo) {
        String texto = campo.getText().toString().trim();
        return texto.isEmpty() ? null : texto;
    }

    private void mostrarDialogo(final Proveedor existente) {
        View vista = getLayoutInflater().inflate(R.layout.dialog_proveedor, null);
        final EditText campoNombre = vista.findViewById(R.id.campoNombre);
        final EditText campoTelefono = vista.findViewById(R.id.campoTelefono);
        final EditText campoEmail = vista.findViewById(R.id.campoEmail);
        final EditText campoDireccion = vista.findViewById(R.id.campoDireccion);

        if (existente != null) {
            campoNombre.setText(existente.getNombre());
            campoTelefono.setText(existente.getTelefono());
            campoEmail.setText(existente.getEmail());
            campoDireccion.setText(existente.getDireccion());
        }

        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle(existente == null ? "Nuevo proveedor" : "Editar proveedor")
                .setView(vista)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();
        dialogo.show();

        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nombre = campoNombre.getText().toString().trim();
            if (nombre.isEmpty()) {
                campoNombre.setError("Obligatorio");
                return;
            }

            try {
                if (existente == null) {
                    proveedorDAO.insertar(new Proveedor(0, nombre, textoOpcional(campoTelefono),
                            textoOpcional(campoEmail), textoOpcional(campoDireccion)));
                } else {
                    proveedorDAO.actualizar(new Proveedor(existente.getId(), nombre,
                            textoOpcional(campoTelefono), textoOpcional(campoEmail),
                            textoOpcional(campoDireccion)));
                }
            } catch (SQLiteConstraintException excepcion) {
                campoEmail.setError("Ese correo ya está registrado");
                return;
            }
            dialogo.dismiss();
            cargar();
        });
    }

    private void confirmarEliminar(final Proveedor proveedor) {
        new AlertDialog.Builder(this)
                .setMessage("¿Eliminar al proveedor \"" + proveedor.getNombre() + "\"?")
                .setPositiveButton("Eliminar", (dialogo, boton) -> {
                    try {
                        proveedorDAO.eliminar(proveedor.getId());
                        cargar();
                    } catch (SQLiteConstraintException excepcion) {
                        Toast.makeText(this, "No se puede eliminar: el proveedor tiene compras registradas.",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
