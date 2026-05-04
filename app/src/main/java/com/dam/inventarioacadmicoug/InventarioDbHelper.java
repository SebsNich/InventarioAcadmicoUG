package com.dam.inventarioacadmicoug;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class InventarioDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventario.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_INVENTARIO = "inventario";

    public InventarioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creación de la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_INVENTARIO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "categoria TEXT NOT NULL, " +
                "cantidad INTEGER NOT NULL, " +
                "ubicacion TEXT, " +
                "observacion TEXT, " +
                "fecha_registro TEXT" +
                ");";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTARIO);
        onCreate(db);
    }

    // CRUD
    // Insertar item de inventario
    public long insertarItem(String nombre, String categoria, int cantidad, String ubicacion, String observacion, String fechaRegistro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("categoria", categoria);
        values.put("cantidad", cantidad);
        values.put("ubicacion", ubicacion);
        values.put("observacion", observacion);
        values.put("fecha_registro", fechaRegistro);

        long id = db.insert(TABLE_INVENTARIO, null, values);
        db.close();
        return id;
    }

    // Seleccionar todos los items de inventario
    public List<ItemInventario> obtenerTodosLosItems() {
        List<ItemInventario> listaItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INVENTARIO, null);

        if (cursor.moveToFirst()) {
            do {
                ItemInventario item = new ItemInventario();
                item.setId(cursor.getInt(0));
                item.setNombre(cursor.getString(1));
                item.setCategoria(cursor.getString(2));
                item.setCantidad(cursor.getInt(3));
                item.setUbicacion(cursor.getString(4));
                item.setObservacion(cursor.getString(5));
                item.setFechaRegistro(cursor.getString(6));
                listaItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaItems;
    }

    // Actualizar item de inventario
    public int actualizarItem(int id, String nombre, String categoria, int cantidad, String ubicacion, String observacion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("categoria", categoria);
        values.put("cantidad", cantidad);
        values.put("ubicacion", ubicacion);
        values.put("observacion", observacion);

        int filasAfectadas = db.update(TABLE_INVENTARIO, values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas;
    }

    // Eliminar item de inventario
    public void eliminarItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INVENTARIO, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}