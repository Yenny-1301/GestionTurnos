package com.example.gestionturnos;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.gestionturnos.data.repository.ServicioRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.textfield.TextInputLayout;
import android.view.ViewParent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ServicioNuevoFragment extends Fragment {
    private TextInputEditText etNombreServicioNuevo, etDuracionNuevo, etPrecioNuevo;
    private MaterialButton btnGuardar, btnCancelar;
    private MaterialTextView etTituloServicio;
    private Servicio servicioOriginal;
    public static final String REQUEST_KEY_NUEVO = "nuevoServicioRequest";
    public static final String REQUEST_KEY_EDITADO = "servicioEditadoRequest";

    public ServicioNuevoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicio_nuevo, container, false);

        etNombreServicioNuevo = view.findViewById(R.id.etNombreServicioNuevo);
        etDuracionNuevo = view.findViewById(R.id.etDuracionNuevo);
        etPrecioNuevo = view.findViewById(R.id.etPrecioNuevo);
        etTituloServicio = view.findViewById(R.id.tvTituloFormularioServicio);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        addClearErrorOnChange(etNombreServicioNuevo);
        addClearErrorOnChange(etDuracionNuevo);
        addClearErrorOnChange(etPrecioNuevo);

        TextWatcher limpiarErrorWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (etNombreServicioNuevo.hasFocus()) {
                    etNombreServicioNuevo.setError(null);
                } else if (etDuracionNuevo.hasFocus()) {
                    etDuracionNuevo.setError(null);
                } else if (etPrecioNuevo.hasFocus()) {
                    etPrecioNuevo.setError(null);
                }
            }
        };

        etNombreServicioNuevo.addTextChangedListener(limpiarErrorWatcher);
        etDuracionNuevo.addTextChangedListener(limpiarErrorWatcher);
        etPrecioNuevo.addTextChangedListener(limpiarErrorWatcher);

        btnGuardar.setOnClickListener(v -> guardarServicio(v));
        btnCancelar.setOnClickListener(v -> closeFragment());

        return view;
    }

    private void closeFragment() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else {
            requireActivity().finish();
        }
    }
    private void guardarServicio(View view) {

        if (!validarFormulario()) {
            // si la validación falla, no seguimos
            return;
        }

        final String requestKey;
        final Servicio servicioParaEnviar;
        ServicioRepository repo = new ServicioRepository(requireContext());

        if (servicioOriginal != null) {
            requestKey = REQUEST_KEY_EDITADO;
            servicioParaEnviar = servicioOriginal;

            servicioParaEnviar.setNombreServicio(etNombreServicioNuevo.getText().toString());
            servicioParaEnviar.setMinutos(etDuracionNuevo.getText().toString());
            servicioParaEnviar.setPrecio(etPrecioNuevo.getText().toString());

            repo.actualizarServicio(servicioParaEnviar.getId(), servicioParaEnviar);

        } else {
            requestKey = REQUEST_KEY_NUEVO;
            servicioParaEnviar = new Servicio();

            servicioParaEnviar.setNombreServicio(etNombreServicioNuevo.getText().toString());
            servicioParaEnviar.setMinutos(etDuracionNuevo.getText().toString());
            servicioParaEnviar.setPrecio(etPrecioNuevo.getText().toString());

            repo.insertarServicio(SessionManager.obtenerUsuarioActivo(requireContext()), servicioParaEnviar);
        }

        Bundle result = new Bundle();
        closeFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            servicioOriginal = (Servicio) getArguments().getSerializable("servicio");
        }

        if (servicioOriginal != null) {
            etTituloServicio.setText("Editar Servicio");
            etNombreServicioNuevo.setText(servicioOriginal.getNombreServicio());
            etDuracionNuevo.setText(servicioOriginal.getMinutos());
            etPrecioNuevo.setText(servicioOriginal.getPrecio());
        } else {
            etTituloServicio.setText("Nuevo Servicio");
        }

    }

    private boolean validarFormulario() {

        boolean esValido = true;

        String nombre     = etNombreServicioNuevo.getText().toString().trim();
        String minutosStr = etDuracionNuevo.getText().toString().trim();
        String precioStr  = etPrecioNuevo.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            setFieldError(etNombreServicioNuevo, "Completar campo");
            esValido = false;
        }

        if (TextUtils.isEmpty(minutosStr)) {
            setFieldError(etDuracionNuevo, "Completar campo");
            esValido = false;
        } else {
            try {
                int minutos = Integer.parseInt(minutosStr);
                if (minutos <= 0) {
                    setFieldError(etDuracionNuevo, "Los minutos deben ser mayores a 0");
                    esValido = false;
                }
            } catch (NumberFormatException e) {
                setFieldError(etDuracionNuevo, "Ingresá un número válido");
                esValido = false;
            }
        }

        if (TextUtils.isEmpty(precioStr)) {
            setFieldError(etPrecioNuevo, "Ingresá el precio");
            esValido = false;
        } else {
            try {
                double precio = Double.parseDouble(precioStr.replace(",", "."));
                if (precio <= 0) {
                    setFieldError(etPrecioNuevo, "El precio debe ser mayor a 0");
                    esValido = false;
                }
            } catch (NumberFormatException e) {
                setFieldError(etPrecioNuevo, "Ingresá un número válido");
                esValido = false;
            }
        }

        if (!esValido) {
            Toast.makeText(requireContext(),
                    "Completá correctamente todos los campos",
                    Toast.LENGTH_SHORT).show();
        }

        return esValido;
    }

    private void addClearErrorOnChange(TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                clearFieldError(editText);
            }
        });
    }

    private void setFieldError(TextInputEditText editText, String mensaje) {

        // No usamos editText.setError()
        TextInputLayout til = findTextInputLayout(editText);

        if (til != null) {
            til.setError(mensaje);
            til.setErrorEnabled(true);
        }
    }

    private void clearFieldError(TextInputEditText editText) {

        // No usamos editText.setError(null)
        TextInputLayout til = findTextInputLayout(editText);
        if (til != null) {
            til.setError(null);
            til.setErrorEnabled(false);
        }
    }

    private TextInputLayout findTextInputLayout(TextInputEditText editText) {
        ViewParent parent = editText.getParent();
        while (parent != null) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
