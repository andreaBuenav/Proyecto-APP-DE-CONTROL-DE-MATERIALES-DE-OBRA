package com.example.control_material.ControlUsuario;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.control_material.BaseDatosSQLite;
import com.example.control_material.LoginActivity;
import com.example.control_material.MainActivity;
import com.example.control_material.R;
import com.google.android.material.textfield.TextInputEditText;

public class ConsultarUsuarioActivity extends AppCompatActivity {

    TextInputEditText cedulaInput, nombresInput, apellidosInput,
            edadInput, fechaInput, usuarioInput, passwordInput;

    Spinner nacionalidadSpinner, generoSpinner;

    RadioGroup estadoCivilGroup;

    RatingBar ratingBar;

    Button buscarBtn, actualizarBtn, eliminarBtn;

    BaseDatosSQLite db;

    int usuarioId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_usuario);

        db = new BaseDatosSQLite(this);

        inicializarComponentes();

        cargarSpinners();

        buscarBtn.setOnClickListener(v -> buscarUsuario());

        actualizarBtn.setOnClickListener(v -> actualizarUsuario());

        eliminarBtn.setOnClickListener(v -> eliminarUsuario());
    }

    private void inicializarComponentes(){

        cedulaInput = findViewById(R.id.cedulaInput);
        nombresInput = findViewById(R.id.nombresInput);
        apellidosInput = findViewById(R.id.apellidosInput);
        edadInput = findViewById(R.id.edadInput);
        fechaInput = findViewById(R.id.fechaNacInput);
        usuarioInput = findViewById(R.id.txtUsuario);
        passwordInput = findViewById(R.id.txtpassword);

        nacionalidadSpinner = findViewById(R.id.nacionalidadSpinner);
        generoSpinner = findViewById(R.id.generoSpinner);

        estadoCivilGroup = findViewById(R.id.estadoCivilGroup);

        ratingBar = findViewById(R.id.nivelInglesRating);

        buscarBtn = findViewById(R.id.btn_Buscar);
        actualizarBtn = findViewById(R.id.btn_actualizar);
        eliminarBtn = findViewById(R.id.btn_eliminar);
    }

    private void cargarSpinners(){

        String[] nacionalidades = {"Ecuatoriana", "Colombiana", "Peruana"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nacionalidades
        );

        nacionalidadSpinner.setAdapter(adapter1);

        String[] generos = {"Masculino", "Femenino"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                generos
        );

        generoSpinner.setAdapter(adapter2);
    }

    private void buscarUsuario(){

        String cedula = cedulaInput.getText().toString();

        Cursor cursor = db.buscarUsuarioPorCedula(cedula);

        if(cursor.moveToFirst()){

            usuarioId = cursor.getInt(0);

            nombresInput.setText(cursor.getString(1));
            apellidosInput.setText(cursor.getString(2));
            edadInput.setText(cursor.getString(3));

            nacionalidadSpinner.setSelection(
                    obtenerPosicionSpinner(
                            nacionalidadSpinner,
                            cursor.getString(5)
                    )
            );

            generoSpinner.setSelection(
                    obtenerPosicionSpinner(
                            generoSpinner,
                            cursor.getString(6)
                    )
            );

            fechaInput.setText(cursor.getString(7));

            String estadoCivil = cursor.getString(8);

            if(estadoCivil.equals("Soltero")){
                ((RadioButton)findViewById(R.id.radioSoltero)).setChecked(true);
            }else{
                ((RadioButton)findViewById(R.id.radioCasado)).setChecked(true);
            }

            usuarioInput.setText(cursor.getString(9));
            passwordInput.setText(cursor.getString(10));

            ratingBar.setRating(cursor.getFloat(11));

            Toast.makeText(this,
                    "Usuario encontrado",
                    Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(this,
                    "Usuario no encontrado",
                    Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void actualizarUsuario(){

        if(usuarioId == -1){

            Toast.makeText(this,
                    "Primero busque un usuario",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        String nombre = nombresInput.getText().toString();
        String apellido = apellidosInput.getText().toString();

        int edad = Integer.parseInt(
                edadInput.getText().toString()
        );

        String nacionalidad = nacionalidadSpinner.getSelectedItem().toString();

        String genero = generoSpinner.getSelectedItem().toString();

        String fecha = fechaInput.getText().toString();

        String estadoCivil = "";

        int radioSeleccionado = estadoCivilGroup.getCheckedRadioButtonId();

        RadioButton radioButton = findViewById(radioSeleccionado);

        estadoCivil = radioButton.getText().toString();

        String username = usuarioInput.getText().toString();
        String password = passwordInput.getText().toString();

        float nivel = ratingBar.getRating();

        int respuesta = db.actualizarUsuario(
                usuarioId,
                nombre,
                apellido,
                edad,
                nacionalidad,
                genero,
                fecha,
                estadoCivil,
                username, password,
                nivel
        );

        if(respuesta > 0){

            Toast.makeText(this,
                    "Usuario actualizado correctamente",
                    Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(this,
                    "Error al actualizar",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarUsuario(){

        if(usuarioId == -1){

            Toast.makeText(this,
                    "Primero busque un usuario",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        int respuesta = db.eliminarUsuario(usuarioId);

        if(respuesta > 0){

            Toast.makeText(this,
                    "Usuario eliminado",
                    Toast.LENGTH_SHORT).show();

            limpiarCampos();

        }else{

            Toast.makeText(this,
                    "Error al eliminar",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos(){

        nombresInput.setText("");
        apellidosInput.setText("");
        edadInput.setText("");
        fechaInput.setText("");
        usuarioInput.setText("");

        ratingBar.setRating(0);

        usuarioId = -1;
    }

    private int obtenerPosicionSpinner(Spinner spinner, String valor){

        for(int i = 0; i < spinner.getCount(); i++){

            if(spinner.getItemAtPosition(i).toString().equals(valor)){

                return i;
            }
        }

        return 0;
    }

    public void regresarMenu(View v) {
        Intent menu = new Intent(this, MainActivity.class);
        startActivity(menu);
        finish();
    }
}