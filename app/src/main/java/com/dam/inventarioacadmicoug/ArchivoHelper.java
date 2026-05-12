package com.dam.inventarioacadmicoug;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class ArchivoHelper {

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

            return context.getString(R.string.archivo_interno_ok);

        } catch (Exception e) {

            e.printStackTrace();

            return context.getString(R.string.archivo_interno_error);
        }
    }

    public static String exportarReporteExterno(
            Context context,
            String contenido
    ) {

        try {

            File carpeta = context.getExternalFilesDir(null);

            if (carpeta == null) {
                return context.getString(R.string.memoria_externa_error);
            }

            File archivo = new File(
                    carpeta,
                    "reporte_inventario.txt"
            );

            FileOutputStream fos =
                    new FileOutputStream(archivo);

            fos.write(contenido.getBytes());
            fos.close();

            return context.getString(R.string.reporte_exportado_ok);

        } catch (Exception e) {

            e.printStackTrace();

            return context.getString(R.string.reporte_exportado_error);
        }
    }

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
                            context.getString(R.string.compartir_reporte_titulo)
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}