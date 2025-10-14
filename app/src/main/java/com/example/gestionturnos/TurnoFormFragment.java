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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TurnoFormFragment extends Fragment {

    private TextInputEditText etNombre, etApellido, etTelefono, etFecha, etHora, etNotas ;
    private AutoCompleteTextView actvServicio;

    private MaterialTextView etTitulo;
    private MaterialButton btnGuardar, btnCancelar;
    private Turno turnoOriginal;
    public static final String REQUEST_KEY_NUEVO = "nuevoTurnoRequest";
    public static final String REQUEST_KEY_EDITADO = "turnoEditadoRequest";

    public TurnoFormFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turno_form, container, false);

        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etTelefono = view.findViewById(R.id.etTelefono);
        etFecha = view.findViewById(R.id.etFecha);
        etHora = view.findViewById(R.id.etHora);
        etNotas = view.findViewById(R.id.etNotas);
        etTitulo = view.findViewById(R.id.tvTituloFormulario);
        actvServicio = view.findViewById(R.id.actvServicio);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        etFecha.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Pre-select today's date
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                String formattedDate = sdf.format(new Date(selection));
                etFecha.setText(formattedDate);
            });

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });

        configurarServicios();

        btnGuardar.setOnClickListener(v -> guardarTurno(v));
        btnCancelar.setOnClickListener(v -> closeFragment());

        return view;
    }

    private void configurarServicios() {
        String[] servicios = {"Corte simple", "Coloración", "Peinado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, servicios);
        actvServicio.setAdapter(adapter);
    }

    private void closeFragment() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else {
            requireActivity().finish();
        }
    }
    private void guardarTurno(View view) {

        final String requestKey;
        final Turno turnoParaEnviar;
        if (turnoOriginal != null) {
            requestKey = REQUEST_KEY_EDITADO;
            turnoParaEnviar = turnoOriginal;

        } else {
            requestKey = REQUEST_KEY_NUEVO;
            turnoParaEnviar = new Turno();
        }

        turnoParaEnviar.setNombreCliente(etNombre.getText().toString());
        turnoParaEnviar.setApellidoCliente(etApellido.getText().toString());
        turnoParaEnviar.setContacto(etTelefono.getText().toString());
        turnoParaEnviar.setServicio(actvServicio.getText().toString());
        turnoParaEnviar.setFecha(etFecha.getText().toString());
        turnoParaEnviar.setHora(etHora.getText().toString());
        turnoParaEnviar.setComentarios(etNotas.getText().toString());

        Turno nuevoTurno = new Turno(
                etNombre.getText().toString() ,
                etApellido.getText().toString(),
                etFecha.getText().toString(),
                etHora.getText().toString(),
                etTelefono.getText().toString(),
                actvServicio.getText().toString(),
                etNotas.getText().toString()
        );

        Bundle result = new Bundle();
        result.putSerializable("turno", turnoParaEnviar);
        getParentFragmentManager().setFragmentResult(requestKey, result);

        closeFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Turno turno = null;
        if (getArguments() != null) {
            turno = (Turno) getArguments().getSerializable("turno");
            turnoOriginal = (Turno) getArguments().getSerializable("turno");
        }

        if (turnoOriginal != null) {
            etTitulo.setText("Editar Turno");
            etNombre.setText(turno.getNombreCliente());
            etApellido.setText(turno.getApellidoCliente());
            etTelefono.setText(turno.getContacto());
            actvServicio.setText(turno.getServicio(), false);
            etFecha.setText(turno.getFecha());
            etHora.setText(turno.getHora());
            etNotas.setText(turno.getComentarios());
        } else {
            etTitulo.setText("Nuevo Turno");
        }
    }
}
