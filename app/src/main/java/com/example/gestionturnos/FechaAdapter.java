package com.example.gestionturnos;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class FechaAdapter extends RecyclerView.Adapter<FechaAdapter.FechaViewHolder> {

    private List<LocalDate> fechas;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnFechaClickListener listener;

    public interface OnFechaClickListener {
        void onFechaClick(int position, LocalDate fecha);
    }

    public FechaAdapter(List<LocalDate> fechas, OnFechaClickListener listener) {
        this.fechas = fechas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FechaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fechas, parent, false);
        return new FechaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FechaViewHolder holder, int position) {
        LocalDate fecha = fechas.get(position);
        Locale locale = new Locale("es", "AR");

        String diaSemana = fecha.getDayOfWeek().getDisplayName(TextStyle.SHORT, locale).toUpperCase();
        String diaNumero = String.valueOf(fecha.getDayOfMonth());

        holder.tvDayName.setText(diaSemana);
        holder.tvDayNumber.setText(diaNumero);

        Context context = holder.itemView.getContext();
        TypedValue typedValue = new TypedValue();
        boolean isSelected = position == selectedPosition;

        // Color de fondo
        int colorFondo;
        if (isSelected) {
            // Siempre usar primaryViolet cuando está seleccionado (independiente del tema)
            colorFondo = ContextCompat.getColor(context, R.color.primaryViolet);
        } else {
            colorFondo = ContextCompat.getColor(context, R.color.outlineSec);
        }

        // Color de texto
        int colorTexto;
        if (isSelected) {
            // Texto blanco cuando está seleccionado
            context.getTheme().resolveAttribute(
                    com.google.android.material.R.attr.colorOnPrimary,
                    typedValue,
                    true
            );
            colorTexto = typedValue.data;
        } else {
            // Texto oscuro cuando NO está seleccionado
            context.getTheme().resolveAttribute(
                    com.google.android.material.R.attr.colorOnSurface,
                    typedValue,
                    true
            );
            colorTexto = typedValue.data;
        }

        // Aplicar colores
        holder.cardView.setCardBackgroundColor(colorFondo);
        holder.tvDayName.setTextColor(colorTexto);
        holder.tvDayNumber.setTextColor(colorTexto);

        // Efecto de escala (más grande si está seleccionado)
        holder.cardView.setScaleX(isSelected ? 1.1f : 1f);
        holder.cardView.setScaleY(isSelected ? 1.1f : 1f);

        holder.itemView.setOnClickListener(v -> {
            int prev = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(prev);
            notifyItemChanged(position);
            if (listener != null) listener.onFechaClick(position, fecha);
        });
    }

    @Override
    public int getItemCount() {
        return fechas.size();
    }

    static class FechaViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayName, tvDayNumber;
        CardView cardView;

        FechaViewHolder(View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tvDayName);
            tvDayNumber = itemView.findViewById(R.id.tvDayNumber);
            cardView = itemView.findViewById(R.id.cardDate);
        }
    }

    public void seleccionarPorFecha(LocalDate fechaActual) {
        for (int i = 0; i < fechas.size(); i++) {
            if (fechas.get(i).isEqual(fechaActual)) {
                int prev = selectedPosition;
                selectedPosition = i;
                if (prev != RecyclerView.NO_POSITION) notifyItemChanged(prev);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}