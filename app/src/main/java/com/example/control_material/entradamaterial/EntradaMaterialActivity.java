package com.example.control_material.entradamaterial;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.R;

import java.util.ArrayList;

public class EntradaMaterialActivity extends AppCompatActivity {

    Spinner spMaterial;

    EditText etCantidad;
    EditText etPrecio;
    EditText etObservacion;

    Button btnGuardar;

    BaseDatosSQLite conexion;

    SQLiteDatabase db;

    ArrayList<String> listaMateriales;

    ArrayList<Integer> listaIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_material);

        // CONECTAR COMPONENTES

        spMaterial = findViewById(R.id.spMaterial);

        etCantidad = findViewById(R.id.etCantidad);

        etPrecio = findViewById(R.id.etPrecio);

        etObservacion = findViewById(R.id.etObservacion);

        btnGuardar = findViewById(R.id.btnGuardar);

        // CONEXION SQLITE

        conexion = new BaseDatosSQLite(this);

        db = conexion.getWritableDatabase();

        // CARGAR MATERIALES EN SPINNER

        cargarMateriales();

        // BOTON GUARDAR

        btnGuardar.setOnClickListener(v -> guardarEntrada());

    }

    // CARGAR MATERIALES DESDE SQLITE

    private void cargarMateriales() {

        listaMateriales = new ArrayList<>();

        listaIds = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT material_id, nombre FROM material",
                null
        );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);

            String nombre = cursor.getString(1);

            listaIds.add(id);

            listaMateriales.add(nombre);
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaMateriales
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spMaterial.setAdapter(adapter);
    }

    // GUARDAR ENTRADA

    private void guardarEntrada() {

        String cantidadTexto = etCantidad.getText().toString();

        String precioTexto = etPrecio.getText().toString();

        String observacion = etObservacion.getText().toString();

        // VALIDAR CAMPOS

        if (cantidadTexto.isEmpty() || precioTexto.isEmpty()) {

            Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // CONVERTIR VALORES

        double cantidad = Double.parseDouble(cantidadTexto);

        double precio = Double.parseDouble(precioTexto);

        // OBTENER MATERIAL SELECCIONADO

        int posicion = spMaterial.getSelectedItemPosition();

        int materialId = listaIds.get(posicion);

        // INSERTAR EN entrada_material

        ContentValues valuesEntrada = new ContentValues();

        valuesEntrada.put("usuario_id", 1);

        valuesEntrada.put(
                "fecha_entrada",
                String.valueOf(System.currentTimeMillis())
        );

        valuesEntrada.put("observacion", observacion);

        long entradaId = db.insert(
                "entrada_material",
                null,
                valuesEntrada
        );

        // VALIDAR INSERT

        if (entradaId == -1) {

            Toast.makeText(
                    this,
                    "Error al guardar entrada",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // INSERTAR DETALLE

        ContentValues valuesDetalle = new ContentValues();

        valuesDetalle.put("entrada_id", entradaId);

        valuesDetalle.put("material_id", materialId);

        valuesDetalle.put("cantidad", cantidad);

        valuesDetalle.put("precio_unitario", precio);

        long detalleId = db.insert(
                "detalle_entrada",
                null,
                valuesDetalle
        );

        // VALIDAR DETALLE

        if (detalleId == -1) {

            Toast.makeText(
                    this,
                    "Error al guardar detalle",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // ACTUALIZAR STOCK

        db.execSQL(
                "UPDATE material " +
                        "SET stock_actual = stock_actual + ? " +
                        "WHERE material_id = ?",
                new Object[]{cantidad, materialId}
        );

        Toast.makeText(
                this,
                "Entrada guardada correctamente",
                Toast.LENGTH_LONG
        ).show();

        limpiarCampos();
    }

    // LIMPIAR CAMPOS

    private void limpiarCampos() {

        etCantidad.setText("");

        etPrecio.setText("");

        etObservacion.setText("");

        spMaterial.setSelection(0);
    }
}