package com.dam.inventarioacadmicoug;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleItemActivity extends AppCompatActivity {

    private TextView tvNombre, tvCategoria, tvCantidad,
            tvUbicacion, tvObservacion, tvFecha;

    private Button btnEditar, btnEliminar, btnVolver;

    private InventarioDbHelper dbHelper;

    private int idItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item);

        tvNombre = findViewById(R.id.tvNombre);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvCantidad = findViewById(R.id.tvCantidad);
        tvUbicacion = findViewById(R.id.tvUbicacion);
        tvObservacion = findViewById(R.id.tvObservacion);
        tvFecha = findViewById(R.id.tvFecha);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);

        dbHelper = new InventarioDbHelper(this);

        Intent intent = getIntent();

        idItem = intent.getIntExtra("id", -1);

        String nombre = intent.getStringExtra("nombre");
        String categoria = intent.getStringExtra("categoria");
        int cantidad = intent.getIntExtra("cantidad", 0);
        String ubicacion = intent.getStringExtra("ubicacion");
        String observacion = intent.getStringExtra("observacion");
        String fecha = intent.getStringExtra("fecha");

        tvNombre.setText("Nombre: " + nombre);
        tvCategoria.setText("Categoría: " + categoria);
        tvCantidad.setText("Cantidad: " + cantidad);
        tvUbicacion.setText("Ubicación: " + ubicacion);
        tvObservacion.setText("Observación: " + observacion);
        tvFecha.setText("Fecha: " + fecha);

        btnVolver.setOnClickListener(v -> finish());

        btnEditar.setOnClickListener(v -> {

            Intent editarIntent = new Intent(
                    DetalleItemActivity.this,
                    FormItemActivity.class
            );

            editarIntent.putExtra("id", idItem);
            editarIntent.putExtra("nombre", nombre);
            editarIntent.putExtra("categoria", categoria);
            editarIntent.putExtra("cantidad", cantidad);
            editarIntent.putExtra("ubicacion", ubicacion);
            editarIntent.putExtra("observacion", observacion);

            startActivity(editarIntent);

        });

        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());

    }

    private void mostrarDialogoEliminar() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Confirmar eliminación");

        builder.setMessage("¿Desea eliminar este elemento?");

        builder.setPositiveButton("Sí", (dialog, which) -> {

            dbHelper.eliminarItem(idItem);

            Toast.makeText(
                    this,
                    "Elemento eliminado",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        });

        builder.setNegativeButton("No", null);

        builder.show();
    }
}