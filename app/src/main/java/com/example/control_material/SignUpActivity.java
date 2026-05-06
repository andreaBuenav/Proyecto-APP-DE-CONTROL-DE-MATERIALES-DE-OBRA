package com.example.control_material;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText cedulaInput, nombresInput, apellidosInput, edadInput, fechaNacInput;
    private Spinner nacionalidadSpinner, generoSpinner;
    private RadioGroup estadoCivilGroup;
    private RatingBar nivelInglesRating;
    private Button registrarBtn, borrarBtn, cancBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cedulaInput = findViewById(R.id.cedulaInput);
        nombresInput = findViewById(R.id.nombresInput);
        apellidosInput = findViewById(R.id.apellidosInput);
        edadInput = findViewById(R.id.edadInput);
        fechaNacInput = findViewById(R.id.fechaNacInput);
        nacionalidadSpinner = findViewById(R.id.nacionalidadSpinner);
        generoSpinner = findViewById(R.id.generoSpinner);
        estadoCivilGroup = findViewById(R.id.estadoCivilGroup);
        nivelInglesRating = findViewById(R.id.nivelInglesRating);
        registrarBtn = findViewById(R.id.registrarBtn);
        borrarBtn = findViewById(R.id.borrarBtn);
        cancBtn = findViewById(R.id.cancBtn);

        String[] nacionalidades = {"Ecuatoriana", "Colombiana", "Peruana", "Otra"};
        String[] generos = {"Masculino", "Femenino", "Otro"};

        nacionalidadSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nacionalidades));
        generoSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos));

        findViewById(R.id.fechaBtn).setOnClickListener(v -> showDatePickerDialog());
        fechaNacInput.setOnClickListener(v -> showDatePickerDialog());
        registrarBtn.setOnClickListener(v -> mostrarRegistro());
        borrarBtn.setOnClickListener(v -> borrarCampos());
        cancBtn.setOnClickListener(v -> regresarALogin(v));
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> fechaNacInput.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    public void regresarALogin(View v) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void mostrarRegistro() {
        String cedula = cedulaInput.getText().toString();
        String nombres = nombresInput.getText().toString();
        String apellidos = apellidosInput.getText().toString();
        String edad = edadInput.getText().toString();
        String nacionalidad = nacionalidadSpinner.getSelectedItem().toString();
        String genero = generoSpinner.getSelectedItem().toString();
        String fechaNac = fechaNacInput.getText().toString();
        float nivelIngles = nivelInglesRating.getRating();

        int selectedId = estadoCivilGroup.getCheckedRadioButtonId();
        String estadoCivil = "";
        if (selectedId != -1) {
            RadioButton rb = findViewById(selectedId);
            estadoCivil = rb.getText().toString();
        }

        String info = "Cédula: " + cedula + "\n" +
                "Nombres: " + nombres + "\n" +
                "Apellidos: " + apellidos + "\n" +
                "Edad: " + edad + "\n" +
                "Nacionalidad: " + nacionalidad + "\n" +
                "Género: " + genero + "\n" +
                "Estado Civil: " + estadoCivil + "\n" +
                "Fecha Nac: " + fechaNac + "\n" +
                "Nivel Inglés: " + nivelIngles + " estrellas";

        Log.d("REGISTRO_SISTEMA", info);
        Toast.makeText(this, "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
    }

    public void borrarCampos() {
        cedulaInput.setText("");
        nombresInput.setText("");
        apellidosInput.setText("");
        edadInput.setText("");
        fechaNacInput.setText("");
        nacionalidadSpinner.setSelection(0);
        generoSpinner.setSelection(0);
        estadoCivilGroup.clearCheck();
        nivelInglesRating.setRating(0);
        Toast.makeText(this, "Campos borrados", Toast.LENGTH_SHORT).show();
    }
}