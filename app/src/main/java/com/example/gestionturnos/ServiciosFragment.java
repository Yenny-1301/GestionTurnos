package com.example.gestionturnos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.gestionturnos.data.repository.ServicioRepository;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ServiciosFragment extends Fragment implements ServicioAdapter.OnEditClickListener{

    public interface NavigationHost { // Debe ser public
        void navigateTo(Fragment fragment, boolean addToBackStack);
    }
    private RecyclerView recyclerView;
    private ServicioAdapter adapter;
    private List<Servicio> listaServicios = new ArrayList<>();

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

        ServicioRepository repo = new ServicioRepository(requireContext());
        listaServicios.clear();
        listaServicios.addAll(repo.obtenerPorUsuario(SessionManager.obtenerUsuarioActivo(requireContext())));

        recyclerView = view.findViewById(R.id.rvServicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ServicioAdapter(listaServicios);
        recyclerView.setAdapter(adapter);
        adapter.setOnEditClickListener(this);

        adapter.setOnDeleteClickListener((servicio, position) -> {
            mostrarDialogEliminarServicio(servicio, position, repo);
        });
        /*adapter.setOnDeleteClickListener((servicio, position) -> {
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_eliminar_servicio, (ViewGroup) getView(), false);

            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setView(dialogView)
                    .create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
            MaterialButton btnSi = dialogView.findViewById(R.id.btnSiEliminar);
            MaterialButton btnNo = dialogView.findViewById(R.id.btnNoVolver);
            MaterialButton btnCerrar = dialogView.findViewById(R.id.btnCerrar);

            btnSi.setOnClickListener(v -> {
                listaServicios.remove(position);
                repo.eliminarServicio(servicio.getId());

                adapter.notifyItemRemoved(position);
                dialog.dismiss();
            });

            btnNo.setOnClickListener(v -> {
                dialog.dismiss();
            });

            btnCerrar.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        });*/

        final String REQUEST_KEY_NUEVO = ServicioNuevoFragment.REQUEST_KEY_NUEVO;
        final String REQUEST_KEY_EDITADO = ServicioNuevoFragment.REQUEST_KEY_EDITADO;

        FloatingActionButton fab = view.findViewById(R.id.fabAddServicio);

        fab.setOnClickListener(v -> {
            ((NavigationHost) requireActivity()).navigateTo(
                    new ServicioNuevoFragment(),
                    true
            );
        });

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_NUEVO, getViewLifecycleOwner(),
                (requestKey, result) -> {
                        Servicio nuevoServicio = (Servicio) result.getSerializable("servicio");
                        if (nuevoServicio != null) {
                            listaServicios.add(nuevoServicio);
                            adapter.notifyItemInserted(listaServicios.size() - 1);
                        }
                });
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_EDITADO, getViewLifecycleOwner(),
                (requestKey, result) -> {
                    Servicio servicioEditado = (Servicio) result.getSerializable("servicio");
                    if (servicioEditado == null) return;
                    int position = listaServicios.indexOf(servicioEditado);

                    if (position != -1) {
                        listaServicios.set(position, servicioEditado);
                        adapter.notifyItemChanged(position);
                    }
                });
        return view;
    }
    @Override
    public void onEditClick(Servicio servicio) {
        ServicioNuevoFragment formFragment = new ServicioNuevoFragment();
        Bundle args = new Bundle();
        args.putSerializable("servicio", servicio);
        formFragment.setArguments(args);

        ((NavigationHost) requireActivity()).navigateTo(
                formFragment,
                true
        );
    }

    private void mostrarDialogEliminarServicio(Servicio servicio, int position, ServicioRepository repo) {
        //misma logica que en cerrar sesion
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_eliminar_servicio);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        MaterialButton btnSi = dialog.findViewById(R.id.btnSiEliminar);
        MaterialButton btnNo = dialog.findViewById(R.id.btnNoVolver);
        MaterialButton btnCerrar = dialog.findViewById(R.id.btnCerrar);

        btnCerrar.setOnClickListener(v -> dialog.dismiss());
        btnNo.setOnClickListener(v -> dialog.dismiss());

        btnSi.setOnClickListener(v -> {
            dialog.dismiss();

            // Eliminar del repositorio
            repo.eliminarServicio(servicio.getId());

            // Eliminar de la lista y actualizar el adapter
            listaServicios.remove(position);
            adapter.notifyItemRemoved(position);
        });

        dialog.show();
    }
}