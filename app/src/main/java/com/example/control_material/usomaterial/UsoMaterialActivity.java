package com.example.control_material.usomaterial;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.R;
import com.example.control_material.inventory.InventarioModelo;

import java.util.ArrayList;
import java.util.List;

public class UsoMaterialActivity extends AppCompatActivity {

    private Spinner spMaterialUso;
    private EditText etObraUso;
    private EditText etActividadUso;
    private EditText etCantidadUso;
    private EditText etObservacionUso;
    private TextView tvStockDisponible;
    private Button btnGuardarUso;

    private BaseDatosSQLite conexion;

    private List<InventarioModelo> listaMateriales;
    private ArrayList<String> nombresMateriales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uso_material);

        Toolbar toolbar = findViewById(R.id.toolbarUsoMaterial);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Uso de Material");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        conexion = new BaseDatosSQLite(this);

        spMaterialUso = findViewById(R.id.spMaterialUso);
        etObraUso = findViewById(R.id.etObraUso);
        etActividadUso = findViewById(R.id.etActividadUso);
        etCantidadUso = findViewById(R.id.etCantidadUso);
        etObservacionUso = findViewById(R.id.etObservacionUso);
        tvStockDisponible = findViewById(R.id.tvStockDisponible);
        btnGuardarUso = findViewById(R.id.btnGuardarUso);

        cargarMateriales();

        spMaterialUso.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                mostrarStockDisponible(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        btnGuardarUso.setOnClickListener(v -> guardarUsoMaterial());
    }

    private void cargarMateriales() {
        listaMateriales = conexion.obtenerMaterialesDisponiblesParaUso();
        nombresMateriales = new ArrayList<>();

        for (InventarioModelo material : listaMateriales) {
            nombresMateriales.add(material.getNombreMaterial());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresMateriales
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaterialUso.setAdapter(adapter);

        if (listaMateriales.isEmpty()) {
            Toast.makeText(this, "No existen materiales con stock disponible", Toast.LENGTH_LONG).show();
            tvStockDisponible.setText("Stock disponible: 0");
        }
    }

    private void mostrarStockDisponible(int position) {
        if (listaMateriales == null || listaMateriales.isEmpty()) {
            tvStockDisponible.setText("Stock disponible: 0");
            return;
        }

        InventarioModelo material = listaMateriales.get(position);

        tvStockDisponible.setText(
                "Stock disponible: " +
                        material.getStockActual() +
                        " " +
                        material.getUnidadMedida()
        );
    }

    private void guardarUsoMaterial() {
        if (listaMateriales == null || listaMateriales.isEmpty()) {
            Toast.makeText(this, "No hay materiales disponibles para registrar consumo", Toast.LENGTH_SHORT).show();
            return;
        }

        String obra = etObraUso.getText().toString().trim();
        String actividad = etActividadUso.getText().toString().trim();
        String cantidadTexto = etCantidadUso.getText().toString().trim();
        String observacion = etObservacionUso.getText().toString().trim();

        if (obra.isEmpty()) {
            etObraUso.setError("Ingrese la obra");
            return;
        }

        if (actividad.isEmpty()) {
            etActividadUso.setError("Ingrese la actividad");
            return;
        }

        if (cantidadTexto.isEmpty()) {
            etCantidadUso.setError("Ingrese la cantidad utilizada");
            return;
        }

        double cantidad;

        try {
            cantidad = Double.parseDouble(cantidadTexto);
        } catch (NumberFormatException e) {
            etCantidadUso.setError("Cantidad inválida");
            return;
        }

        int posicion = spMaterialUso.getSelectedItemPosition();
        InventarioModelo materialSeleccionado = listaMateriales.get(posicion);

        String resultado = conexion.registrarUsoMaterial(
                obra,
                1,
                materialSeleccionado.getMaterialId(),
                cantidad,
                actividad,
                observacion
        );

        if (resultado.equals("OK")) {
            Toast.makeText(this, "Uso de material registrado correctamente", Toast.LENGTH_LONG).show();
            limpiarCampos();
            cargarMateriales();
        } else {
            Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
        }
    }

    private void limpiarCampos() {
        etObraUso.setText("");
        etActividadUso.setText("");
        etCantidadUso.setText("");
        etObservacionUso.setText("");

        if (spMaterialUso.getCount() > 0) {
            spMaterialUso.setSelection(0);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}