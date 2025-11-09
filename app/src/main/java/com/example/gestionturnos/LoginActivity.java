package com.example.gestionturnos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        EditText correo = findViewById(R.id.textoInput);
        EditText contraseña = findViewById(R.id.textoPass);
        Button iniciarSesion = findViewById(R.id.inicioSesion);
        ImageView iconoPass = findViewById(R.id.iconoPass);
        TextView registrateAqui = findViewById(R.id.registrateAqui);

        TextInputLayout emailLayout = findViewById(R.id.emailInputLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordInputLayout);

        TextInputEditText emailEditText = findViewById(R.id.textoInput);
        TextInputEditText passwordEditText = findViewById(R.id.textoPass);

        iconoPass.setOnClickListener(new View.OnClickListener() {
            boolean visible = false;

            @Override
            public void onClick(View v) {
             if (visible) {
                 // Ocultar contraseña
                 contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                 iconoPass.setImageResource(R.drawable.icon_passclose);
             } else {
                 // Mostrar contraseña
                 contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                 iconoPass.setImageResource(R.drawable.icon_passopen);
             }
             visible = !visible;
             // Mantener el cursor al final
             contraseña.setSelection(contraseña.getText().length());
            }
            });
        //linkeo a la pantalla de registro
        registrateAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean camposValidos = true;
                //validacion de campos
                if (emailEditText.getText().toString().trim().isEmpty()) {
                    emailLayout.setError("Completar campo");
                    camposValidos = false;
                }

                if (passwordEditText.getText().toString().trim().isEmpty()) {
                    passwordLayout.setError("Completar campo");
                    camposValidos = false;
                }

                if (camposValidos) {
                    // guardar estado de logueado del usuario
                    SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                    // Continuar con el inicio de sesión si los campos fueron llenados correctamente
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Cierra la pantalla de inicio seision (no se puede volver atras a la misma)
                }
            }
        });
        // CONTROLA LOS CAMPOS DE IMPUTS, ESPERA UN CONTENIDO DENTRO PARA ELIMINAR EL MENSAJE DE ERROR
        TextWatcher clearErrorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Borra el error automáticamente cuando el usuario escribe
                if (emailEditText.hasFocus()) {
                    emailLayout.setError(null);
                }
                if (passwordEditText.hasFocus()) {
                    passwordLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

// Asignar el watcher a cada campo
        emailEditText.addTextChangedListener(clearErrorWatcher);
        passwordEditText.addTextChangedListener(clearErrorWatcher);

    }}