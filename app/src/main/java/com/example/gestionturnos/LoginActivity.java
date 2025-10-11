package com.example.gestionturnos;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        EditText correo = findViewById(R.id.textoInput);
        EditText contraseña = findViewById(R.id.textoPass);
        Button iniciarSesion = findViewById(R.id.inicioSesion);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean camposValidos = true;

                if (correo.getText().toString().trim().isEmpty()) {
                    correo.setError("Ingresa tu correo");
                    camposValidos = false;
                }

                if (contraseña.getText().toString().trim().isEmpty()) {
                    contraseña.setError("Ingresa tu contraseña");
                    camposValidos = false;
                }

                if (camposValidos) {
                    // Continuar con el inicio de sesión
                    // Por ejemplo, abrir la pantalla principal:
                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // startActivity(intent);
                }
                EditText contraseña = findViewById(R.id.textoPass);
                ImageView iconoPass = findViewById(R.id.iconoPass);

                iconoPass.setOnClickListener(new View.OnClickListener() {
                    boolean visible = false;

                    @Override
                    public void onClick(View v) {
                        if (visible) {
                            // Ocultar contraseña
                            contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            iconoPass.setImageResource(R.drawable.icon_passopen);
                        } else {
                            // Mostrar contraseña
                            contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            iconoPass.setImageResource(R.drawable.icon_passclose);
                        }
                        visible = !visible;
                        // Mantener el cursor al final
                        contraseña.setSelection(contraseña.getText().length());
                                                 }
                                             }
                );
            }
        });
    }}