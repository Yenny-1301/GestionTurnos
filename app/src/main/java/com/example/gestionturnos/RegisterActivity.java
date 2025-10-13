package com.example.gestionturnos;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        EditText correo = findViewById(R.id.textoInput);
        EditText contrasenia = findViewById(R.id.textoPass);
        EditText contraseniaVerif = findViewById(R.id.textoPassVerif);
        Button iniciarSesion = findViewById(R.id.inicioSesion);
        CheckBox terminosCond = findViewById(R.id.checkbox_terminos);
        TextView checkboxError = findViewById(R.id.checkboxError);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean camposValidos = true;
                // MANEJO DE ERRORS
                if (correo.getText().toString().trim().isEmpty()) {
                    correo.setError("Ingresa tu correo");
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
                //el checkbox debe estar chequeado
                if (!terminosCond.isChecked()){
                    checkboxError.setVisibility(View.VISIBLE);
                    checkboxError.setText("Debes aceptar los Términos y condiciones");
                    camposValidos = false;
                }

                if (camposValidos) {
                    // Continuar a iniciar sesion
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                EditText contrasenia = findViewById(R.id.textoPass);
                EditText contraseniaVerif = findViewById(R.id.textoPassVerif);

                ImageView iconoPass = findViewById(R.id.iconoPass);
                ImageView iconoPassVerif = findViewById(R.id.iconoPassVerif);

                // Toggle mostrar contraseña
                iconoPass.setOnClickListener(new View.OnClickListener() {
                    boolean visible = false;

                    @Override
                    public void onClick(View v) {
                        if (visible) {
                            contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            iconoPass.setImageResource(R.drawable.icon_passclose);
                        } else {
                            contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            iconoPass.setImageResource(R.drawable.icon_passopen);
                        }
                        visible = !visible;
                        contrasenia.setSelection(contrasenia.getText().length());
                    }
                });

                // Toggle mostrar contraseña para verificación
                iconoPassVerif.setOnClickListener(new View.OnClickListener() {
                    boolean visible = false;

                    @Override
                    public void onClick(View v) {
                        if (visible) {
                            contraseniaVerif.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            iconoPassVerif.setImageResource(R.drawable.icon_passclose);
                        } else {
                            contraseniaVerif.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            iconoPassVerif.setImageResource(R.drawable.icon_passopen);
                        }
                        visible = !visible;
                        contraseniaVerif.setSelection(contraseniaVerif.getText().length());
                    }
                });

            }});
                TextView ingresaAqui = findViewById(R.id.ingresaAqui);
                    ingresaAqui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });


    }
    }