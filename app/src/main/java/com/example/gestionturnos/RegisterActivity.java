package com.example.gestionturnos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestionturnos.data.MyDatabaseHelper;
import com.example.gestionturnos.data.dao.UserDAO;
import com.google.android.material.button.MaterialButton;


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
        TextView ingresaAqui = findViewById(R.id.ingresaAqui);
                            ingresaAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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
                if (!contrasenia.getText().toString().trim().equals(contraseniaVerif.getText().toString().trim())) {
                    contraseniaVerif.setError("Contraseña no coincide");
                    camposValidos = false;
                }
                //el checkbox debe estar chequeado
                if (!terminosCond.isChecked()){
                    checkboxError.setVisibility(View.VISIBLE);
                    checkboxError.setText("Debes aceptar los Términos y condiciones");
                    camposValidos = false;
                }
                if (camposValidos) {
                    UserDAO userData = new UserDAO(RegisterActivity.this);

//                    if (userData.getUserByEmail(correo.getText().toString().trim()) == null) {
//                        Toast.makeText(RegisterActivity.this, "El correo ya está registrado", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    Usuario user = new Usuario();
                    user.setEmail( correo.getText().toString().trim());
                    user.setPassword(contrasenia.getText().toString().trim());
                    userData.addUser(user);
                    mostrarDialogRegistro();}}

                    //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    //startActivity(intent);
                    //finish();
            private void mostrarDialogRegistro() {
                Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.registro_mensaje);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);

                // ajustar tamaño de dialogo
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,  // ancho
                            ViewGroup.LayoutParams.WRAP_CONTENT   // alto
                    );
                    // agrega márgenes laterales
                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 90% del ancho de pantalla
                    window.setAttributes(layoutParams);
                }

                // botones del dialogo
                MaterialButton btnIrAInicio = dialog.findViewById(R.id.btnIrAInicio);

                btnIrAInicio.setOnClickListener(v -> {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });

                dialog.show();
        }
            });
    }
};