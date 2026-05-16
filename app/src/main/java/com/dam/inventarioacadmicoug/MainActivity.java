package com.dam.inventarioacadmicoug;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NOMBRE = "prefs_inventario";
    public static final String KEY_NOMBRE   = "nombre_estudiante";
    public static final String KEY_PARALELO = "paralelo";
    public static final String KEY_SALUDO   = "mostrar_saludo";

    private TextView tvSaludo;
    private View btnRegistrar, btnListar, btnPreferencias, btnReporte, btnWebUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvSaludo        = findViewById(R.id.tvSaludo);
        btnRegistrar    = findViewById(R.id.btnRegistrar);
        btnListar       = findViewById(R.id.btnListar);
        btnPreferencias = findViewById(R.id.btnPreferencias);
        btnReporte      = findViewById(R.id.btnReporte);
        btnWebUG        = findViewById(R.id.btnWebUG);

        btnRegistrar.setOnClickListener(v -> abrirRegistro());
        btnListar.setOnClickListener(v -> abrirLista());
        btnPreferencias.setOnClickListener(v -> abrirPreferencias());
        btnReporte.setOnClickListener(v -> generarReporte());
        btnWebUG.setOnClickListener(v -> abrirWebUG());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarSaludo();
    }

    private void cargarSaludo() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
        boolean mostrarSaludo = prefs.getBoolean(KEY_SALUDO, true);

        if (mostrarSaludo) {
            String nombre   = prefs.getString(KEY_NOMBRE, "");
            String paralelo = prefs.getString(KEY_PARALELO, "");
            if (!nombre.isEmpty()) {
                String saludo = "Hola, " + nombre;
                if (!paralelo.isEmpty()) saludo += "  |  " + paralelo;
                tvSaludo.setText(saludo);
            } else {
                tvSaludo.setText(R.string.saludo_default);
            }
        } else {
            tvSaludo.setText("");
        }
    }

    private void generarReporte() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
            String nombreEstudiante = prefs.getString(KEY_NOMBRE, "No especificado");
            String paralelo         = prefs.getString(KEY_PARALELO, "No especificado");

            InventarioDbHelper dbHelper = new InventarioDbHelper(this);
            List<ItemInventario> items = dbHelper.obtenerTodosLosItems();

            String fecha = new SimpleDateFormat(
                    "dd/MM/yyyy HH:mm", Locale.getDefault()
            ).format(new Date());

            // Construir texto del reporte
            StringBuilder sb = new StringBuilder();
            sb.append("===================================\n");
            sb.append("   REPORTE DE INVENTARIO ACADÉMICO UG    \n");
            sb.append("===================================\n\n");
            sb.append("Responsable : ").append(nombreEstudiante).append("\n");
            sb.append("Paralelo    : ").append(paralelo).append("\n");
            sb.append("Fecha       : ").append(fecha).append("\n");
            sb.append("Total items : ").append(items.size()).append("\n");
            sb.append("\n------------------------------------------\n");
            sb.append("  DETALLE DEL INVENTARIO\n");
            sb.append("------------------------------------------\n\n");

            if (items.isEmpty()) {
                sb.append("No hay elementos registrados.\n");
            } else {
                int numero = 1;
                for (ItemInventario item : items) {
                    sb.append(numero).append(". ").append(item.getNombre()).append("\n");
                    sb.append("   Categoría  : ").append(item.getCategoria()).append("\n");
                    sb.append("   Cantidad   : ").append(item.getCantidad()).append("\n");
                    sb.append("   Ubicación  : ").append(
                            item.getUbicacion() != null && !item.getUbicacion().isEmpty()
                                    ? item.getUbicacion() : "—").append("\n");
                    sb.append("   Observación: ").append(
                            item.getObservacion() != null && !item.getObservacion().isEmpty()
                                    ? item.getObservacion() : "—").append("\n");
                    sb.append("   Fecha reg. : ").append(item.getFechaRegistro()).append("\n\n");
                    numero++;
                }
            }

            sb.append("===================================\n");
            sb.append("  Universidad de Guayaquil - Carrera SW  \n");
            sb.append("===================================\n");

            String textoReporte = sb.toString();

            ArchivoHelper.guardarResumenInterno(this, textoReporte);

            ArchivoHelper.exportarReporteExterno(this, textoReporte);

            ArchivoHelper.compartirReporte(this);

        } catch (Exception e) {
            Toast.makeText(this,
                    getString(R.string.msg_error_reporte),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirRegistro() {
        startActivity(new Intent(this, FormItemActivity.class));
    }

    private void abrirLista() {
        startActivity(new Intent(this, ListaItemsActivity.class));
    }

    private void abrirPreferencias() {
        startActivity(new Intent(this, PreferenciasActivity.class));
    }

    private void abrirWebUG() {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.ug.edu.ec")));
    }
}