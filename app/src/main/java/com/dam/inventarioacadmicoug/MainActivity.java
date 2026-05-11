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

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NOMBRE = "prefs_inventario";
    public static final String KEY_NOMBRE   = "nombre_estudiante";
    public static final String KEY_PARALELO = "paralelo";
    public static final String KEY_SALUDO   = "mostrar_saludo";

    private TextView tvSaludo;

    // Ahora son View porque en el XML son LinearLayout, no Button
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

        // Vincular vistas
        tvSaludo        = findViewById(R.id.tvSaludo);
        btnRegistrar    = findViewById(R.id.btnRegistrar);
        btnListar       = findViewById(R.id.btnListar);
        btnPreferencias = findViewById(R.id.btnPreferencias);
        btnReporte      = findViewById(R.id.btnReporte);
        btnWebUG        = findViewById(R.id.btnWebUG);

        // Listeners (igual que antes, solo cambia el tipo de v)
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

    private void abrirRegistro() {
        startActivity(new Intent(this, FormItemActivity.class));
    }

    private void abrirLista() {
        startActivity(new Intent(this, ListaItemsActivity.class));
    }

    private void abrirPreferencias() {
        startActivity(new Intent(this, PreferenciasActivity.class));
    }

    private void generarReporte() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
            String nombreEstudiante = prefs.getString(KEY_NOMBRE, "Estudiante");

            InventarioDbHelper dbHelper = new InventarioDbHelper(this);
            java.util.List<ItemInventario> items = dbHelper.obtenerTodosLosItems();

            StringBuilder reporte = new StringBuilder();
            reporte.append("=== REPORTE DE INVENTARIO ACADÉMICO UG ===\n");
            reporte.append("Responsable: ").append(nombreEstudiante).append("\n");
            reporte.append("Fecha: ").append(
                    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",
                            java.util.Locale.getDefault()).format(new java.util.Date())
            ).append("\n");
            reporte.append("Total de elementos: ").append(items.size()).append("\n");
            reporte.append("------------------------------------------\n");
            for (ItemInventario item : items) {
                reporte.append("• ").append(item.getNombre())
                        .append(" | ").append(item.getCategoria())
                        .append(" | Cant: ").append(item.getCantidad()).append("\n");
            }

            // TODO: descomentar cuando Kleber termine ArchivoHelper
            // ArchivoHelper archivoHelper = new ArchivoHelper(this);
            // archivoHelper.guardarArchivoInterno(reporte.toString());
            // archivoHelper.guardarArchivoExterno(reporte.toString());

            Toast.makeText(this, getString(R.string.msg_reporte_generado), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.msg_error_reporte), Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirWebUG() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ug.edu.ec")));
    }
}

