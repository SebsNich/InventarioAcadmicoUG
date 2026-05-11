package com.dam.inventarioacadmicoug;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormItemActivity extends AppCompatActivity {

    private EditText etNombre, etCategoria, etCantidad, etUbicacion, etObservacion;
    private Button btnGuardar;

    // 1. Declarar el Helper de la Base de Datos
    private InventarioDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_item);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etCategoria = findViewById(R.id.etCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        etUbicacion = findViewById(R.id.etUbicacion);
        etObservacion = findViewById(R.id.etObservacion);
        btnGuardar = findViewById(R.id.btnGuardar);

        // 2. Inicializar la base de datos
        dbHelper = new InventarioDbHelper(this);

        btnGuardar.setOnClickListener(v -> guardarItem());
    }

    private void guardarItem() {
        // Obtener textos y limpiar espacios en blanco
        String nombre = etNombre.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String cantidadTxt = etCantidad.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String observacion = etObservacion.getText().toString().trim();

        // Validar nombre obligatorio
        if (nombre.isEmpty()) {
            etNombre.setError(getString(R.string.err_obligatorio));
            return;
        }

        // Validar categoría obligatoria
        if (categoria.isEmpty()) {
            etCategoria.setError(getString(R.string.err_obligatorio));
            return;
        }

        // Validar cantidad
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTxt);
            if (cantidad <= 0) {
                etCantidad.setError(getString(R.string.err_cantidad));
                return;
            }
        } catch (NumberFormatException e) {
            etCantidad.setError(getString(R.string.err_numero_invalido));
            return;
        }

        // 3. Generar la fecha actual para el registro
        String fechaRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // 4. Guardar en SQLite llamando a tu Backend
        long resultado = dbHelper.insertarItem(nombre, categoria, cantidad, ubicacion, observacion, fechaRegistro);

        if (resultado != -1) {
            confirmarGuardado();
        } else {
            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarGuardado() {
        Toast.makeText(this, R.string.msg_guardado_exito, Toast.LENGTH_SHORT).show();
        finish(); // Regresa a la pantalla anterior
    }
}