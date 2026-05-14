package com.example.control_material.inventory;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.R;

import java.util.List;

public class InvView extends AppCompatActivity implements InventarioAdapter.OnEliminarListener {

    private RecyclerView recyclerView;
    private InventarioAdapter adapter;
    private BaseDatosSQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inv_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.invViewCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new BaseDatosSQLite(this);
        cargarInventario();
    }

    private void cargarInventario() {
        List<InventarioModelo> lista = db.obtenerInventario();
        adapter = new InventarioAdapter(lista, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEliminar(InventarioModelo material) {
        new AlertDialog.Builder(this)
            .setTitle("Eliminar material")
            .setMessage("¿Está seguro de eliminar \"" + material.getNombreMaterial() + "\"?")
            .setPositiveButton("Eliminar", (dialog, which) -> {
                db.eliminarMaterial(material.getMaterialId());
                Toast.makeText(this, "Material eliminado", Toast.LENGTH_SHORT).show();
                cargarInventario();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}
