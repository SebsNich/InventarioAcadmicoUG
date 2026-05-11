package com.dam.inventarioacadmicoug;
import android.app.AlertDialog;
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
    private Button btnGuardarInterno;
    private Button btnExportar;
    private Button btnCompartir;
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
        btnGuardarInterno = findViewById(R.id.btnGuardarInterno);
        btnExportar = findViewById(R.id.btnExportar);
        btnCompartir = findViewById(R.id.btnCompartir);

        dbHelper = new InventarioDbHelper(this);

        Intent intent = getIntent();

        idItem = intent.getIntExtra("id", -1);

        String nombre = intent.getStringExtra("nombre");
        String categoria = intent.getStringExtra("categoria");
        int cantidad = intent.getIntExtra("cantidad", 0);
        String ubicacion = intent.getStringExtra("ubicacion");
        String observacion = intent.getStringExtra("observacion");
        String fecha = intent.getStringExtra("fecha");
        String reporte =
                "REPORTE INVENTARIO\n\n" +
                        "Nombre: " + nombre + "\n" +
                        "Categoría: " + categoria + "\n" +
                        "Cantidad: " + cantidad + "\n" +
                        "Ubicación: " + ubicacion + "\n" +
                        "Observación: " + observacion + "\n" +
                        "Fecha: " + fecha;

        tvNombre.setText(getString(R.string.txt_nombre) + " " + nombre);

        tvCategoria.setText(
                getString(R.string.txt_categoria) + " " + categoria
        );

        tvCantidad.setText(
                getString(R.string.txt_cantidad) + " " + cantidad
        );

        tvUbicacion.setText(
                getString(R.string.txt_ubicacion) + " " + ubicacion
        );

        tvObservacion.setText(
                getString(R.string.txt_observacion) + " " + observacion
        );

        tvFecha.setText(
                getString(R.string.txt_fecha) + " " + fecha
        );

        btnVolver.setOnClickListener(v -> finish());
        btnGuardarInterno.setOnClickListener(v -> {

            String mensaje =
                    ArchivoHelper.guardarResumenInterno(
                            this,
                            reporte
                    );

            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
        });


        btnExportar.setOnClickListener(v -> {

            String mensaje =
                    ArchivoHelper.exportarReporteExterno(
                            this,
                            reporte
                    );

            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
        });


        btnCompartir.setOnClickListener(v -> {

            ArchivoHelper.compartirReporte(this);

        });

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

        builder.setTitle(
                getString(R.string.dialogo_eliminar_titulo)
        );

        builder.setMessage(
                getString(R.string.dialogo_eliminar_mensaje)
        );

        builder.setPositiveButton("Sí", (dialog, which) -> {

            dbHelper.eliminarItem(idItem);

            Toast.makeText(
                    this,
                    getString(R.string.toast_eliminado),
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        });

        builder.setNegativeButton("No", null);

        builder.show();
    }
}