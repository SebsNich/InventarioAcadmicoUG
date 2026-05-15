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

    // 1. Declarar Helper de la Base de Datos
    private InventarioDbHelper dbHelper;

    // Variable :: Se esta editando (-1) o no
    private int idItemEditar = -1;

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

        // VAlidar si el intent trae datos
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            idItemEditar = intent.getIntExtra("id", -1);

            if (idItemEditar != -1) {
                // Llenar los campos con los datos existentes
                etNombre.setText(intent.getStringExtra("nombre"));
                etCategoria.setText(intent.getStringExtra("categoria"));
                etCantidad.setText(String.valueOf(intent.getIntExtra("cantidad", 0)));
                etUbicacion.setText(intent.getStringExtra("ubicacion"));
                etObservacion.setText(intent.getStringExtra("observacion"));

                // Cambiar el texto del botón para que sea intuitivo
                btnGuardar.setText("Actualizar Registro");
            }
        }

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

        // Validación de nueva inserción o edición
        if (idItemEditar == -1) {
            // Modo Crear Nuevo
            // 3. Generar la fecha actual para el registro
            String fechaRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

            // 4. Guardar en SQLite
            long resultado = dbHelper.insertarItem(nombre, categoria, cantidad, ubicacion, observacion, fechaRegistro);

            if (resultado != -1) {
                confirmarGuardado("Elemento registrado correctamente");
            } else {
                Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Modo Actualizar
            // Usamos el método actualizarItem de la base de datos
            int filasAfectadas = dbHelper.actualizarItem(idItemEditar, nombre, categoria, cantidad, ubicacion, observacion);

            if (filasAfectadas > 0) {
                confirmarGuardado("Elemento actualizado correctamente");
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmarGuardado(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListaItemsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }
}