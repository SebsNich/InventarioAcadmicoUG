package com.dam.inventarioacadmicoug;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DetalleItemActivity extends AppCompatActivity {

    private static final int REQUEST_EDITAR = 1;

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

        tvNombre      = findViewById(R.id.tvNombre);
        tvCategoria   = findViewById(R.id.tvCategoria);
        tvCantidad    = findViewById(R.id.tvCantidad);
        tvUbicacion   = findViewById(R.id.tvUbicacion);
        tvObservacion = findViewById(R.id.tvObservacion);
        tvFecha       = findViewById(R.id.tvFecha);

        btnEditar         = findViewById(R.id.btnEditar);
        btnEliminar       = findViewById(R.id.btnEliminar);
        btnVolver         = findViewById(R.id.btnVolver);
        btnGuardarInterno = findViewById(R.id.btnGuardarInterno);
        btnExportar       = findViewById(R.id.btnExportar);
        btnCompartir      = findViewById(R.id.btnCompartir);

        dbHelper = new InventarioDbHelper(this);

        // Cargar datos del Intent
        Intent intent = getIntent();
        idItem = intent.getIntExtra("id", -1);

        cargarDatosEnVista(
                intent.getStringExtra("nombre"),
                intent.getStringExtra("categoria"),
                intent.getIntExtra("cantidad", 0),
                intent.getStringExtra("ubicacion"),
                intent.getStringExtra("observacion"),
                intent.getStringExtra("fecha")
        );

        btnVolver.setOnClickListener(v -> finish());

        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());

        // Editar — usa startActivityForResult para refrescar al volver
        btnEditar.setOnClickListener(v -> {
            Intent editarIntent = new Intent(
                    DetalleItemActivity.this,
                    FormItemActivity.class
            );
            editarIntent.putExtra("id", idItem);
            editarIntent.putExtra("nombre",
                    tvNombre.getText().toString()
                            .replace(getString(R.string.txt_nombre) + " ", ""));
            editarIntent.putExtra("categoria",
                    tvCategoria.getText().toString()
                            .replace(getString(R.string.txt_categoria) + " ", ""));
            editarIntent.putExtra("cantidad",
                    intent.getIntExtra("cantidad", 0));
            editarIntent.putExtra("ubicacion",
                    tvUbicacion.getText().toString()
                            .replace(getString(R.string.txt_ubicacion) + " ", ""));
            editarIntent.putExtra("observacion",
                    tvObservacion.getText().toString()
                            .replace(getString(R.string.txt_observacion) + " ", ""));

            startActivityForResult(editarIntent, REQUEST_EDITAR);
        });

        // Botones de reporte
        btnGuardarInterno.setOnClickListener(v -> {
            String resultado = ArchivoHelper.guardarResumenInterno(
                    this, construirReporte());
            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
        });

        btnExportar.setOnClickListener(v -> {
            String resultado = ArchivoHelper.exportarReporteExterno(
                    this, construirReporte());
            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
        });

        btnCompartir.setOnClickListener(v ->
                ArchivoHelper.compartirReporte(this));
    }

    // Cuando regresa de FormItemActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDITAR && resultCode == RESULT_OK) {
            // Recargar datos actualizados desde la base de datos
            List<ItemInventario> items = dbHelper.obtenerTodosLosItems();
            for (ItemInventario item : items) {
                if (item.getId() == idItem) {
                    cargarDatosEnVista(
                            item.getNombre(),
                            item.getCategoria(),
                            item.getCantidad(),
                            item.getUbicacion(),
                            item.getObservacion(),
                            item.getFechaRegistro()
                    );
                    break;
                }
            }
        }
    }

    // Método reutilizable para mostrar datos en la vista
    private void cargarDatosEnVista(String nombre, String categoria,
                                    int cantidad, String ubicacion, String observacion, String fecha) {
        tvNombre.setText(getString(R.string.txt_nombre) + " " + nombre);
        tvCategoria.setText(getString(R.string.txt_categoria) + " " + categoria);
        tvCantidad.setText(getString(R.string.txt_cantidad) + " " + cantidad);
        tvUbicacion.setText(getString(R.string.txt_ubicacion) + " " + ubicacion);
        tvObservacion.setText(getString(R.string.txt_observacion) + " " + observacion);
        tvFecha.setText(getString(R.string.txt_fecha) + " " + fecha);
    }

    private String construirReporte() {
        return getString(R.string.reporte_titulo) + "\n\n" +
                tvNombre.getText().toString() + "\n" +
                tvCategoria.getText().toString() + "\n" +
                tvCantidad.getText().toString() + "\n" +
                tvUbicacion.getText().toString() + "\n" +
                tvObservacion.getText().toString() + "\n" +
                tvFecha.getText().toString();
    }

    private void mostrarDialogoEliminar() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialogo_eliminar_titulo))
                .setMessage(getString(R.string.dialogo_eliminar_mensaje))
                .setPositiveButton(getString(R.string.opcion_si), (dialog, which) -> {
                    dbHelper.eliminarItem(idItem);
                    Toast.makeText(this,
                            getString(R.string.toast_eliminado),
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(getString(R.string.opcion_no), null)
                .show();
    }
}