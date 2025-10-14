package com.example.gestionturnos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                if (correo.getText().toString().trim().isEmpty()) {
                    correo.setError("Ingresa tu correo");
                    camposValidos = false;
                }

                if (contraseña.getText().toString().trim().isEmpty()) {
                    contraseña.setError("Ingresa tu contraseña");
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
    }}