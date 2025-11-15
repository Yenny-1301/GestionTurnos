package com.example.gestionturnos;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

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

    public TurnosFragment() {
        if (listaTurnos == null) {
            listaTurnos = new ArrayList<>();
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

        View view = inflater.inflate(R.layout.fragment_turnos, container, false);

        // --- Filtro de fechas (debajo del título) ---
        RecyclerView rvFechas = view.findViewById(R.id.rvFechas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFechas.setLayoutManager(layoutManager);

        // Genera 10 días antes y 10 después del día actual
        List<LocalDate> fechas = generarFechas(10, 10);

        FechaAdapter fechaAdapter = new FechaAdapter(fechas, (pos, fecha) ->
                Toast.makeText(requireContext(), "Seleccionaste: " + fecha.toString(), Toast.LENGTH_SHORT).show());
        rvFechas.setAdapter(fechaAdapter);

        // ⭐ AGREGAR SNAPHELPER para centrar items automáticamente
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvFechas);

        // Selecciona y centra el día actual
        fechaAdapter.seleccionarPorFecha(LocalDate.now());

        // ⭐ SOLUCIÓN: Esperar a que el layout esté completamente medido
        rvFechas.getViewTreeObserver().addOnGlobalLayoutListener(
                new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Remover el listener para que solo se ejecute una vez
                        rvFechas.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int selectedPos = fechaAdapter.getSelectedPosition();
                        if (selectedPos != RecyclerView.NO_POSITION) {
                            // Hacer scroll primero a la posición aproximada
                            layoutManager.scrollToPosition(selectedPos);

                            // Luego ajustar el centrado exacto
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

        // --- Lista de turnos ---
        recyclerView = view.findViewById(R.id.rvTurnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TurnoAdapter(listaTurnos);
        recyclerView.setAdapter(adapter);
        adapter.setOnEditClickListener(this);
        adapter.setOnStatusChangeListener(this);

        // --- Botón flotante ---
        FloatingActionButton fab = view.findViewById(R.id.fabAddTurno);
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
                        adapter.notifyItemInserted(listaTurnos.size() - 1);
                    }
                });

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_EDITADO, this,
                (requestKey, result) -> {
                    Turno turnoEditado = (Turno) result.getSerializable("turno");
                    if (turnoEditado == null) return;
                    int position = listaTurnos.indexOf(turnoEditado);
                    if (position != -1) {
                        listaTurnos.set(position, turnoEditado);
                        adapter.notifyItemChanged(position);
                    }
                });

        return view;
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
            adapter.notifyItemChanged(position);
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

    private int buscarPosicionHoy(List<LocalDate> lista) {
        LocalDate hoy = LocalDate.now();
        for (int i = 0; i < lista.size(); i++)
            if (lista.get(i).isEqual(hoy)) return i;
        return -1;
    }
}