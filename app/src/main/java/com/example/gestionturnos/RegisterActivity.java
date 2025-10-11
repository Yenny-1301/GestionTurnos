package com.example.gestionturnos;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        EditText correo = findViewById(R.id.textoInput);
        EditText correoVerif = findViewById(R.id.textoInputVerif);
        EditText contrasenia = findViewById(R.id.textoPass);
        EditText contraseniaVerif = findViewById(R.id.textoPassVerif);
        Button iniciarSesion = findViewById(R.id.inicioSesion);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean camposValidos = true;

                if (correo.getText().toString().trim().isEmpty()) {
                    correo.setError("Ingresa tu correo");
                    camposValidos = false;
                }
                if (correoVerif.getText().toString().trim().isEmpty()) {
                    correoVerif.setError("Ingresa tu correo");
                    camposValidos = false;
                }

                if (contrasenia.getText().toString().trim().isEmpty()) {
                    contrasenia.setError("Ingresa tu contraseña");
                    camposValidos = false;
                }
                if (contraseniaVerif.getText().toString().trim().isEmpty()) {
                    contraseniaVerif.setError("Ingresa tu contraseña");
                    camposValidos = false;
                }

                if (camposValidos) {
                    // Continuar con el inicio de sesión
                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // startActivity(intent);
                }
                EditText contrasenia = findViewById(R.id.textoPass);
                EditText contraseniaVerif = findViewById(R.id.textoPassVerif);

                ImageView iconoPass = findViewById(R.id.iconoPass);
                ImageView iconoPassVerif = findViewById(R.id.iconoPassVerif);

// Toggle para contraseña principal
                iconoPass.setOnClickListener(new View.OnClickListener() {
                    boolean visible = false;

                    @Override
                    public void onClick(View v) {
                        if (visible) {
                            contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            iconoPass.setImageResource(R.drawable.icon_passopen);
                        } else {
                            contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            iconoPass.setImageResource(R.drawable.icon_passclose);
                        }
                        visible = !visible;
                        contrasenia.setSelection(contrasenia.getText().length());
                    }
                });

// Toggle para verificación de contraseña
                iconoPassVerif.setOnClickListener(new View.OnClickListener() {
                    boolean visible = false;

                    @Override
                    public void onClick(View v) {
                        if (visible) {
                            contraseniaVerif.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            iconoPassVerif.setImageResource(R.drawable.icon_passopen);
                        } else {
                            contraseniaVerif.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            iconoPassVerif.setImageResource(R.drawable.icon_passclose);
                        }
                        visible = !visible;
                        contraseniaVerif.setSelection(contraseniaVerif.getText().length());
                    }
                });

            }});
        }
    }