package com.prueba.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class InicioSesion extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        mAuth = FirebaseAuth.getInstance();

        // 🔹 Campos
        EditText txtCorreo = findViewById(R.id.etCorreo);
        EditText txtPass = findViewById(R.id.etContrasena);
        ImageView ivTogglePass = findViewById(R.id.ivTogglePass);

        // 🔹 Botón Iniciar Sesión
        Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener(view -> {
            String correo = txtCorreo.getText().toString().trim();
            String pass = txtPass.getText().toString().trim();

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Complete ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            ingresar(correo, pass);
        });

        // 🔹 Texto para ir al registro
        TextView tvRegistro = findViewById(R.id.tvRegistro);
        tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(InicioSesion.this, Registro.class);
            startActivity(intent);
        });

        // 🔹 Candado (mostrar / ocultar contraseña)
        ivTogglePass.setOnClickListener(v -> {
            if (passwordVisible) {
                // Ocultar contraseña
                txtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePass.setImageResource(R.drawable.ic_visibility_off);
            } else {
                // Mostrar contraseña
                txtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePass.setImageResource(R.drawable.ic_visibility);
            }
            txtPass.setSelection(txtPass.getText().length()); // mantener cursor al final
            passwordVisible = !passwordVisible;
        });
    }

    // 🔹 Iniciar sesión con Firebase
    public void ingresar(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(InicioSesion.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 🔹 Si ya está logueado, pasa directo al Main
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(InicioSesion.this, MainActivity.class));
            finish();
        }
    }
}
