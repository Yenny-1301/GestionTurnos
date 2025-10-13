package com.example.gestionturnos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ServicioNuevoFragment extends Fragment {
    private TextInputEditText etNombreServicioNuevo, etDuracionNuevo, etPrecioNuevo;
    private MaterialButton btnGuardar, btnCancelar;

    public ServicioNuevoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicio_nuevo, container, false);

        etNombreServicioNuevo = view.findViewById(R.id.etNombreServicioNuevo);
        etDuracionNuevo = view.findViewById(R.id.etDuracionNuevo);
        etPrecioNuevo = view.findViewById(R.id.etPrecioNuevo);
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
        Servicio nuevoServicio = new Servicio(
                etNombreServicioNuevo.getText().toString(),
                etDuracionNuevo.getText().toString(),
                etPrecioNuevo.getText().toString()
        );

        Bundle result = new Bundle();
        result.putSerializable("servicio", nuevoServicio);
        getParentFragmentManager().setFragmentResult("nuevoServicioRequest", result);
        closeFragment();
    }
}
