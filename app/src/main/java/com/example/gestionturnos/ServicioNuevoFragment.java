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

import com.example.gestionturnos.data.repository.ServicioRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

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

        final String requestKey;
        final Servicio servicioParaEnviar;

        if (servicioOriginal != null) {
            requestKey = REQUEST_KEY_EDITADO;
            servicioParaEnviar = servicioOriginal;
        } else {
            requestKey = REQUEST_KEY_NUEVO;
            servicioParaEnviar = new Servicio();
        }
        servicioParaEnviar.setNombreServicio(etNombreServicioNuevo.getText().toString());
        servicioParaEnviar.setMinutos(etDuracionNuevo.getText().toString());
        servicioParaEnviar.setPrecio(etPrecioNuevo.getText().toString());

        ServicioRepository repo = new ServicioRepository(requireContext());
        repo.insertarServicio(1, servicioParaEnviar);

        Bundle result = new Bundle();
        result.putSerializable("servicio", servicioParaEnviar);
        getParentFragmentManager().setFragmentResult(requestKey, result);
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
}
