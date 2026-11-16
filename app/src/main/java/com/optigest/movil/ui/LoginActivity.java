package com.optigest.movil.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.optigest.movil.R;
import com.optigest.movil.datos.UsuarioDAO;
import com.optigest.movil.modelo.Usuario;
import com.optigest.movil.util.Seguridad;
import com.optigest.movil.util.Sesion;

/** Pantalla de inicio de sesión: valida correo y contraseña (hash SHA-256) contra SQLite. */
public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoClave;
    private TextView textoError;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioDAO = new UsuarioDAO(this);
        campoEmail = findViewById(R.id.campoEmail);
        campoClave = findViewById(R.id.campoClave);
        textoError = findViewById(R.id.textoError);
        Button botonIngresar = findViewById(R.id.botonIngresar);
        botonIngresar.setOnClickListener(v -> intentarIngreso());
    }

    private void intentarIngreso() {
        String email = campoEmail.getText().toString().trim();
        String clave = campoClave.getText().toString();

        if (email.isEmpty() || clave.isEmpty()) {
            textoError.setText("Ingresa el correo y la contraseña.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(email, Seguridad.hashearClave(clave));
        if (usuario == null) {
            textoError.setText("Correo o contraseña incorrectos, o el usuario está inactivo.");
            return;
        }

        Sesion.iniciar(usuario);
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}
