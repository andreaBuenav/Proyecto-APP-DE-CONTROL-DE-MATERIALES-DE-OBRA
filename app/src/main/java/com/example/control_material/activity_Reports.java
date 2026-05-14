package com.example.control_material;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class activity_Reports extends AppCompatActivity {

    Spinner spinnerReportes;
    Button btnGenerar;

    RecyclerView recyclerReportes;

    TextView txtTotal;

    EditText txtFechaInicio, txtFechaFin;

    ArrayList<ReporteModelo> listaReportes;

    ReporteAdapter adapter;

    BaseDatosSQLite conexion;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        spinnerReportes = findViewById(R.id.spinnerReportes);
        btnGenerar = findViewById(R.id.btnGenerarReporte);

        recyclerReportes = findViewById(R.id.recyclerReportes);

        txtTotal = findViewById(R.id.txtTotal);

        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);

        conexion = new BaseDatosSQLite(this);

        db = conexion.getReadableDatabase();

        listaReportes = new ArrayList<>();

        adapter = new ReporteAdapter(listaReportes);

        recyclerReportes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerReportes.setAdapter(adapter);

        cargarSpinner();

        //=========================================
        // DATE PICKER FECHA INICIO
        //=========================================

        txtFechaInicio.setOnClickListener(v -> {
            mostrarDatePicker(txtFechaInicio);
        });

        //=========================================
        // DATE PICKER FECHA FIN
        //=========================================

        txtFechaFin.setOnClickListener(v -> {
            mostrarDatePicker(txtFechaFin);
        });

        //=========================================
        // BOTON GENERAR
        //=========================================

        btnGenerar.setOnClickListener(v -> {
            generarReporte();
        });
    }

    //=========================================
    // DATE PICKER
    //=========================================

    private void mostrarDatePicker(EditText editText) {

        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, year1, month1, dayOfMonth) -> {

                            String fecha =
                                    year1 + "-" +
                                            String.format("%02d", month1 + 1) + "-" +
                                            String.format("%02d", dayOfMonth);

                            editText.setText(fecha);

                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }

    //=========================================
    // CARGAR SPINNER
    //=========================================

    private void cargarSpinner() {

        String[] reportes = {

                "Materiales Bajo Stock",
                "Entradas de Material",
                "Uso de Materiales",
                "Alertas de Stock"
        };

        ArrayAdapter<String> adapterSpinner =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        reportes
                );

        adapterSpinner.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerReportes.setAdapter(adapterSpinner);
    }

    //=========================================
    // GENERAR REPORTES
    //=========================================

    private void generarReporte() {

        listaReportes.clear();

        String reporte =
                spinnerReportes.getSelectedItem().toString();

        String fechaInicio =
                txtFechaInicio.getText().toString();

        String fechaFin =
                txtFechaFin.getText().toString();

        if(reporte.equals("Materiales Bajo Stock")) {

            reporteBajoStock();

        }
        else if(reporte.equals("Entradas de Material")) {

            reporteEntradas(fechaInicio, fechaFin);

        }
        else if(reporte.equals("Uso de Materiales")) {

            reporteUsoMateriales(fechaInicio, fechaFin);

        }
        else if(reporte.equals("Alertas de Stock")) {

            reporteAlertas(fechaInicio, fechaFin);
        }

        adapter.notifyDataSetChanged();

        txtTotal.setText(
                "Total registros: " +
                        listaReportes.size()
        );
    }

    //=========================================
    // REPORTE BAJO STOCK
    //=========================================

    private void reporteBajoStock() {

        Cursor cursor = db.rawQuery(

                "SELECT nombre, stock_actual, stock_minimo " +
                        "FROM material " +
                        "WHERE stock_actual <= stock_minimo",

                null
        );

        if(cursor.moveToFirst()) {

            do {

                String nombre =
                        cursor.getString(0);

                String stockActual =
                        cursor.getString(1);

                String stockMinimo =
                        cursor.getString(2);

                listaReportes.add(

                        new ReporteModelo(
                                nombre,
                                "Stock actual: " + stockActual,
                                "Stock mínimo: " + stockMinimo
                        )
                );

            } while(cursor.moveToNext());
        }

        cursor.close();
    }

    //=========================================
    // REPORTE ENTRADAS
    //=========================================

    private void reporteEntradas(
            String fechaInicio,
            String fechaFin
    ) {

        Cursor cursor = db.rawQuery(

                "SELECT entrada_id, fecha_entrada, observacion " +
                        "FROM entrada_material " +
                        "WHERE fecha_entrada BETWEEN ? AND ?",

                new String[]{
                        fechaInicio,
                        fechaFin
                }
        );

        if(cursor.moveToFirst()) {

            do {

                String id =
                        cursor.getString(0);

                String fecha =
                        cursor.getString(1);

                String observacion =
                        cursor.getString(2);

                listaReportes.add(

                        new ReporteModelo(
                                "Entrada #" + id,
                                "Fecha: " + fecha,
                                observacion
                        )
                );

            } while(cursor.moveToNext());
        }

        cursor.close();
    }

    //=========================================
    // REPORTE USO MATERIALES
    //=========================================

    private void reporteUsoMateriales(
            String fechaInicio,
            String fechaFin
    ) {

        Cursor cursor = db.rawQuery(

                "SELECT actividad, fecha_uso, observacion " +
                        "FROM uso_material " +
                        "WHERE fecha_uso BETWEEN ? AND ?",

                new String[]{
                        fechaInicio,
                        fechaFin
                }
        );

        if(cursor.moveToFirst()) {

            do {

                String actividad =
                        cursor.getString(0);

                String fecha =
                        cursor.getString(1);

                String observacion =
                        cursor.getString(2);

                listaReportes.add(

                        new ReporteModelo(
                                actividad,
                                "Fecha: " + fecha,
                                observacion
                        )
                );

            } while(cursor.moveToNext());
        }

        cursor.close();
    }

    //=========================================
    // REPORTE ALERTAS
    //=========================================

    private void reporteAlertas(
            String fechaInicio,
            String fechaFin
    ) {

        Cursor cursor = db.rawQuery(

                "SELECT mensaje, fecha_alerta, atendida " +
                        "FROM alerta_stock " +
                        "WHERE fecha_alerta BETWEEN ? AND ?",

                new String[]{
                        fechaInicio,
                        fechaFin
                }
        );

        if(cursor.moveToFirst()) {

            do {

                String mensaje =
                        cursor.getString(0);

                String fecha =
                        cursor.getString(1);

                int atendida =
                        cursor.getInt(2);

                String estado =
                        atendida == 1
                                ? "Atendida"
                                : "Pendiente";

                listaReportes.add(

                        new ReporteModelo(
                                mensaje,
                                "Fecha: " + fecha,
                                estado
                        )
                );

            } while(cursor.moveToNext());
        }

        cursor.close();
    }

    public void regresarMenu(View v) {
        Intent Menu = new Intent(this, MainActivity.class);
        startActivity(Menu);
        finish();
    }
}