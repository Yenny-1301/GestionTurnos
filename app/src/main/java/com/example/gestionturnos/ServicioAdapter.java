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
    private static ServicioAdapter.OnEditClickListener onEditClickListener;

    public interface OnEditClickListener {
        void onEditClick(Servicio servicio);
    }
    public ServicioAdapter(List<Servicio> servicios) { this.servicios = servicios; }

    public void setOnEditClickListener(ServicioAdapter.OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent,false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);

        holder.txtServicio.setText(servicio.getNombreServicio());
        holder.txtMinuto.setText(servicio.getMinutos());
        holder.txtPrecio.setText(servicio.getPrecio());

        holder.btnEditar.setOnClickListener(v -> {
            if (onEditClickListener != null && position != RecyclerView.NO_POSITION) {
                onEditClickListener.onEditClick(servicios.get(position));
            }
        });
    }

    @Override
    public int getItemCount() { return servicios.size(); }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView txtServicio, txtMinuto, txtPrecio;
        MaterialButton btnEditar;

        public ServicioViewHolder (@NonNull View itemView){
            super(itemView);
            txtServicio = itemView.findViewById(R.id.NombreServicio);
            txtMinuto = itemView.findViewById(R.id.detalleServicioMinutos);
            txtPrecio = itemView.findViewById(R.id.detalleServicioPrecio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}
