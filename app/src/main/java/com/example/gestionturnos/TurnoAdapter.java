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
public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder>  {
    private List<Turno> turnos;
    private static OnEditClickListener onEditClickListener;
    private OnStatusChangeListener onStatusChangeListener;

    public interface OnStatusChangeListener {
        void onStatusChange(Turno turno, String nuevoEstado);
    }
    public interface OnEditClickListener {
        void onEditClick(Turno turno);
    }
    public TurnoAdapter(List<Turno> turnos) {
        this.turnos = turnos;
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }
    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.onStatusChangeListener = listener;
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
        Context context = holder.itemView.getContext();

        String nombreCliente = turno.getNombreCliente() + " " + turno.getApellidoCliente();
        String fechaHora = turno.getFecha() + " - " + turno.getHora();

        holder.txtCliente.setText(nombreCliente);
        holder.txtFecha.setText(fechaHora);
        holder.txtContacto.setText(turno.getContacto());
        holder.txtServicio.setText(turno.getServicio());
        holder.chipEstado.setText(turno.getEstado());

        int colorTexto = turno.getEstadoColorResource(context);
        holder.chipEstado.setTextColor(colorTexto);
        holder.chipEstado.setChipStrokeColorResource(R.color.azul);

        holder.btnEditar.setOnClickListener(v -> {
            if (onEditClickListener != null && position != RecyclerView.NO_POSITION) {
                onEditClickListener.onEditClick(turnos.get(position));
            }
        });

        holder.btnConfirmar.setOnClickListener(v -> {
            if (onStatusChangeListener != null) {
                onStatusChangeListener.onStatusChange(turno, Turno.ESTADO_CONFIRMADO);
            }
        });

        holder.btnCancelar.setOnClickListener(v -> {
            if (onStatusChangeListener != null) {
                onStatusChangeListener.onStatusChange(turno, Turno.ESTADO_CANCELADO);
            }
        });
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }
    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente,txtFecha, txtContacto, txtServicio;
        MaterialButton btnEditar, btnConfirmar,btnCancelar;
        Chip chipEstado;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCliente = itemView.findViewById(R.id.tvNombreCliente);
            txtFecha = itemView.findViewById(R.id.tvFecha);
            txtContacto = itemView.findViewById(R.id.tvContacto);
            txtServicio = itemView.findViewById(R.id.tvNombreServicio);
            chipEstado = itemView.findViewById(R.id.chipEstado);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnConfirmar = itemView.findViewById(R.id.btnConfirmar);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }
    }
}
