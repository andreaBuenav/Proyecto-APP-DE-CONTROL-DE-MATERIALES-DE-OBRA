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
import android.widget.AdapterView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.HistorialEntradaActivity;
import com.example.control_material.R;

import java.util.ArrayList;

public class EntradaMaterialActivity extends AppCompatActivity {

    Spinner spMaterial;

    EditText etCantidad;
    EditText etPrecio;
    EditText etObservacion;

    Button btnGuardar;
    Button btnHistorial;
    TextView tvStock;

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

        btnHistorial = findViewById(R.id.btnHistorial);

        tvStock = findViewById(R.id.tvStock);

        // CONEXION SQLITE

        conexion = new BaseDatosSQLite(this);

        db = conexion.getWritableDatabase();

        // CARGAR MATERIALES EN SPINNER

        cargarMateriales();

        spMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {

                mostrarStock(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // BOTON GUARDAR

        btnGuardar.setOnClickListener(v -> guardarEntrada());
        btnHistorial.setOnClickListener(v -> {

            startActivity(
                    new android.content.Intent(
                            this,
                            HistorialEntradaActivity.class
                    )
            );
        });

    }

    // LIMPIAR CAMPOS

    private void limpiarCampos() {

        etCantidad.setText("");

        etPrecio.setText("");

        etObservacion.setText("");

        spMaterial.setSelection(0);
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
    private void mostrarStock(int position) {

        try {

            int materialId = listaIds.get(position);

            Cursor cursor = db.rawQuery(
                    "SELECT stock_actual FROM material WHERE material_id = ?",
                    new String[]{String.valueOf(materialId)}
            );

            if (cursor.moveToFirst()) {

                double stock = cursor.getDouble(0);

                tvStock.setText("Stock actual: " + stock);
            }

            cursor.close();

        } catch (Exception e) {

            tvStock.setText("Stock actual: Error");
        }
    }

    // GUARDAR ENTRADA

    private void guardarEntrada() {

        try {

            // VALIDAR QUE EXISTAN MATERIALES

            if (listaIds.isEmpty()) {

                Toast.makeText(
                        this,
                        "No existen materiales registrados",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // OBTENER DATOS

            String cantidadTexto = etCantidad.getText().toString().trim();

            String precioTexto = etPrecio.getText().toString().trim();

            String observacion = etObservacion.getText().toString().trim();

            // VALIDAR CAMPOS VACIOS

            if (cantidadTexto.isEmpty() || precioTexto.isEmpty()) {

                Toast.makeText(
                        this,
                        "Complete todos los campos",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // CONVERTIR NUMEROS

            double cantidad = Double.parseDouble(cantidadTexto);

            double precio = Double.parseDouble(precioTexto);

            // VALIDAR NEGATIVOS O CERO

            if (cantidad <= 0 || precio <= 0) {

                Toast.makeText(
                        this,
                        "Cantidad y precio deben ser mayores a 0",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // OBTENER MATERIAL SELECCIONADO

            int posicion = spMaterial.getSelectedItemPosition();

            int materialId = listaIds.get(posicion);

            // INSERTAR ENTRADA

            ContentValues valuesEntrada = new ContentValues();

            valuesEntrada.put("usuario_id", 1);

            String fechaActual = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm",
                    Locale.getDefault()
            ).format(new Date());

            valuesEntrada.put("fecha_entrada", fechaActual);

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

            // MENSAJE EXITO

            Toast.makeText(
                    this,
                    "Entrada registrada correctamente",
                    Toast.LENGTH_LONG
            ).show();

            // LIMPIAR CAMPOS

            limpiarCampos();

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Ingrese valores numéricos válidos",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        }
    }


}