package com.example.gestionturnos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.gestionturnos.data.entities.UsuarioEntity;
import com.example.gestionturnos.data.repository.UsuarioRepository;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        EditText nombre = findViewById(R.id.textoNombre);
        EditText correo = findViewById(R.id.textoInput);
        EditText contrasenia = findViewById(R.id.textoPass);
        EditText contraseniaVerif = findViewById(R.id.textoPassVerif);
        Button iniciarSesion = findViewById(R.id.inicioSesion);

        ImageView iconoPass = findViewById(R.id.iconoPass);
        ImageView iconoPassVerif = findViewById(R.id.iconoPassVerif);

        TextInputLayout nameLayout = findViewById(R.id.nameInputLayout);
        TextInputLayout emailLayout = findViewById(R.id.emailInputLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordInputLayout);
        TextInputLayout passwordVerifLayout = findViewById(R.id.passwordInputLayoutPass);
        TextInputEditText nameEditText = findViewById(R.id.textoNombre);
        TextInputEditText emailEditText = findViewById(R.id.textoInput);
        TextInputEditText passwordEditText = findViewById(R.id.textoPass);
        TextInputEditText passwordVerifEditText = findViewById(R.id.textoPassVerif);

        // forzar que no se visualice la pass al comienzo
        contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        contrasenia.setTypeface(android.graphics.Typeface.DEFAULT);


        // Toggle mostrar contraseña
        iconoPass.setOnClickListener(new View.OnClickListener() {
            boolean visible = true;

            @Override
            public void onClick(View v) {
                if (visible) {
                    // Mostrar contraseña
                    contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    contrasenia.setTypeface(android.graphics.Typeface.DEFAULT);
                    iconoPass.setImageResource(R.drawable.icon_passopen);
                    visible = false;
                } else {
                    // Ocultar contraseña
                    contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    contrasenia.setTypeface(android.graphics.Typeface.DEFAULT);
                    iconoPass.setImageResource(R.drawable.icon_passclose);
                    visible = true;
                }
                contrasenia.setSelection(contrasenia.getText().length());
            }
        });

        // Toggle mostrar contraseña para verificación
        iconoPassVerif.setOnClickListener(new View.OnClickListener() {
            boolean visible = true;

            @Override
            public void onClick(View v) {
                if (visible) {
                    // Mostrar contraseña
                    contraseniaVerif.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    contraseniaVerif.setTypeface(android.graphics.Typeface.DEFAULT);
                    iconoPassVerif.setImageResource(R.drawable.icon_passopen);
                    visible = false;
                } else {
                    // Ocultar contraseña
                    contraseniaVerif.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    contraseniaVerif.setTypeface(android.graphics.Typeface.DEFAULT);
                    iconoPassVerif.setImageResource(R.drawable.icon_passclose);
                    visible = true;
                }
                contraseniaVerif.setSelection(contraseniaVerif.getText().length());
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

                //VALIDACION NOMBRE
                if (nameEditText.getText().toString().trim().isEmpty()) {
                    nameLayout.setError("Completar campo");
                    camposValidos = false;
                } else {
                    nameLayout.setError(null);
                }
                // VALIDACION CORREO
                if (emailEditText.getText().toString().trim().isEmpty()) {
                    emailLayout.setError("Completar campo");
                    camposValidos = false;
                }else if (!esEmailValido(emailEditText.getText().toString().trim())) {
                    emailLayout.setError("Correo inválido");
                    camposValidos = false;
                }

                //VALIDACION CONTRASENIA
                if (passwordEditText.getText().toString().trim().isEmpty()) {
                    passwordLayout.setError("Completar campo");
                    camposValidos = false;
                }else if (!esContraseniaValida(passwordEditText.getText().toString().trim())) {
                    passwordLayout.setError("Debe tener al menos 8 caracteres, una mayúscula y un caracter especial");
                    camposValidos = false;
                }
                if (!contrasenia.getText().toString().trim().equals(contraseniaVerif.getText().toString().trim())) {
                    contraseniaVerif.setError("Contraseña no coincide");
                    camposValidos = false;
                }

                UsuarioRepository repo = new UsuarioRepository(RegisterActivity.this);
                UsuarioEntity usuarioRegistrado = repo.obtenerUsuarioPorEmail(correo.getText().toString());

                if(usuarioRegistrado != null){
                    correo.setError("El correo ya se encuentra registrado");
                    camposValidos = false;
                }

                if (camposValidos) {

                    Usuario user = new Usuario();
                    user.setNombre(nombre.getText().toString().trim());
                    user.setEmail( correo.getText().toString().trim());
                    user.setPassword(contrasenia.getText().toString().trim());
                    long id = repo.insertarUsuario(user);

                    if(id > 0){
                        SessionManager.guardarUsuarioActivo(RegisterActivity.this, (int) id);
                    }
                    mostrarDialogRegistro();}}

                    private void mostrarDialogRegistro() {
                        Dialog dialog = new Dialog(RegisterActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.registro_mensaje);

                        Window window = dialog.getWindow();
                        if (window != null) {
                            // Fondo completamente transparente para el window
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            // Configurar tamaño
                            window.setLayout(
                                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );

                            window.setFlags(
                                    WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                                    WindowManager.LayoutParams.FLAG_DIM_BEHIND
                            );
                            window.setDimAmount(0.8f);
                        }

                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);

                        MaterialButton btnIrAInicio = dialog.findViewById(R.id.btnIrAInicio);
                        btnIrAInicio.setOnClickListener(v -> {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });

                        dialog.show();
                    }
            });

        TextWatcher clearErrorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nameEditText.hasFocus()) {
                    nameLayout.setError(null);
                }
                if (emailEditText.hasFocus()) {
                    emailLayout.setError(null);
                }
                if (passwordEditText.hasFocus()) {
                    passwordLayout.setError(null);
                }
                if (passwordVerifEditText.hasFocus()) {
                    passwordVerifLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        nameEditText.addTextChangedListener(clearErrorWatcher);
        emailEditText.addTextChangedListener(clearErrorWatcher);
        passwordEditText.addTextChangedListener(clearErrorWatcher);
        passwordVerifEditText.addTextChangedListener(clearErrorWatcher);
    }
    private boolean esEmailValido(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean esContraseniaValida(String pass) {
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=?]).{8,}$";
        return pass != null && pass.matches(regex);
    }
}