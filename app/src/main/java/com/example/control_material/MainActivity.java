package com.example.control_material;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.control_material.ControlUsuario.ConsultarUsuarioActivity;
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

        preferences = getSharedPreferences("datosLogin", MODE_PRIVATE);
        editor = preferences.edit();

        // Botón cerrar sesión
        Button btnCerrar = findViewById(R.id.btnCerrarSesion);

        btnCerrar.setOnClickListener(v -> {
            editor.clear();
            editor.apply();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Card catálogo
        CardView cardCatalogo = findViewById(R.id.cardCatalogo);

        cardCatalogo.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CatalogoActivity.class)));

        // Inventario
        CardView cardInventario = findViewById(R.id.cardInventario);

        cardInventario.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, InvView.class)));

        // Reportes
        CardView cardReportes = findViewById(R.id.cardReportes);

        cardReportes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, activity_Reports.class)));

        // Entrada Material
        CardView cardEntradaMaterial = findViewById(R.id.cardEntradaMaterial);

        cardEntradaMaterial.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, EntradaMaterialActivity.class)));

        // Control usuario
        Button cardControlUsuario = findViewById(R.id.cardControl);

        cardControlUsuario.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ConsultarUsuarioActivity.class)));
    }
}
