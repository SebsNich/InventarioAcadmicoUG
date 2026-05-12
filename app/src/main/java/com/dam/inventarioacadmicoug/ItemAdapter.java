package com.dam.inventarioacadmicoug;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<ItemInventario> {

    // Constructor: recibe el contexto y la lista de items
    public ItemAdapter(Context context, List<ItemInventario> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutilizar vistas para mejor rendimiento
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_lista, parent, false);
        }

        // Obtener el item en esta posición
        ItemInventario item = getItem(position);

        // Conectar los TextView del XML con los datos del objeto
        TextView tvNombre   = convertView.findViewById(R.id.tvNombreItem);
        TextView tvCategoria = convertView.findViewById(R.id.tvCategoriaItem);
        TextView tvCantidad  = convertView.findViewById(R.id.tvCantidadItem);

        tvNombre.setText(item.getNombre());
        tvCategoria.setText("📁  " + item.getCategoria());
        tvCantidad.setText("Cant: " + item.getCantidad());

        return convertView;
    }
}