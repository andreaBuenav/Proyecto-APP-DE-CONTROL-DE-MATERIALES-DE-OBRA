package com.example.control_material.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.control_material.R;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {

    public interface OnEliminarListener {
        void onEliminar(InventarioModelo material);
    }

    private List<InventarioModelo> listaInventario;
    private Context context;
    private OnEliminarListener listener;

    public InventarioAdapter(List<InventarioModelo> listaInventario, Context context, OnEliminarListener listener) {
        this.listaInventario = listaInventario;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventarioModelo modelo = listaInventario.get(position);

        holder.tvNombre.setText(modelo.getNombreMaterial());
        holder.tvObra.setText("");
        holder.tvCantidad.setText(modelo.getStockActual() + " " + modelo.getUnidadMedida());

        if (modelo.isAlertaActiva()) {
            holder.ivAlert.setVisibility(View.VISIBLE);
        } else {
            holder.ivAlert.setVisibility(View.GONE);
        }

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onEliminar(modelo);
        });
    }

    @Override
    public int getItemCount() {
        return listaInventario.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvObra;
        TextView tvCantidad;
        ImageView ivAlert;
        MaterialButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.invItemMatValue);
            tvObra = itemView.findViewById(R.id.invViewWorkValue);
            tvCantidad = itemView.findViewById(R.id.invViewAvailableValue);
            ivAlert = itemView.findViewById(R.id.invViewAlertIcon);
            btnEliminar = itemView.findViewById(R.id.invViewBtnDelete);
        }
    }
}
