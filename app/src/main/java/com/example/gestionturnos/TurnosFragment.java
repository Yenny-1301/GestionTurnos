package com.example.gestionturnos;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gestionturnos.data.repository.ServicioRepository;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionturnos.data.entities.ServicioEntity;
import com.example.gestionturnos.data.entities.TurnoEntity;
import com.example.gestionturnos.data.repository.TurnoRepository;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TurnosFragment extends Fragment implements TurnoAdapter.OnEditClickListener, TurnoAdapter.OnStatusChangeListener {

    public interface NavigationHost {
        void navigateTo(Fragment fragment, boolean addToBackStack);
    }
    private RecyclerView recyclerView;
    private TurnoAdapter adapter;
    private List<Turno> listaTurnos;
    private List<Turno> turnosFiltrados;
    private String estadoSeleccionado = "Pendiente";
    private View rootView;
    private ServicioRepository servicioRepository;
    private LocalDate fechaSeleccionada = LocalDate.now();
    private TurnoRepository turnoRepository;
    private ExecutorService executorService;
    private int usuarioId;
    private MaterialCardView cardPendiente, cardConfirmado, cardCancelado;
    private TextView tvPendiente, tvConfirmado, tvCancelado;
    public TurnosFragment() {
        if (listaTurnos == null) {
            listaTurnos = new ArrayList<>();
            turnosFiltrados = new ArrayList<>();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicioRepository = new ServicioRepository(requireContext());
        turnoRepository = new TurnoRepository(requireContext());
        executorService = Executors.newSingleThreadExecutor();
        usuarioId = SessionManager.obtenerUsuarioActivo(requireContext());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof NavigationHost)) {
            throw new RuntimeException(context.toString() + " debe implementar NavigationHost");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_turnos, container, false);

        // componente filtro de fecha
        RecyclerView rvFechas = rootView.findViewById(R.id.rvFechas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFechas.setLayoutManager(layoutManager);

        List<LocalDate> fechas = generarFechas(10, 10);

        FechaAdapter fechaAdapter = new FechaAdapter(fechas, (pos, fecha) -> {
            fechaSeleccionada = fecha;
            filtrarPorEstadoYFecha(estadoSeleccionado, fechaSeleccionada);
        });
        rvFechas.setAdapter(fechaAdapter);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvFechas);

        fechaAdapter.seleccionarPorFecha(LocalDate.now());

        rvFechas.getViewTreeObserver().addOnGlobalLayoutListener(
                new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rvFechas.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int selectedPos = fechaAdapter.getSelectedPosition();
                        if (selectedPos != RecyclerView.NO_POSITION) {
                            layoutManager.scrollToPosition(selectedPos);

                            rvFechas.post(() -> {
                                View selectedView = layoutManager.findViewByPosition(selectedPos);
                                if (selectedView != null) {
                                    int itemWidth = selectedView.getWidth();
                                    int recyclerWidth = rvFechas.getWidth();
                                    int offset = (recyclerWidth / 2) - (itemWidth / 2);

                                    layoutManager.scrollToPositionWithOffset(selectedPos, offset);
                                }
                            });
                        }
                    }
                }
        );

        // componente filstro de estado
        inicializarFiltrosEstado();

        // --- Lista de turnos ---
        recyclerView = rootView.findViewById(R.id.rvTurnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TurnoAdapter(turnosFiltrados);
        recyclerView.setAdapter(adapter);
        adapter.setOnEditClickListener(this);
        adapter.setOnStatusChangeListener(this);

        cargarTurnosDesdeBaseDatos();

        // --- Botón flotante ---
        FloatingActionButton fab = rootView.findViewById(R.id.fabAddTurno);
        fab.setOnClickListener(v -> ((NavigationHost) requireActivity()).navigateTo(
                new TurnoFormFragment(), true));

        // --- Listeners de resultados ---
        final String REQUEST_KEY_NUEVO = TurnoFormFragment.REQUEST_KEY_NUEVO;
        final String REQUEST_KEY_EDITADO = TurnoFormFragment.REQUEST_KEY_EDITADO;

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_NUEVO, this,
                (requestKey, result) -> {
                    // Recargar turnos después de agregar uno nuevo
                    cargarTurnosDesdeBaseDatos();
                });

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_EDITADO, this,
                (requestKey, result) -> {
                    // Recargar turnos después de editar
                    cargarTurnosDesdeBaseDatos();
                });

        return rootView;
    }

    //cargar los turnos que se encuentran en la base
    private void cargarTurnosDesdeBaseDatos() {
        executorService.execute(() -> {
            try {
                List<TurnoEntity> turnosEntity = turnoRepository.obtenerTurnosPorUsuario(usuarioId);

                // Convertir TurnoEntity a Turno
                List<Turno> turnosCargados = new ArrayList<>();
                if (turnosEntity != null) {
                    for (TurnoEntity entity : turnosEntity) {
                        Turno turno = convertirEntityATurno(entity);
                        turnosCargados.add(turno);
                    }
                }

                // Verificar que el fragmento sigue activo
                if (getActivity() != null && isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        if (listaTurnos != null) {
                            listaTurnos.clear();
                            listaTurnos.addAll(turnosCargados);
                            filtrarPorEstado(estadoSeleccionado);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null && isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(),
                                "Error al cargar turnos: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }


    // convierte TurnoEntity a Turno
    private Turno convertirEntityATurno(TurnoEntity entity) {
        Turno turno = new Turno();
        turno.setId(entity.id);
        turno.setNombreCliente(entity.nombreCliente);
        turno.setApellidoCliente(entity.apellidoCliente);
        turno.setContacto(entity.contacto);
        turno.setComentarios(entity.comentarios);
        turno.setServicioId(entity.servicioId);
        turno.setEstadoId(entity.estadoId);

        String[] fechaHora = separarFechaHora(entity.fechaTurno);
        turno.setFecha(fechaHora[0]);
        turno.setHora(fechaHora[1]);

        String estadoTexto = convertirEstadoIdATexto(entity.estadoId);
        turno.setEstado(estadoTexto);

        // Obtener nombre del servicio (esto en background thread)
        String nombreServicio = obtenerNombreServicio(entity.servicioId);
        turno.setServicio(nombreServicio);

        return turno;
    }
    private String obtenerNombreServicio(int servicioId) {
        try {
            ServicioEntity servicio = servicioRepository.obtenerServicioPorId(servicioId);
            return servicio != null ? servicio.nombre : "Servicio #" + servicioId;
        } catch (Exception e) {
            e.printStackTrace();
            return "Servicio #" + servicioId;
        }
    }
    //Separa fechaTurno "yyyy-MM-dd HH:mm" en DOS componentes
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

    // convierte estadoId (int) a texto
    private String convertirEstadoIdATexto(Integer estadoId) {
        if (estadoId == null) return Turno.ESTADO_PENDIENTE;

        switch (estadoId) {
            case TurnoRepository.ESTADO_PENDIENTE:
                return Turno.ESTADO_PENDIENTE;
            case TurnoRepository.ESTADO_CONFIRMADO:
                return Turno.ESTADO_CONFIRMADO;
            case TurnoRepository.ESTADO_CANCELADO:
                return Turno.ESTADO_CANCELADO;
            default:
                return Turno.ESTADO_PENDIENTE;
        }
    }

    private void inicializarFiltrosEstado() {
        cardPendiente = rootView.findViewById(R.id.cardPendiente);
        cardConfirmado = rootView.findViewById(R.id.cardConfirmado);
        cardCancelado = rootView.findViewById(R.id.cardCancelado);

        tvPendiente = rootView.findViewById(R.id.tvEstadoPendiente);
        tvConfirmado = rootView.findViewById(R.id.tvEstadoConfirmado);
        tvCancelado = rootView.findViewById(R.id.tvEstadoCancelado);

        View.OnClickListener estadoClickListener = v -> {
            if (v == cardPendiente) {
                estadoSeleccionado = "Pendiente";
            } else if (v == cardConfirmado) {
                estadoSeleccionado = "Confirmado";
            } else if (v == cardCancelado) {
                estadoSeleccionado = "Cancelado";
            }

            actualizarColores();
            filtrarPorEstado(estadoSeleccionado);
        };

        cardPendiente.setOnClickListener(estadoClickListener);
        cardConfirmado.setOnClickListener(estadoClickListener);
        cardCancelado.setOnClickListener(estadoClickListener);

        actualizarColores();
    }

    private void actualizarColores() {
        Context context = getContext();
        if (context == null) return;

        int colorInactivo = ContextCompat.getColor(context, R.color.greySecondary);
        int colorTextoActivo = ContextCompat.getColor(context, android.R.color.white);
        int colorTextoInactivo = ContextCompat.getColor(context, R.color.gray_input_texto);

        int colorPendiente = ContextCompat.getColor(context, R.color.azul);
        int colorConfirmado = ContextCompat.getColor(context, R.color.green);
        int colorCancelado = ContextCompat.getColor(context, R.color.red);

        boolean isPendienteSeleccionado = estadoSeleccionado.equals("Pendiente");
        cardPendiente.setCardBackgroundColor(isPendienteSeleccionado ? colorPendiente : colorInactivo);
        tvPendiente.setTextColor(isPendienteSeleccionado ? colorTextoActivo : colorTextoInactivo);

        boolean isConfirmadoSeleccionado = estadoSeleccionado.equals("Confirmado");
        cardConfirmado.setCardBackgroundColor(isConfirmadoSeleccionado ? colorConfirmado : colorInactivo);
        tvConfirmado.setTextColor(isConfirmadoSeleccionado ? colorTextoActivo : colorTextoInactivo);

        boolean isCanceladoSeleccionado = estadoSeleccionado.equals("Cancelado");
        cardCancelado.setCardBackgroundColor(isCanceladoSeleccionado ? colorCancelado : colorInactivo);
        tvCancelado.setTextColor(isCanceladoSeleccionado ? colorTextoActivo : colorTextoInactivo);
    }

    private void filtrarPorEstado(String estado) {
        filtrarPorEstadoYFecha(estado, fechaSeleccionada);
    }
    @Override
    public void onEditClick(Turno turno) {
        Bundle args = new Bundle();
        args.putInt("turnoId", turno.getId()); // CORREGIDO

        TurnoFormFragment fragment = new TurnoFormFragment();
        fragment.setArguments(args);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void filtrarPorEstadoYFecha(String estado, LocalDate fecha) {
        turnosFiltrados.clear();

        // Convertir la fecha seleccionada a formato dd/MM/yyyy para comparar
        String fechaFormateada = String.format("%02d/%02d/%d",
                fecha.getDayOfMonth(),
                fecha.getMonthValue(),
                fecha.getYear());

        for (Turno turno : listaTurnos) {
            // Filtrar por estado Y por fecha
            if (turno.getEstado().equals(estado) && turno.getFecha().equals(fechaFormateada)) {
                turnosFiltrados.add(turno);
            }
        }

        adapter.notifyDataSetChanged();
    }
    @Override
    public void onStatusChange(Turno turno, String nuevoEstado) {
        executorService.execute(() -> {
            try {
                int nuevoEstadoId = convertirTextoAEstadoId(nuevoEstado);

                // Actualizar estado en la BD
                TurnoEntity entity = turnoRepository.obtenerTurnoPorId(turno.getId());
                if (entity != null) {
                    entity.estadoId = nuevoEstadoId;
                    turnoRepository.actualizarTurno(entity);

                    // Verificar que el fragmento sigue activo
                    if (getActivity() != null && isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            // Actualizar en memoria
                            int position = listaTurnos.indexOf(turno);
                            if (position != -1) {
                                Turno turnoActualizar = listaTurnos.get(position);
                                turnoActualizar.setEstado(nuevoEstado);
                                turnoActualizar.setEstadoId(nuevoEstadoId);
                                filtrarPorEstado(estadoSeleccionado);
                            }
                            Toast.makeText(requireContext(), "Estado actualizado",
                                    Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null && isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(),
                                "Error al actualizar estado: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    // convierte texto de estado a ID int
    private int convertirTextoAEstadoId(String estadoTexto) {
        switch (estadoTexto) {
            case Turno.ESTADO_CONFIRMADO:
                return TurnoRepository.ESTADO_CONFIRMADO;
            case Turno.ESTADO_CANCELADO:
                return TurnoRepository.ESTADO_CANCELADO;
            case Turno.ESTADO_PENDIENTE:
            default:
                return TurnoRepository.ESTADO_PENDIENTE;
        }
    }

    private List<LocalDate> generarFechas(int diasAntes, int diasDespues) {
        List<LocalDate> lista = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        for (int i = -diasAntes; i <= diasDespues; i++) {
            lista.add(hoy.plusDays(i));
        }
        return lista;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}