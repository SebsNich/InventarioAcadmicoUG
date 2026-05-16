package com.dam.inventarioacadmicoug;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PreferenciasActivity extends AppCompatActivity {

    // EditTexts y controles
    private EditText etNombreEstudiante;
    private EditText etParalelo;
    private androidx.appcompat.widget.SwitchCompat switchSaludo;
    private Button   btnGuardarPreferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        etNombreEstudiante    = findViewById(R.id.etNombreEstudiante);
        etParalelo            = findViewById(R.id.etParalelo);
        switchSaludo          = findViewById(R.id.switchSaludo);
        btnGuardarPreferencias = findViewById(R.id.btnGuardarPreferencias);

        cargarPreferencias();

        btnGuardarPreferencias.setOnClickListener(v -> guardarPreferencias());
    }


    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.PREFS_NOMBRE, MODE_PRIVATE);

        String nombre   = prefs.getString(MainActivity.KEY_NOMBRE, "");
        String paralelo = prefs.getString(MainActivity.KEY_PARALELO, "");
        boolean saludo  = prefs.getBoolean(MainActivity.KEY_SALUDO, true);

        etNombreEstudiante.setText(nombre);
        etParalelo.setText(paralelo);
        switchSaludo.setChecked(saludo);
    }


    private void guardarPreferencias() {
        String nombre   = etNombreEstudiante.getText().toString().trim();
        String paralelo = etParalelo.getText().toString().trim();
        boolean saludo  = switchSaludo.isChecked();

        if (nombre.isEmpty()) {
            etNombreEstudiante.setError(getString(R.string.msg_nombre_requerido));
            etNombreEstudiante.requestFocus();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(
                MainActivity.PREFS_NOMBRE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MainActivity.KEY_NOMBRE, nombre);
        editor.putString(MainActivity.KEY_PARALELO, paralelo);
        editor.putBoolean(MainActivity.KEY_SALUDO, saludo);
        editor.apply();  // apply() es asíncrono y más eficiente que commit()

        Toast.makeText(this, getString(R.string.msg_preferencias_guardadas),
                Toast.LENGTH_SHORT).show();

        finish();
    }
}