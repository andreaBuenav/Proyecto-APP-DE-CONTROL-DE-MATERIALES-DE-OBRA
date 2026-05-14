package com.example.control_material;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.control_material.catalog.CatalogoActivity;
import com.example.control_material.inventory.InvView;

import com.example.control_material.entradamaterial.EntradaMaterialActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar preferencias
        preferences = getSharedPreferences("datosLogin", MODE_PRIVATE);
        editor = preferences.edit();

        Intent intent = new Intent(
                MainActivity.this,
                EntradaMaterialActivity.class
        );

        startActivity(intent);
        // Botón cerrar sesión
        Button btnCerrar = findViewById(R.id.btnCerrarSesion);
        btnCerrar.setOnClickListener(v -> {
            editor.clear();
            editor.apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Card: Catálogo de Materiales
        CardView cardCatalogo = findViewById(R.id.cardCatalogo);
        cardCatalogo.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CatalogoActivity.class)));

        // Card: Disponibilidad de Inventario
        CardView cardInventario = findViewById(R.id.cardInventario);
        cardInventario.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, InvView.class)));

        // Card: Reportes
        CardView cardReportes = findViewById(R.id.cardReportes);
        cardReportes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, activity_Reports.class)));
    }
}
