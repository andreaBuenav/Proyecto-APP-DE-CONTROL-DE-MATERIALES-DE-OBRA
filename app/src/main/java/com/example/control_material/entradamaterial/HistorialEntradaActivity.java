package com.example.control_material.entradamaterial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.EntradaAdapter;
import com.example.control_material.EntradaModel;
import com.example.control_material.R;

import java.util.ArrayList;

public class HistorialEntradaActivity extends AppCompatActivity {

    RecyclerView recyclerEntradas;

    ArrayList<EntradaModel> listaEntradas;

    BaseDatosSQLite conexion;

    SQLiteDatabase db;

    EntradaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_entrada);

        recyclerEntradas = findViewById(R.id.recyclerEntradas);

        recyclerEntradas.setLayoutManager(
                new LinearLayoutManager(this)
        );

        conexion = new BaseDatosSQLite(this);

        db = conexion.getReadableDatabase();

        listaEntradas = new ArrayList<>();

        cargarEntradas();

        adapter = new com.example.control_material.EntradaAdapter(listaEntradas);

        recyclerEntradas.setAdapter(adapter);
    }

    private void cargarEntradas() {

        Cursor cursor = db.rawQuery(

                "SELECT em.fecha_entrada, " +
                        "m.nombre, " +
                        "de.cantidad, " +
                        "de.precio_unitario " +

                        "FROM detalle_entrada de " +

                        "INNER JOIN entrada_material em " +
                        "ON de.entrada_id = em.entrada_id " +

                        "INNER JOIN material m " +
                        "ON de.material_id = m.material_id",

                null
        );

        while (cursor.moveToNext()) {

            String fecha = cursor.getString(0);

            String material = cursor.getString(1);

            double cantidad = cursor.getDouble(2);

            double precio = cursor.getDouble(3);

            listaEntradas.add(
                    new com.example.control_material.EntradaModel(
                            fecha,
                            material,
                            cantidad,
                            precio
                    )
            );
        }

        cursor.close();
    }
}