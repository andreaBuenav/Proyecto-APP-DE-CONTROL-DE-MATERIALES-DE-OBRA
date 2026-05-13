package com.example.control_material;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.control_material.entradamaterial.EntradaMaterialActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar
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
    }
}