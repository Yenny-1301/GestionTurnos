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

public class TurnosFragment extends Fragment {

    public interface NavigationHost { // Debe ser public
        void navigateTo(Fragment fragment, boolean addToBackStack);
    }
    private RecyclerView recyclerView;
    private TurnoAdapter adapter;
    private List<Turno> listaTurnos;

    public TurnosFragment() {
        if(listaTurnos == null){
            listaTurnos = new ArrayList<>();
        }
    }

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

        View view = inflater.inflate(R.layout.fragment_turnos, container, false);

        recyclerView = view.findViewById(R.id.rvTurnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TurnoAdapter(listaTurnos);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddTurno);

        fab.setOnClickListener(v -> {
            ((NavigationHost) requireActivity()).navigateTo(
                    new TurnoFormFragment(),
                    true
            );
        });

        getParentFragmentManager().setFragmentResultListener("nuevoTurnoRequest", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Turno nuevoTurno = (Turno) result.getSerializable("turno");
                        if (nuevoTurno != null) {
                            listaTurnos.add(nuevoTurno);
                            adapter.notifyItemInserted(listaTurnos.size() - 1);
                        }
                    }
                });

        return view;
    }
}