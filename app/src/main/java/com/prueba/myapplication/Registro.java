package com.prueba.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity {

    private FirebaseAuth oFirebaseAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Ir al inicio de sesión
        TextView tvRegistro = findViewById(R.id.tvIrLogin);
        tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(Registro.this, InicioSesion.class);
            startActivity(intent);
        });

        oFirebaseAuth = FirebaseAuth.getInstance();

        // Campos de entrada
        EditText correo1 = findViewById(R.id.etCorreoRegistro);
        EditText pass1 = findViewById(R.id.etContrasenaRegistro);
        EditText pass2 = findViewById(R.id.etConfirmarContrasena);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        // Íconos de visibilidad de contraseñas
        ImageView toggle1 = findViewById(R.id.ivTogglePass1);
        ImageView toggle2 = findViewById(R.id.ivTogglePass2);

        // Estados de visibilidad
        final boolean[] visible1 = {false};
        final boolean[] visible2 = {false};

        // Mostrar / ocultar contraseña 1
        toggle1.setOnClickListener(v -> {
            if (visible1[0]) {
                pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggle1.setImageResource(R.drawable.ic_visibility_off);
            } else {
                pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggle1.setImageResource(R.drawable.ic_visibility);
            }
            pass1.setSelection(pass1.getText().length());
            visible1[0] = !visible1[0];
        });

        // Mostrar / ocultar contraseña 2
        toggle2.setOnClickListener(v -> {
            if (visible2[0]) {
                pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggle2.setImageResource(R.drawable.ic_visibility_off);
            } else {
                pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggle2.setImageResource(R.drawable.ic_visibility);
            }
            pass2.setSelection(pass2.getText().length());
            visible2[0] = !visible2[0];
        });

        // Acción del botón de registro
        btnRegistrar.setOnClickListener(view -> crearRegistro(correo1, pass1, pass2));
    }

    private void crearRegistro(EditText correo1, EditText pass1, EditText pass2) {
        String correo = correo1.getText().toString().trim();
        String pass = pass1.getText().toString().trim();
        String passConfirm = pass2.getText().toString().trim();

        // Validaciones básicas
        if (correo.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(passConfirm)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registro en Firebase
        oFirebaseAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registro.this, InicioSesion.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    }
                });
    }
}
