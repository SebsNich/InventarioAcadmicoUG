package com.dam.inventarioacadmicoug;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class ArchivoHelper {

    // =========================
    // GUARDAR EN MEMORIA INTERNA
    // =========================
    public static String guardarResumenInterno(
            Context context,
            String contenido
    ) {

        try {

            FileOutputStream fos = context.openFileOutput(
                    "resumen_interno.txt",
                    Context.MODE_PRIVATE
            );

            fos.write(contenido.getBytes());

            fos.close();

            return "Archivo interno guardado correctamente";

        } catch (Exception e) {

            e.printStackTrace();

            return "Error al guardar archivo interno";
        }
    }


    // =========================
    // EXPORTAR REPORTE
    // =========================
    public static String exportarReporteExterno(
            Context context,
            String contenido
    ) {

        try {

            File carpeta = context.getExternalFilesDir(null);

            if (carpeta == null) {
                return "No existe memoria externa";
            }

            File archivo = new File(
                    carpeta,
                    "reporte_inventario.txt"
            );

            FileOutputStream fos =
                    new FileOutputStream(archivo);

            fos.write(contenido.getBytes());

            fos.close();

            return "Reporte exportado correctamente";

        } catch (Exception e) {

            e.printStackTrace();

            return "Error al exportar reporte";
        }
    }


    // =========================
    // COMPARTIR REPORTE
    // =========================
    public static void compartirReporte(Context context) {

        try {

            File carpeta = context.getExternalFilesDir(null);

            if (carpeta == null) {
                return;
            }

            File archivo = new File(
                    carpeta,
                    "reporte_inventario.txt"
            );

            Uri uri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    archivo
            );

            Intent intent =
                    new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");

            intent.putExtra(
                    Intent.EXTRA_STREAM,
                    uri
            );

            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            context.startActivity(
                    Intent.createChooser(
                            intent,
                            "Compartir Reporte"
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}