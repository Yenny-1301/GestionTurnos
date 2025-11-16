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
import android.widget.Toast;

import com.example.gestionturnos.data.entities.TurnoEntity;
import com.example.gestionturnos.data.repository.ServicioRepository;
import com.example.gestionturnos.data.repository.TurnoRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TurnoFormFragment extends Fragment {

    private TextInputEditText etNombre, etApellido, etTelefono, etFecha, etHora, etNotas;
    private AutoCompleteTextView actvServicio;
    private MaterialTextView etTitulo;
    private MaterialButton btnGuardar, btnCancelar;
    private Turno turnoOriginal;
    private TurnoEntity turnoEntity; // AGREGADO - Para edición
    private TurnoRepository turnoRepository;
    private ServicioRepository servicioRepository;
    private ExecutorService executorService;

    private int usuarioId;
    private Map<String, Integer> serviciosMap;
    private List<Servicio> listaServicios;

    public static final String REQUEST_KEY_NUEVO = "nuevoTurnoRequest";
    public static final String REQUEST_KEY_EDITADO = "turnoEditadoRequest";

    public TurnoFormFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        turnoRepository = new TurnoRepository(requireContext());
        servicioRepository = new ServicioRepository(requireContext());
        executorService = Executors.newSingleThreadExecutor();
        serviciosMap = new HashMap<>();

        usuarioId = SessionManager.obtenerUsuarioActivo(requireContext());

        if (usuarioId == -1) {
            Toast.makeText(requireContext(), "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            closeFragment();
        }
    }

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

        etFecha.setOnClickListener(v -> mostrarSelectorFecha());
        etFecha.setFocusable(false);
        etFecha.setClickable(true);

        etHora.setOnClickListener(v -> mostrarSelectorHora());
        etHora.setFocusable(false);
        etHora.setClickable(true);

        cargarServicios();

        btnGuardar.setOnClickListener(v -> guardarTurno());
        btnCancelar.setOnClickListener(v -> closeFragment());

        return view;
    }

    private void cargarServicios() {
        executorService.execute(() -> {
            try {
                listaServicios = servicioRepository.obtenerPorUsuario(usuarioId);

                requireActivity().runOnUiThread(() -> {
                    if (listaServicios.isEmpty()) {
                        Toast.makeText(requireContext(), "No hay servicios disponibles. Agregue servicios primero.", Toast.LENGTH_LONG).show();
                        actvServicio.setEnabled(false);
                        btnGuardar.setEnabled(false);
                    } else {
                        configurarServicios();
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Error al cargar servicios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void configurarServicios() {
        String[] nombresServicios = new String[listaServicios.size()];

        for (int i = 0; i < listaServicios.size(); i++) {
            Servicio servicio = listaServicios.get(i);
            String nombreCompleto = servicio.getNombreServicio() + " - $" + servicio.getPrecio();
            nombresServicios[i] = nombreCompleto;
            serviciosMap.put(servicio.getNombreServicio(), servicio.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, nombresServicios);
        actvServicio.setAdapter(adapter);
    }

    private void mostrarSelectorFecha() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(selection));
            etFecha.setText(formattedDate);
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void mostrarSelectorHora() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hora = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(java.util.Calendar.MINUTE);

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hora)
                .setMinute(minutos)
                .setTitleText("Seleccionar hora")
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            int selectedHour = timePicker.getHour();
            int selectedMinute = timePicker.getMinute();

            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
            etHora.setText(formattedTime);
        });

        timePicker.show(getParentFragmentManager(), "TIME_PICKER");
    }

    private void closeFragment() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else {
            requireActivity().finish();
        }
    }

    private void guardarTurno() {
        if (!validarCampos()) {
            return;
        }

        String servicioTexto = actvServicio.getText().toString().trim();
        String nombreServicio = servicioTexto.split(" - \\$")[0];

        Integer servicioId = serviciosMap.get(nombreServicio);

        if (servicioId == null) {
            Toast.makeText(requireContext(), "Servicio no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Turno turno = new Turno();
        turno.setNombreCliente(etNombre.getText().toString().trim());
        turno.setApellidoCliente(etApellido.getText().toString().trim());
        turno.setContacto(etTelefono.getText().toString().trim());
        turno.setServicio(nombreServicio);
        turno.setFecha(etFecha.getText().toString().trim());
        turno.setHora(etHora.getText().toString().trim());
        turno.setComentarios(etNotas.getText().toString().trim());
        turno.setServicioId(servicioId);

        // Si estamos editando, mantener el estadoId original
        if (turnoEntity != null) {
            turno.setEstadoId(turnoEntity.estadoId);
            turno.setId(turnoEntity.id);
        }

        executorService.execute(() -> {
            try {
                if (turnoEntity != null) {
                    // EDITAR turno existente
                    turnoRepository.actualizarTurnoCompleto(turnoEntity.id, turno);
                } else {
                    // CREAR nuevo turno
                    turnoRepository.insertarTurno(usuarioId, turno);
                }

                requireActivity().runOnUiThread(() -> {
                    String mensaje = turnoEntity != null ? "Turno actualizado correctamente" : "Turno guardado correctamente";
                    Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();

                    Bundle result = new Bundle();
                    result.putSerializable("turno", turno);
                    String requestKey = turnoEntity != null ? REQUEST_KEY_EDITADO : REQUEST_KEY_NUEVO;
                    getParentFragmentManager().setFragmentResult(requestKey, result);

                    closeFragment();
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private boolean validarCampos() {
        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError("Campo requerido");
            etNombre.requestFocus();
            return false;
        }

        if (etApellido.getText().toString().trim().isEmpty()) {
            etApellido.setError("Campo requerido");
            etApellido.requestFocus();
            return false;
        }

        if (etTelefono.getText().toString().trim().isEmpty()) {
            etTelefono.setError("Campo requerido");
            etTelefono.requestFocus();
            return false;
        }

        if (actvServicio.getText().toString().trim().isEmpty()) {
            actvServicio.setError("Seleccione un servicio");
            actvServicio.requestFocus();
            return false;
        }

        if (etFecha.getText().toString().trim().isEmpty()) {
            etFecha.setError("Campo requerido");
            etFecha.requestFocus();
            return false;
        }

        if (etHora.getText().toString().trim().isEmpty()) {
            etHora.setError("Campo requerido");
            etHora.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            // Verificar si viene un TurnoEntity (desde la BD) o un Turno (modelo viejo)
            turnoOriginal = (Turno) getArguments().getSerializable("turno");
            int turnoId = getArguments().getInt("turnoId", -1);

            // Si viene un turnoId, cargar desde la BD
            if (turnoId != -1) {
                cargarTurnoDesdeBaseDatos(turnoId);
            } else if (turnoOriginal != null) {
                // Modo legacy con objeto Turno
                etTitulo.setText("Editar Turno");
                etNombre.setText(turnoOriginal.getNombreCliente());
                etApellido.setText(turnoOriginal.getApellidoCliente());
                etTelefono.setText(turnoOriginal.getContacto());
                actvServicio.setText(turnoOriginal.getServicio(), false);
                etFecha.setText(turnoOriginal.getFecha());
                etHora.setText(turnoOriginal.getHora());
                etNotas.setText(turnoOriginal.getComentarios());
            } else {
                etTitulo.setText("Nuevo Turno");
            }
        } else {
            etTitulo.setText("Nuevo Turno");
        }
    }

    /**
     * Carga un turno desde la base de datos para editar
     */
    private void cargarTurnoDesdeBaseDatos(int turnoId) {
        executorService.execute(() -> {
            try {
                turnoEntity = turnoRepository.obtenerTurnoPorId(turnoId);

                if (turnoEntity != null) {
                    requireActivity().runOnUiThread(() -> {
                        etTitulo.setText("Editar Turno");
                        etNombre.setText(turnoEntity.nombreCliente);
                        etApellido.setText(turnoEntity.apellidoCliente);
                        etTelefono.setText(turnoEntity.contacto);

                        // Extraer fecha y hora del fechaTurno concatenado
                        String[] fechaHora = separarFechaHora(turnoEntity.fechaTurno);
                        etFecha.setText(fechaHora[0]);
                        etHora.setText(fechaHora[1]);

                        etNotas.setText(turnoEntity.comentarios);

                        // Buscar y seleccionar el servicio correspondiente
                        for (Servicio servicio : listaServicios) {
                            if (servicio.getId() == turnoEntity.servicioId) {
                                actvServicio.setText(servicio.getNombreServicio() + " - $" + servicio.getPrecio(), false);
                                break;
                            }
                        }
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Error al cargar turno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    closeFragment();
                });
            }
        });
    }

    /**
     * Separa fechaTurno "yyyy-MM-dd HH:mm" en ["dd/MM/yyyy", "HH:mm"]
     */
    private String[] separarFechaHora(String fechaTurno) {
        String[] resultado = new String[2];

        try {
            String[] partes = fechaTurno.split(" ");
            if (partes.length >= 2) {
                // Convertir yyyy-MM-dd a dd/MM/yyyy
                String[] fechaParts = partes[0].split("-");
                if (fechaParts.length == 3) {
                    resultado[0] = fechaParts[2] + "/" + fechaParts[1] + "/" + fechaParts[0];
                } else {
                    resultado[0] = partes[0];
                }

                resultado[1] = partes[1];
            } else {
                resultado[0] = "";
                resultado[1] = "";
            }
        } catch (Exception e) {
            resultado[0] = "";
            resultado[1] = "";
        }

        return resultado;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}