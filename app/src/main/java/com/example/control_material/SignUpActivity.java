package com.example.control_material;

import android.app.AlertDialog;
import android.content.Context;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText cedulaInput, nombresInput, apellidosInput, edadInput, fechaNacInput, usuarioInput, contraseniaInput;
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
        Button mostrarBtn = findViewById(R.id.mostrarBtn);

        String[] nacionalidades = { "Ecuatoriana", "Colombiana", "Peruana", "Otra" };
        String[] generos = { "Masculino", "Femenino", "Otro" };

        nacionalidadSpinner
                .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nacionalidades));
        generoSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos));

        findViewById(R.id.fechaBtn).setOnClickListener(v -> showDatePickerDialog());
        fechaNacInput.setOnClickListener(v -> showDatePickerDialog());
        registrarBtn.setOnClickListener(v -> mostrarRegistro());
        mostrarBtn.setOnClickListener(v -> mostrarDatosEnModal());
        borrarBtn.setOnClickListener(v -> borrarCampos());
        cancBtn.setOnClickListener(v -> regresarALogin(v));
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> fechaNacInput
                        .setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
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
        String usuario = usuarioInput.getText().toString();
        String contrasenia = contraseniaInput.getText().toString();
        float nivelIngles = nivelInglesRating.getRating();

        int selectedId = estadoCivilGroup.getCheckedRadioButtonId();
        String estadoCivil = "";
        if (selectedId != -1) {
            RadioButton rb = findViewById(selectedId);
            estadoCivil = rb.getText().toString();
        }

        String info = cedula + ";" + nombres + ";" + apellidos + ";" + edad + ";" +
                nacionalidad + ";" + genero + ";" + estadoCivil + ";" +
                fechaNac + ";" + nivelIngles + "\n";

        try {
            FileOutputStream fos = openFileOutput("usuarios.txt", Context.MODE_APPEND);
            fos.write(info.getBytes());
            fos.close();
            Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            borrarCampos();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarDatosEnModal() {
        try {
            FileInputStream fis = openFileInput("usuarios.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 9) {
                    sb.append("Cédula: ").append(datos[0]).append("\n")
                            .append("Nombres: ").append(datos[1]).append("\n")
                            .append("Apellidos: ").append(datos[2]).append("\n")
                            .append("Edad: ").append(datos[3]).append("\n")
                            .append("Nacionalidad: ").append(datos[4]).append("\n")
                            .append("Género: ").append(datos[5]).append("\n")
                            .append("Estado Civil: ").append(datos[6]).append("\n")
                            .append("Fecha Nac: ").append(datos[7]).append("\n")
                            .append("Nivel Inglés: ").append(datos[8]).append("\n\n");
                } else {
                    sb.append(linea).append("\n\n");
                }
            }
            fis.close();

            String mensaje = sb.toString();
            if (mensaje.isEmpty()) {
                mensaje = "No hay datos registrados.";
            }

            new AlertDialog.Builder(this)
                    .setTitle("Datos Registrados")
                    .setMessage(mensaje)
                    .setPositiveButton("Cerrar", null)
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se pudieron cargar los datos o el archivo no existe.")
                    .setPositiveButton("Aceptar", null)
                    .show();
        }
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

    public void guardarBD(String cedula, String nombres, String apellidos, String edad, String fechaNac, String nacionalidad, String genero,
                          String estadoCivil,String usuario, String contrasenia, float nivelIng){
        BaseDatosSQLite dbControlMaterial = new BaseDatosSQLite(this);
        final SQLiteDatabase dbControlMaterialModoWrite = dbControlMaterial.getWritableDatabase();

        if(dbControlMaterialModoWrite != null){
            ContentValues cv = new ContentValues();
            cv.put("cedula", cedula);
            cv.put("nombre", nombres);
            cv.put("apellido", apellidos);
            cv.put("edad", edad);
            cv.put("nacionalidad", nacionalidad);
            cv.put("genero", genero);
            cv.put("fechaNac", fechaNac);
            cv.put("estadoCivil", estadoCivil);
            cv.put("username", usuario);
            cv.put("password", contrasenia);
            cv.put("nivelIngles", nivelIng);

            long resultado = dbControlMaterialModoWrite.insert("usuario", null, cv);
            Toast.makeText(this, "Se ha guardado los datos en la BD", Toast.LENGTH_LONG).show();
            Log.d("SQLITE", "ID INSERTADO: " + resultado);
        }
        //dbControlMaterialModoWrite.close();

    }


}