package com.example.control_material;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private static final String usuario_valido = "Mario";
    private static final String clave_valido = "12345";

    TextInputEditText usuario, clave;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = findViewById(R.id.login_txtusuario);
        clave = findViewById(R.id.login_txtclave);

        // 🔹 Inicializar SharedPreferences
        preferences = getSharedPreferences("datosLogin", MODE_PRIVATE);
        editor = preferences.edit();

        // 🔹 AUTOLOGIN
        String usuarioGuardado = preferences.getString("usuario", null);
        String claveGuardada = preferences.getString("clave", null);

        if (usuarioGuardado != null && claveGuardada != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void iniciarSesion(View v) {

        String usuarioT = usuario.getText().toString();
        String claveT = clave.getText().toString();

        if (usuarioT.equals(usuario_valido) && claveT.equals(clave_valido)) {

            CheckBox cbRecordar = findViewById(R.id.cbRecordar);

            if (cbRecordar.isChecked()) {
                editor.putString("usuario", usuarioT);
                editor.putString("clave", claveT);
                editor.apply();
            }

            startActivity(new Intent(LoginActivity.this, activity_Reports.class));
            finish();

        } else {
            Toast.makeText(this, "Datos Incorrectos", Toast.LENGTH_LONG).show();
        }
    }

    public void registrarse(View v) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}