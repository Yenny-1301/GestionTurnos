package com.example.gestionturnos;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {
    private List<Servicio> servicios;
    private OnEditClickListener onEditClickListener;  // ← QUITAR static
    private OnDeleteClickListener onDeleteClickListener;  // ← QUITAR static

    public ServicioAdapter(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public interface OnEditClickListener {
        void onEditClick(Servicio servicio);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Servicio servicio, int position);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {  // ← Quitar ServicioAdapter.
        this.onEditClickListener = listener;  // ← Agregar this.
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;  // ← Agregar this.
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);

        holder.txtServicio.setText(servicio.getNombreServicio());
        holder.txtMinuto.setText(servicio.getMinutos());
        holder.txtPrecio.setText(servicio.getPrecio());

        holder.btnEditar.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(servicio);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(servicio, holder.getAdapterPosition());  // ← Usar getAdapterPosition() en lugar de position
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    // actualizar luego de eliminar
    public void eliminarServicio(int position) {
        if (position >= 0 && position < servicios.size()) {
            servicios.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, servicios.size());
        }
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView txtServicio, txtMinuto, txtPrecio;
        MaterialButton btnEditar, btnEliminar;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtServicio = itemView.findViewById(R.id.NombreServicio);
            txtMinuto = itemView.findViewById(R.id.detalleServicioMinutos);
            txtPrecio = itemView.findViewById(R.id.detalleServicioPrecio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}