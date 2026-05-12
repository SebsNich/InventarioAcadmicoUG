package com.dam.inventarioacadmicoug;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ListaItemsActivity extends AppCompatActivity {

    // Variables de la pantalla
    private EditText etBuscar;
    private ListView lvInventario;
    private TextView tvVacio;

    // Variables de datos
    private ItemAdapter adaptador;
    private List<ItemInventario> listaCompleta;   // todos los items de la BD
    private List<ItemInventario> listaFiltrada;   // los que coinciden con la búsqueda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_items);

        // PASO A: Conectar variables con los elementos del XML
        etBuscar     = findViewById(R.id.etBuscar);
        lvInventario = findViewById(R.id.lvInventario);
        tvVacio      = findViewById(R.id.tvVacio);

        // PASO B: Inicializar las listas
        listaCompleta = new ArrayList<>();
        listaFiltrada = new ArrayList<>();

        // PASO C: Crear el adaptador y asignarlo al ListView
        adaptador = new ItemAdapter(this, listaFiltrada);
        lvInventario.setAdapter(adaptador);

        // PASO D: Cargar datos desde la base de datos
        cargarDatos();

        // PASO E: Configurar la búsqueda en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cada vez que el usuario escribe, filtrar la lista
                filtrarLista(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // PASO F: Al tocar un item, abrir la pantalla de detalles (de Denisse)
        lvInventario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemInventario itemSeleccionado = listaFiltrada.get(position);

                // Enviar el ID del item a DetalleItemActivity
                Intent intent = new Intent(ListaItemsActivity.this, DetalleItemActivity.class);
                intent.putExtra("item_id", itemSeleccionado.getId());
                startActivity(intent);
            }
        });
    }

    // Se ejecuta cada vez que vuelves a esta pantalla (por si se agregó o eliminó algo)
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    // Función para leer todos los items de la base de datos
    private void cargarDatos() {
        InventarioDbHelper dbHelper = new InventarioDbHelper(this);
        listaCompleta = dbHelper.obtenerTodosLosItems(); // método de SebastiánN

        // Mostrar todo al cargar
        filtrarLista(etBuscar.getText().toString());
    }

    // Función que filtra por nombre O categoría
    private void filtrarLista(String texto) {
        listaFiltrada.clear(); // limpiar la lista filtrada

        String textoBusqueda = texto.toLowerCase().trim();

        if (textoBusqueda.isEmpty()) {
            // Si no hay búsqueda, mostrar todo
            listaFiltrada.addAll(listaCompleta);
        } else {
            // Recorrer todos los items y guardar los que coincidan
            for (ItemInventario item : listaCompleta) {
                boolean nombreCoincide    = item.getNombre().toLowerCase().contains(textoBusqueda);
                boolean categoriaCoincide = item.getCategoria().toLowerCase().contains(textoBusqueda);

                if (nombreCoincide || categoriaCoincide) {
                    listaFiltrada.add(item);
                }
            }
        }

        // Notificar al adaptador que los datos cambiaron
        adaptador.notifyDataSetChanged();

        // Mostrar mensaje si no hay resultados
        if (listaFiltrada.isEmpty()) {
            tvVacio.setVisibility(View.VISIBLE);
            lvInventario.setVisibility(View.GONE);
        } else {
            tvVacio.setVisibility(View.GONE);
            lvInventario.setVisibility(View.VISIBLE);
        }
    }
}