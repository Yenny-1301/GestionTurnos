package com.example.gestionturnos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ServiciosFragment extends Fragment {

    public interface NavigationHost { // Debe ser public
        void navigateTo(Fragment fragment, boolean addToBackStack);
    }
    private RecyclerView recyclerView;
    private ServicioAdapter adapter;
    private List<Servicio> listaServicios;

    public ServiciosFragment() {
        if(listaServicios == null) {
            listaServicios = new ArrayList<>();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof NavigationHost)) {
            throw new RuntimeException(context.toString()
                    + " debe implementar NavigationHost");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_servicios, container, false);

        recyclerView = view.findViewById(R.id.rvServicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ServicioAdapter(listaServicios);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddServicio);

        fab.setOnClickListener(v -> {
            ((NavigationHost) requireActivity()).navigateTo(
                    new ServicioNuevoFragment(),
                    true
            );
        });

        getParentFragmentManager().setFragmentResultListener("nuevoServicioRequest", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Servicio nuevoServicio = (Servicio) result.getSerializable("servicio");
                        if (nuevoServicio != null) {
                            listaServicios.add(nuevoServicio);
                            adapter.notifyItemInserted(listaServicios.size() - 1);
                        }
                    }
                });

        return view;
    }
}