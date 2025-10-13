package com.example.gestionturnos;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {
    private List<Servicio> servicios;

    public ServicioAdapter(List<Servicio> servicios) { this.servicios = servicios; }

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
    }

    @Override
    public int getItemCount() { return servicios.size(); }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView txtServicio, txtMinuto, txtPrecio;

        public ServicioViewHolder (@NonNull View itemView){
            super(itemView);
            txtServicio = itemView.findViewById(R.id.NombreServicio);
            txtMinuto = itemView.findViewById(R.id.detalleServicioMinutos);
            txtPrecio = itemView.findViewById(R.id.detalleServicioPrecio);
        }
    }
}
