package com.dam.inventarioacadmicoug;

import android.content.Intent;
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
    private int idItem = -1;
    // 1. Declarar Helper de la Base de Datos
    private InventarioDbHelper dbHelper;

    // Variable :: Se esta editando (-1) o no
    private int idItemEditar = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_item);

        // Vincular vistas
        etNombre      = findViewById(R.id.etNombre);
        etCategoria   = findViewById(R.id.etCategoria);
        etCantidad    = findViewById(R.id.etCantidad);
        etUbicacion   = findViewById(R.id.etUbicacion);
        etObservacion = findViewById(R.id.etObservacion);
        btnGuardar    = findViewById(R.id.btnGuardar);



        // Leer Intent — si viene con datos es modo EDITAR
        Intent intent = getIntent();
        idItem = intent.getIntExtra("id", -1);

        if (idItem != -1) {
            // Modo editar — cargar datos en los campos
            etNombre.setText(intent.getStringExtra("nombre"));
            etCategoria.setText(intent.getStringExtra("categoria"));
            etCantidad.setText(String.valueOf(intent.getIntExtra("cantidad", 0)));
            etUbicacion.setText(intent.getStringExtra("ubicacion"));
            etObservacion.setText(intent.getStringExtra("observacion"));
        }

        btnGuardar.setOnClickListener(v -> guardarItem());
    }

    private void guardarItem() {
        String nombre   = etNombre.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String observacion = etObservacion.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            etNombre.setError(getString(R.string.err_obligatorio));
            return;
        }
        if (categoria.isEmpty()) {
            etCategoria.setError(getString(R.string.err_obligatorio));
            return;
        }
        if (cantidadStr.isEmpty()) {
            etCantidad.setError(getString(R.string.err_obligatorio));
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                etCantidad.setError(getString(R.string.err_cantidad));
                return;
            }
        } catch (NumberFormatException e) {
            etCantidad.setError(getString(R.string.err_numero_invalido));
            return;
        }

        InventarioDbHelper dbHelper = new InventarioDbHelper(this);

        if (idItem == -1) {
            // INSERTAR nuevo
            String fecha = new java.text.SimpleDateFormat(
                    "dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                    .format(new java.util.Date());
            dbHelper.insertarItem(nombre, categoria, cantidad,
                    ubicacion, observacion, fecha);
            Toast.makeText(this, getString(R.string.msg_guardado_exito),
                    Toast.LENGTH_SHORT).show();
        } else {
            // ACTUALIZAR existente
            dbHelper.actualizarItem(idItem, nombre, categoria,
                    cantidad, ubicacion, observacion);
            Toast.makeText(this, "Elemento actualizado correctamente",
                    Toast.LENGTH_SHORT).show();
        }
        setResult(RESULT_OK);
        finish(); // Volver a la lista

    }

    private void confirmarGuardado(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListaItemsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


        finish();
    }
}