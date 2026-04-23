package com.example.control_material;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private static  final String usuario_valido ="Mario";
    private static  final String clave_valido ="12345";

    TextInputEditText usuario, clave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usuario = findViewById(R.id.login_txtusuario);
        clave = findViewById(R.id.login_txtclave);
    }

    public void iniciarSesion(View v){
        String usuarioT = usuario.getText().toString();
        String claveT = clave.getText().toString();

        if(usuarioT.equals(usuario_valido)&& claveT.equals(clave_valido)){
            Toast.makeText(v.getContext(),"Acceso Concedido" , Toast.LENGTH_LONG).show();
            Intent paginaprincipal = new Intent(v.getContext(), MainActivity.class);
            startActivity(paginaprincipal);
        }else{
            Toast.makeText(v.getContext(),"Datos Incorrectos", Toast.LENGTH_LONG).show();
        }


    }
}