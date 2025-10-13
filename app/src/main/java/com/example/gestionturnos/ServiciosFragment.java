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

public class ServiciosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ServicioAdapter adapter;
    private List<Servicio> listaServicios;

    public ServiciosFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_servicios, container, false);

        recyclerView = view.findViewById(R.id.rvServicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaServicios = new ArrayList<>();
        cargarServiciosEjemplo();

        adapter = new ServicioAdapter(listaServicios);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddServicio);
        fab.setOnClickListener(v -> {
            // Por ahora solo agregamos un ejemplo al presionar el botón
            listaServicios.add(new Servicio("Nuevo Servicio", "30min", "$18000"));
            adapter.notifyItemInserted(listaServicios.size() - 1);
        });

        return view;
    }

    private void cargarServiciosEjemplo() {
        listaServicios.add(new Servicio("Corte", "30min", "$34000"));
        listaServicios.add(new Servicio("Capping", "60min", "$60000"));
        listaServicios.add(new Servicio("Tratamiento Capilar", "45min", "$46000"));
    }
}