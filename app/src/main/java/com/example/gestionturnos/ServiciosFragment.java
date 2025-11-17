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
import android.widget.TextView;
import android.widget.Toast;

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
    private ServicioRepository repo;

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

        repo = new ServicioRepository(requireContext());

        // Inicializar lista si no existe
        if (listaServicios == null) {
            listaServicios = new ArrayList<>();
        }

        try {
            ServicioRepository repo = new ServicioRepository(requireContext());
            int usuarioId = SessionManager.obtenerUsuarioActivo(requireContext());

            List<Servicio> serviciosDb = repo.obtenerPorUsuario(usuarioId);

            listaServicios.clear();
            if (serviciosDb != null && !serviciosDb.isEmpty()) {
                listaServicios.addAll(serviciosDb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al cargar servicios: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        recyclerView = view.findViewById(R.id.rvServicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView emptyView = view.findViewById(R.id.tvEmptyServicios);

        // Validar vistas
        if (emptyView != null && recyclerView != null) {
            if (listaServicios.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        adapter = new ServicioAdapter(listaServicios);
        recyclerView.setAdapter(adapter);
        adapter.setOnEditClickListener(this);

        adapter.setOnDeleteClickListener((servicio, position) -> {
            mostrarDialogEliminarServicio(servicio, position, repo);
        });


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
                            actualizarVisibilidad();
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

            try {
                // eliminar del repositorio
                repo.eliminarServicio(servicio.getId());

                // eliminar de la lista y actualizar el adapter
                listaServicios.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, listaServicios.size());

                // actualizar visibilidad del mensaje vacío
                actualizarVisibilidad();

                Toast.makeText(requireContext(), "Servicio eliminado", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error al eliminar: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    private void actualizarVisibilidad() {
        TextView emptyView = getView() != null ? getView().findViewById(R.id.tvEmptyServicios) : null;

        if (emptyView != null && recyclerView != null) {
            if (listaServicios.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}