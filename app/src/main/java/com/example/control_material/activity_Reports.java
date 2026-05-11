package com.example.control_material;

import static com.example.control_material.R.*;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;

public class activity_Reports extends AppCompatActivity {
    Spinner spinnerReportes;
    Button btnGenerar;
    RecyclerView recyclerReportes;
    TextView txtTotal;

    ArrayList<ReporteModelo> listaReportes;

    ReporteAdapter adapter;

    BaseDatosSQLite conexion;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_reports);

        spinnerReportes = findViewById(R.id.spinnerReportes);
        btnGenerar = findViewById(R.id.btnGenerarReporte);
        recyclerReportes = findViewById(R.id.recyclerReportes);
        txtTotal = findViewById(R.id.txtTotal);

        conexion = new BaseDatosSQLite(this);
        db = conexion.getReadableDatabase();
        insertarDatosPrueba();

        listaReportes = new ArrayList<>();

        adapter = new ReporteAdapter(listaReportes);

        recyclerReportes.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerReportes.setAdapter(adapter);

        cargarSpinner();

        btnGenerar.setOnClickListener(v -> {
            generarReporte();
        });
    }

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
                android.R.layout.simple_spinner_dropdown_item);

        spinnerReportes.setAdapter(adapterSpinner);
    }

    private void generarReporte() {

        listaReportes.clear();

        String reporte =
                spinnerReportes.getSelectedItem().toString();

        if(reporte.equals("Materiales Bajo Stock")) {

            reporteBajoStock();

        } else if(reporte.equals("Entradas de Material")) {

            reporteEntradas();

        } else if(reporte.equals("Uso de Materiales")) {

            reporteUsoMateriales();

        } else if(reporte.equals("Alertas de Stock")) {

            reporteAlertas();
        }

        adapter.notifyDataSetChanged();

        txtTotal.setText(
                "Total registros: " +
                        listaReportes.size());
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

    private void reporteEntradas() {

        Cursor cursor = db.rawQuery(
                "SELECT entrada_id, fecha_entrada, observacion " +
                        "FROM entrada_material",
                null
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

    private void reporteUsoMateriales() {

        Cursor cursor = db.rawQuery(
                "SELECT actividad, fecha_uso, observacion " +
                        "FROM uso_material",
                null
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

    private void reporteAlertas() {

        Cursor cursor = db.rawQuery(
                "SELECT mensaje, fecha_alerta, atendida " +
                        "FROM alerta_stock",
                null
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
                        atendida == 1 ?
                                "Atendida" :
                                "Pendiente";

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
    //==================================================
    // DATOS DE PRUEBA
    //==================================================

    private void insertarDatosPrueba() {

        // LIMPIAR TABLAS

        db.execSQL("DELETE FROM alerta_stock");
        db.execSQL("DELETE FROM uso_material");
        db.execSQL("DELETE FROM entrada_material");
        db.execSQL("DELETE FROM material");
        db.execSQL("DELETE FROM obra");
        db.execSQL("DELETE FROM usuario");

        //=====================================
        // USUARIO
        //=====================================

        db.execSQL(
                "INSERT INTO usuario " +
                        "(nombre, apellido, edad, cedula, nacionalidad, genero, " +
                        "fechaNac, estadoCivil, username, password, nivelIngles, estado) " +

                        "VALUES " +

                        "('Jeremy', 'Soto', 20, 123456789, 'Ecuador', " +
                        "'Masculino', '2005-01-01', 'Soltero', " +
                        "'jeremy', '1234', 8.5, 1)"
        );

        //=====================================
        // OBRA
        //=====================================

        db.execSQL(
                "INSERT INTO obra " +
                        "(nombre, ubicacion, fecha_inicio, fecha_fin, estado) " +

                        "VALUES " +

                        "('Edificio Central', 'Guayaquil', " +
                        "'2026-05-01', '2026-12-31', 'Activa')"
        );

        //=====================================
        // MATERIAL
        //=====================================

        db.execSQL(
                "INSERT INTO material " +
                        "(nombre, descripcion, unidad_medida, " +
                        "stock_actual, stock_minimo, " +
                        "precio_unitario, estado) " +

                        "VALUES " +

                        "('Cemento', 'Saco de cemento', 'Unidad', " +
                        "5, 10, 8.50, 1)"
        );

        db.execSQL(
                "INSERT INTO material " +
                        "(nombre, descripcion, unidad_medida, " +
                        "stock_actual, stock_minimo, " +
                        "precio_unitario, estado) " +

                        "VALUES " +

                        "('Varilla', 'Varilla de hierro', 'Unidad', " +
                        "50, 20, 12.00, 1)"
        );

        //=====================================
        // ENTRADA MATERIAL
        //=====================================

        db.execSQL(
                "INSERT INTO entrada_material " +
                        "(usuario_id, fecha_entrada, observacion) " +

                        "VALUES " +

                        "(1, '2026-05-11', 'Ingreso de materiales')"
        );

        //=====================================
        // USO MATERIAL
        //=====================================

        db.execSQL(
                "INSERT INTO uso_material " +
                        "(obra_id, usuario_id, fecha_uso, actividad, observacion) " +

                        "VALUES " +

                        "(1, 1, '2026-05-11', " +
                        "'Construcción', 'Uso de cemento')"
        );

        //=====================================
        // ALERTA STOCK
        //=====================================

        db.execSQL(
                "INSERT INTO alerta_stock " +
                        "(material_id, fecha_alerta, mensaje, atendida) " +

                        "VALUES " +

                        "(1, '2026-05-11', " +
                        "'Stock bajo de cemento', 0)"
        );
    }
}