package com.example.control_material.catalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_material.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder> {

    public interface OnCatalogoActionListener {
        void onEditar(MaterialModelo material);
        void onEliminar(MaterialModelo material);
    }

    private List<MaterialModelo> lista;
    private List<MaterialModelo> listaCompleta;
    private final OnCatalogoActionListener listener;

    public CatalogoAdapter(List<MaterialModelo> lista, OnCatalogoActionListener listener) {
        this.lista = lista;
        this.listaCompleta = new ArrayList<>(lista);
        this.listener = listener;
    }

    public void setLista(List<MaterialModelo> lista) {
        this.lista = lista;
        this.listaCompleta = new ArrayList<>(lista);
        notifyDataSetChanged();
    }

    public void filtrar(String texto) {
        lista.clear();
        if (texto == null || texto.trim().isEmpty()) {
            lista.addAll(listaCompleta);
        } else {
            String q = texto.trim().toLowerCase();
            for (MaterialModelo m : listaCompleta) {
                if (m.getNombre().toLowerCase().contains(q)) {
                    lista.add(m);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_catalogo_card, parent, false);
        return new CatalogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
        MaterialModelo material = lista.get(position);
        holder.tvNombre.setText(material.getNombre());
        holder.tvCategoria.setText(material.getCategoria() != null ? material.getCategoria() : "—");
        holder.tvUnidad.setText(material.getUnidadMedida() != null ? material.getUnidadMedida() : "—");

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(material));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(material));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class CatalogoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvCategoria;
        TextView tvUnidad;
        MaterialButton btnEditar;
        MaterialButton btnEliminar;

        CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.catalogoItemNombre);
            tvCategoria = itemView.findViewById(R.id.catalogoItemCategoria);
            tvUnidad = itemView.findViewById(R.id.catalogoItemUnidad);
            btnEditar = itemView.findViewById(R.id.catalogoBtnEditar);
            btnEliminar = itemView.findViewById(R.id.catalogoBtnEliminar);
        }
    }
}
