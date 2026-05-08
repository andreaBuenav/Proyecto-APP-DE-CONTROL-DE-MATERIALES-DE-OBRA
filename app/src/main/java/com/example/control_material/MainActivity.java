package com.example.control_material;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Control de Materiales");
        }

        preferences = getSharedPreferences("datosLogin", MODE_PRIVATE);
        editor = preferences.edit();

        Button btnCerrar = findViewById(R.id.btnCerrarSesion);

        btnCerrar.setOnClickListener(v -> {
            editor.clear();
            editor.apply();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_acerca_de) {
            mostrarDialogoAcercaDe();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoAcercaDe() {
        View view = getLayoutInflater().inflate(R.layout.dialog_acerca_de, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        Button btnCerrar = view.findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}