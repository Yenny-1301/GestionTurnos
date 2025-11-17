package com.example.gestionturnos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.gestionturnos.data.entities.UsuarioEntity;
import com.example.gestionturnos.data.repository.UsuarioRepository;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        EditText correo = findViewById(R.id.textoInput);
        EditText contrasenia = findViewById(R.id.textoPass);
        Button iniciarSesion = findViewById(R.id.inicioSesion);
        ImageView iconoPass = findViewById(R.id.iconoPass);
        TextView registrateAqui = findViewById(R.id.registrateAqui);

        TextInputLayout emailLayout = findViewById(R.id.emailInputLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordInputLayout);

        TextInputEditText emailEditText = findViewById(R.id.textoInput);
        TextInputEditText passwordEditText = findViewById(R.id.textoPass);

        initBiometric();
        MaterialButton btnHuella = findViewById(R.id.btnHuella);
        btnHuella.setOnClickListener(v -> {
            if (!SessionManager.huellaHabilitada(LoginActivity.this)) {
                Toast.makeText(this,
                        "Primero ingresa con usuario y contraseña",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            biometricPrompt.authenticate(promptInfo);
        });

        // VERIFICAR QUE EL DISPOSITIVO PUEDE USAR BIOMETRIA
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                btnHuella.setVisibility(View.VISIBLE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                btnHuella.setVisibility(View.GONE);
                break;
        }


        // con esto se fuerrza a que la pass no muestre
        contrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        contrasenia.setTypeface(android.graphics.Typeface.DEFAULT);

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
                boolean usuarioExistente = false;
                //validacion de campos
                if (emailEditText.getText().toString().trim().isEmpty()) {
                    emailLayout.setError("Completar campo");
                    camposValidos = false;
                }

                if (passwordEditText.getText().toString().trim().isEmpty()) {
                    passwordLayout.setError("Completar campo");
                    camposValidos = false;
                }

                UsuarioRepository repo = new UsuarioRepository(LoginActivity.this);
                usuarioExistente = repo.validarLogin(correo.getText().toString(), contrasenia.getText().toString());

                if (!usuarioExistente) {
                    correo.setError("Usuario Incorrecto");
                    camposValidos = false;
                }

                //Agregar alerta de error
                if (camposValidos) {
                    UsuarioEntity usuario = repo.obtenerUsuarioPorEmail(correo.getText().toString());
                    SessionManager.guardarUsuarioActivo(LoginActivity.this, usuario.id);

                    BiometricManager biometricManager = BiometricManager.from(LoginActivity.this);
                    boolean puedeUsarHuella =
                            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                                    == BiometricManager.BIOMETRIC_SUCCESS;

                    if (SessionManager.huellaYaPreguntada(LoginActivity.this) || !puedeUsarHuella) {
                        irAlHome();
                        return;
                    }

                    SessionManager.marcarHuellaPreguntada(LoginActivity.this);

                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("¿Habilitar inicio con huella?")
                            .setMessage("Podrás iniciar sesión sin escribir tu contraseña.")
                            .setPositiveButton("Sí", (dialog, which) -> {
                                SessionManager.habilitarHuella(LoginActivity.this, usuario.id);
                                irAlHome();
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                irAlHome();
                            })
                            .setCancelable(false)
                            .show();
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

    }

    private void initBiometric() {
        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        int userId = SessionManager.obtenerUsuarioHuella(LoginActivity.this);
                        SessionManager.guardarUsuarioActivo(LoginActivity.this, userId);

                        Toast.makeText(LoginActivity.this,
                                "Autenticación exitosa",
                                Toast.LENGTH_SHORT).show();

                        irAlHome();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode,
                                                      @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(LoginActivity.this,
                                "Error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(LoginActivity.this,
                                "Intentá nuevamente", Toast.LENGTH_SHORT).show();
                    }
                });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Iniciar sesión con huella")
                .setSubtitle("Usá tu huella para ingresar")
                .setNegativeButtonText("Cancelar")
                .build();
    }

    private void irAlHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}