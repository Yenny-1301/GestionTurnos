package com.example.gestionturnos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TurnosFragment extends Fragment {

    private RecyclerView recyclerView;
    private TurnoAdapter adapter;
    private List<Turno> listaTurnos;

    public TurnosFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_turnos, container, false);

        recyclerView = view.findViewById(R.id.rvTurnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaTurnos = new ArrayList<>();
        cargarTurnosEjemplo();

        adapter = new TurnoAdapter(listaTurnos);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddTurno);
        fab.setOnClickListener(v -> {
            // Por ahora solo agregamos un ejemplo al presionar el botón
            listaTurnos.add(new Turno("Nuevo cliente", "2025-10-12", "09:30","1123884217", "capping"));
            adapter.notifyItemInserted(listaTurnos.size() - 1);
        });

        return view;
    }

    private void cargarTurnosEjemplo() {
        listaTurnos.add(new Turno("Juan Pérez", "2025-10-11", "10:00","1123884217", "capping"));
        listaTurnos.add(new Turno("María Gómez", "2025-10-11", "11:30","1123884217", "capping"));
        listaTurnos.add(new Turno("Carlos Díaz", "2025-10-12", "14:00","1123884217", "capping"));
    }
}