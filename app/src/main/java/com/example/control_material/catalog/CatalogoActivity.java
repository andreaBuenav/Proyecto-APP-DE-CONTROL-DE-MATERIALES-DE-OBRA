package com.example.control_material.catalog;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CatalogoActivity extends AppCompatActivity
        implements CatalogoAdapter.OnCatalogoActionListener {

    private RecyclerView recyclerView;
    private CatalogoAdapter adapter;
    private BaseDatosSQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        Toolbar toolbar = findViewById(R.id.catalogoToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new BaseDatosSQLite(this);

        recyclerView = findViewById(R.id.catalogoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.catalogoFab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, CatalogoFormActivity.class);
            intent.putExtra("materialId", -1);
            startActivity(intent);
        });

        cargarCatalogo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCatalogo();
    }

    private void cargarCatalogo() {
        List<MaterialModelo> lista = db.obtenerCatalogo();
        if (adapter == null) {
            adapter = new CatalogoAdapter(lista, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setLista(lista);
        }
    }

    @Override
    public void onEditar(MaterialModelo material) {
        Intent intent = new Intent(this, CatalogoFormActivity.class);
        intent.putExtra("materialId", material.getMaterialId());
        startActivity(intent);
    }

    @Override
    public void onEliminar(MaterialModelo material) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar material")
                .setMessage("¿Desea eliminar \"" + material.getNombre() + "\"? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    db.eliminarMaterial(material.getMaterialId());
                    cargarCatalogo();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
