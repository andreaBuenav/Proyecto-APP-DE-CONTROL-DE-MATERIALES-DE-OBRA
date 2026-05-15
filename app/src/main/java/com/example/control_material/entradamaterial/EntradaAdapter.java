package com.example.control_material;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntradaAdapter extends RecyclerView.Adapter<EntradaAdapter.ViewHolder> {

    ArrayList<EntradaModel> listaEntradas;

    public EntradaAdapter(ArrayList<EntradaModel> listaEntradas) {

        this.listaEntradas = listaEntradas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_entrada,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        EntradaModel entrada = listaEntradas.get(position);

        holder.tvFecha.setText(entrada.getFecha());

        holder.tvMaterial.setText(entrada.getMaterial());

        holder.tvCantidad.setText(
                "Cantidad: " + entrada.getCantidad()
        );

        holder.tvPrecio.setText(
                "Precio: $" + entrada.getPrecio()
        );
    }

    @Override
    public int getItemCount() {

        return listaEntradas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFecha;

        TextView tvMaterial;

        TextView tvCantidad;

        TextView tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFecha = itemView.findViewById(R.id.tvFecha);

            tvMaterial = itemView.findViewById(R.id.tvMaterial);

            tvCantidad = itemView.findViewById(R.id.tvCantidad);

            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}