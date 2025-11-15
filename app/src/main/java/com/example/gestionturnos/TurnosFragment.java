package com.example.gestionturnos;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TurnosFragment extends Fragment implements TurnoAdapter.OnEditClickListener, TurnoAdapter.OnStatusChangeListener {

    public interface NavigationHost {
        void navigateTo(Fragment fragment, boolean addToBackStack);
    }

    private RecyclerView recyclerView;
    private TurnoAdapter adapter;
    private List<Turno> listaTurnos;
    private List<Turno> turnosFiltrados;
    private String estadoSeleccionado = "Pendiente"; // Estado inicial
    private View rootView;

    // Referencias a los cards y textos
    private MaterialCardView cardPendiente, cardConfirmado, cardCancelado;
    private TextView tvPendiente, tvConfirmado, tvCancelado;

    public TurnosFragment() {
        if (listaTurnos == null) {
            listaTurnos = new ArrayList<>();
            turnosFiltrados = new ArrayList<>();
        }
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
        View estadosView = LayoutInflater.from(requireContext()).inflate(R.layout.item_estado, container, false);

        // --- Filtro de fechas ---
        RecyclerView rvFechas = rootView.findViewById(R.id.rvFechas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFechas.setLayoutManager(layoutManager);

        List<LocalDate> fechas = generarFechas(10, 10);

        FechaAdapter fechaAdapter = new FechaAdapter(fechas, (pos, fecha) ->
                Toast.makeText(requireContext(), "Seleccionaste: " + fecha.toString(), Toast.LENGTH_SHORT).show());
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

        // --- Botones de estado ---
        inicializarFiltrosEstado();

        // --- Lista de turnos ---
        recyclerView = rootView.findViewById(R.id.rvTurnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TurnoAdapter(turnosFiltrados);
        recyclerView.setAdapter(adapter);
        adapter.setOnEditClickListener(this);
        adapter.setOnStatusChangeListener(this);

        // Aplicar filtro inicial
        filtrarPorEstado(estadoSeleccionado);

        // --- Botón flotante ---
        FloatingActionButton fab = rootView.findViewById(R.id.fabAddTurno);
        fab.setOnClickListener(v -> ((NavigationHost) requireActivity()).navigateTo(
                new TurnoFormFragment(), true));

        // --- Listeners de resultados ---
        final String REQUEST_KEY_NUEVO = TurnoFormFragment.REQUEST_KEY_NUEVO;
        final String REQUEST_KEY_EDITADO = TurnoFormFragment.REQUEST_KEY_EDITADO;

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_NUEVO, this,
                (requestKey, result) -> {
                    Turno nuevoTurno = (Turno) result.getSerializable("turno");
                    if (nuevoTurno != null) {
                        listaTurnos.add(nuevoTurno);
                        filtrarPorEstado(estadoSeleccionado);
                    }
                });

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_EDITADO, this,
                (requestKey, result) -> {
                    Turno turnoEditado = (Turno) result.getSerializable("turno");
                    if (turnoEditado == null) return;
                    int position = listaTurnos.indexOf(turnoEditado);
                    if (position != -1) {
                        listaTurnos.set(position, turnoEditado);
                        filtrarPorEstado(estadoSeleccionado);
                    }
                });

        return rootView;
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

        // Actualizar colores iniciales
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

        // Pendiente
        boolean isPendienteSeleccionado = estadoSeleccionado.equals("Pendiente");
        cardPendiente.setCardBackgroundColor(isPendienteSeleccionado ? colorPendiente : colorInactivo);
        tvPendiente.setTextColor(isPendienteSeleccionado ? colorTextoActivo : colorTextoInactivo);

        // Confirmado
        boolean isConfirmadoSeleccionado = estadoSeleccionado.equals("Confirmado");
        cardConfirmado.setCardBackgroundColor(isConfirmadoSeleccionado ? colorConfirmado : colorInactivo);
        tvConfirmado.setTextColor(isConfirmadoSeleccionado ? colorTextoActivo : colorTextoInactivo);

        // Cancelado
        boolean isCanceladoSeleccionado = estadoSeleccionado.equals("Cancelado");
        cardCancelado.setCardBackgroundColor(isCanceladoSeleccionado ? colorCancelado : colorInactivo);
        tvCancelado.setTextColor(isCanceladoSeleccionado ? colorTextoActivo : colorTextoInactivo);
    }

    private void filtrarPorEstado(String estado) {
        turnosFiltrados.clear();

        for (Turno turno : listaTurnos) {
            if (turno.getEstado().equals(estado)) {
                turnosFiltrados.add(turno);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditClick(Turno turno) {
        TurnoFormFragment formFragment = new TurnoFormFragment();
        Bundle args = new Bundle();
        args.putSerializable("turno", turno);
        formFragment.setArguments(args);
        ((NavigationHost) requireActivity()).navigateTo(formFragment, true);
    }

    @Override
    public void onStatusChange(Turno turno, String nuevoEstado) {
        int position = listaTurnos.indexOf(turno);
        if (position != -1) {
            Turno turnoActualizar = listaTurnos.get(position);
            turnoActualizar.setEstado(nuevoEstado);
            filtrarPorEstado(estadoSeleccionado);
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
}