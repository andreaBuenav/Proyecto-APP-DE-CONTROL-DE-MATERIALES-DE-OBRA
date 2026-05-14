package com.example.control_material.catalog;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CatalogoFormActivity extends AppCompatActivity {

    private static final String[] CATEGORIAS = {
            "Aglomerante", "Árido", "Metal", "Madera",
            "Cerámica", "Sintético", "Otro"
    };

    private static final String[] UNIDADES = {
            "kg", "g", "toneladas", "m", "m²", "m³",
            "metros lineales", "litros", "galones",
            "unidades", "piezas", "bolsas", "sacos", "rollos", "cajas"
    };

    private TextView tvFormTitle;
    private TextInputLayout tilNombre;
    private TextInputEditText etNombre;
    private TextInputEditText etDescripcion;
    private TextInputLayout tilCategoria;
    private AutoCompleteTextView spinnerCategoria;
    private TextInputLayout tilUnidad;
    private AutoCompleteTextView spinnerUnidad;
    private TextInputEditText etPrecio;

    private BaseDatosSQLite db;
    private int materialId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_form);

        db = new BaseDatosSQLite(this);
        materialId = getIntent().getIntExtra("materialId", -1);

        MaterialToolbar toolbar = findViewById(R.id.catalogoFormToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        bindViews();
        setupDropdowns();

        tvFormTitle.setText(materialId == -1 ? "Registrar Material" : "Editar Material");

        if (materialId != -1) {
            cargarDatosMaterial();
        }

        MaterialButton btnGuardar = findViewById(R.id.catalogoBtnGuardar);
        MaterialButton btnCancelar = findViewById(R.id.catalogoBtnCancelar);

        btnGuardar.setOnClickListener(v -> guardar());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void bindViews() {
        tvFormTitle = findViewById(R.id.catalogoFormTitle);
        tilNombre = findViewById(R.id.catalogoTilNombre);
        etNombre = findViewById(R.id.catalogoEtNombre);
        etDescripcion = findViewById(R.id.catalogoEtDescripcion);
        tilCategoria = findViewById(R.id.catalogoTilCategoria);
        spinnerCategoria = findViewById(R.id.catalogoSpinnerCategoria);
        tilUnidad = findViewById(R.id.catalogoTilUnidad);
        spinnerUnidad = findViewById(R.id.catalogoSpinnerUnidad);
        etPrecio = findViewById(R.id.catalogoEtPrecio);
    }

    private void setupDropdowns() {
        ArrayAdapter<String> adapterCat = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, CATEGORIAS);
        spinnerCategoria.setAdapter(adapterCat);

        ArrayAdapter<String> adapterUnidad = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, UNIDADES);
        spinnerUnidad.setAdapter(adapterUnidad);
    }

    private void cargarDatosMaterial() {
        MaterialModelo m = db.obtenerMaterialPorId(materialId);
        if (m == null) return;
        etNombre.setText(m.getNombre());
        etDescripcion.setText(m.getDescripcion());
        spinnerCategoria.setText(m.getCategoria(), false);
        spinnerUnidad.setText(m.getUnidadMedida(), false);
        if (m.getPrecioUnitario() > 0) {
            etPrecio.setText(String.valueOf(m.getPrecioUnitario()));
        }
    }

    private void guardar() {
        String nombre = etNombre.getText() != null
                ? etNombre.getText().toString().trim() : "";
        String descripcion = etDescripcion.getText() != null
                ? etDescripcion.getText().toString().trim() : "";
        String categoria = spinnerCategoria.getText() != null
                ? spinnerCategoria.getText().toString().trim() : "";
        String unidad = spinnerUnidad.getText() != null
                ? spinnerUnidad.getText().toString().trim() : "";
        String precioStr = etPrecio.getText() != null
                ? etPrecio.getText().toString().trim() : "";

        boolean valido = true;

        if (nombre.isEmpty()) {
            tilNombre.setError("El nombre es obligatorio");
            valido = false;
        } else {
            tilNombre.setError(null);
        }

        if (categoria.isEmpty()) {
            tilCategoria.setError("Seleccione una categoría");
            valido = false;
        } else {
            tilCategoria.setError(null);
        }

        if (unidad.isEmpty()) {
            tilUnidad.setError("Seleccione una unidad de medida");
            valido = false;
        } else {
            tilUnidad.setError(null);
        }

        if (!valido) return;

        double precio = 0.0;
        if (!precioStr.isEmpty()) {
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                precio = 0.0;
            }
        }

        if (materialId == -1) {
            db.insertarMaterial(nombre, descripcion, categoria, unidad, precio);
            Toast.makeText(this, "Material registrado", Toast.LENGTH_SHORT).show();
        } else {
            db.actualizarMaterial(materialId, nombre, descripcion, categoria, unidad, precio);
            Toast.makeText(this, "Material actualizado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
