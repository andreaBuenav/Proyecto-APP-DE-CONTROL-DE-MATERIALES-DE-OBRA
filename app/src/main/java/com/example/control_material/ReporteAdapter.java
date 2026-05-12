package com.example.control_material;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReporteAdapter
        extends RecyclerView.Adapter<ReporteAdapter.ViewHolder> {

    private List<ReporteModelo> listaReportes;

    public ReporteAdapter(List<ReporteModelo> listaReportes) {

        this.listaReportes = listaReportes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        ReporteModelo reporte = listaReportes.get(position);

        holder.txtTitulo.setText(reporte.getTitulo());
        holder.txtDescripcion.setText(reporte.getDescripcion());
        holder.txtDetalle.setText(reporte.getDetalle());
    }

    @Override
    public int getItemCount() {

        return listaReportes.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTitulo;
        TextView txtDescripcion;
        TextView txtDetalle;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtTitulo =
                    itemView.findViewById(R.id.txtTitulo);

            txtDescripcion =
                    itemView.findViewById(R.id.txtDescripcion);

            txtDetalle =
                    itemView.findViewById(R.id.txtDetalle);
        }
    }
}