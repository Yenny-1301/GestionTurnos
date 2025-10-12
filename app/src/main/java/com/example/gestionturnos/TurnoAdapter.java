package com.example.gestionturnos;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder>  {
    private List<Turno> turnos;

    public TurnoAdapter(List<Turno> turnos) {
        this.turnos = turnos;
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        Turno turno = turnos.get(position);
        holder.txtCliente.setText(turno.getNombreCliente());
        holder.txtFecha.setText(turno.getFecha());
        holder.txtContacto.setText(turno.getContacto());
        holder.txtServicio.setText(turno.getServicio());
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente, txtFecha, txtContacto, txtServicio;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCliente = itemView.findViewById(R.id.tvNombreCliente);
            txtFecha = itemView.findViewById(R.id.tvFecha);
            txtContacto = itemView.findViewById(R.id.tvContacto);
            txtServicio = itemView.findViewById(R.id.tvNombreServicio);
        }
    }
}
