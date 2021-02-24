package com.example.mislugares.casos_uso

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.example.mislugares.datos.RepositorioLugares
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.AdaptadorLugares
import com.example.mislugares.presentacion.EdicionLugarActivity
import com.example.mislugares.presentacion.VistaLugarActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URL

class CasosUsosLugar (val actividad: Activity,
                      val lugares: RepositorioLugares ){
    // OPERACIONES BÁSICAS
    fun mostrar(pos: Int) {
        val i = Intent(actividad, VistaLugarActivity::class.java)
        i.putExtra("pos", pos);
        actividad.startActivity(i);
    }

    fun borrar(id: Int) {
        lugares.borrar(id)
        actividad.finish()
    }

    fun guardar(id: Int, nuevoLugar: Lugar) {
        lugares.actualiza(id, nuevoLugar)
    }

    fun editar(pos: Int, codidoSolicitud: Int) {
        val i = Intent(actividad, EdicionLugarActivity::class.java)
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codidoSolicitud)
    }

    // INTENCIONES
    fun compartir(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$(lugar.nombre) - $(lugar.url)")
        })
    fun llamarTelefono(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.telefono)))
    fun verPgWeb(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse(lugar.url)))
    fun verMapa(lugar: Lugar) {
        val lat = lugar.posicion.latitud
        val lon = lugar.posicion.longitud
        val uri = if (lugar.posicion != GeoPunto.SIN_POSICION)
            Uri.parse("geo:$lat,$lon")
        else Uri.parse("geo:0,0?q=" + lugar.direccion)
        actividad.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    // FOTOGRAFÍAS
    fun ponerDeGaleria(cod : Int) {
        val action= if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
            Intent.ACTION_OPEN_DOCUMENT
        } else {
            Intent.ACTION_PICK
        }
        val i = Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }

        actividad.startActivityForResult(i, cod)
    }

    fun ponerFoto(pos: Int, uri: String?, imageView: ImageView) {
        val lugar = lugares.elemento(pos)
        lugar.foto = uri ?: ""
        visualizarFoto(lugar, imageView)
    }

    fun visualizarFoto(lugar: Lugar, imageView: ImageView) {
        if (!(lugar.foto == null || lugar.foto.isEmpty())) {
            imageView.setImageBitmap(reduceBitmap(actividad, lugar.foto, 1024, 1024))
        } else {
            imageView.setImageBitmap(null)
        }
    }

    fun tomarFoto(codidoSolicitud: Int): Uri? {
        try {
            val file = File.createTempFile(
                "img_" + System.currentTimeMillis() / 1000, ".jpg",
                actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES) )
            val uriUltimaFoto = if (Build.VERSION.SDK_INT >= 24)
                FileProvider.getUriForFile(
                    actividad, "es.upv.jtomas.mislugares.fileProvider", file )
            else Uri.fromFile(file)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto)
            actividad.startActivityForResult(intent, codidoSolicitud)
            return uriUltimaFoto
        } catch (ex: IOException) {
            Toast.makeText(actividad, "Error al crear fichero de imagen",
                Toast.LENGTH_LONG).show()
            return null
        }
    }

    private fun reduceBitmap(contexto: Context, uri: String, maxAncho: Int, maxAlto: Int): Bitmap? {
        try {
            var input: InputStream?
            val u = Uri.parse(uri)
            if (u.scheme == "http" || u.scheme == "https") {
                input = URL(uri).openStream()
            } else {
                input = contexto.getContentResolver().openInputStream(u)
            }
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = Math.max(
                Math.ceil((options.outWidth / maxAncho).toDouble()),
                Math.ceil((options.outHeight / maxAlto).toDouble())
            ).toInt()
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeStream(input, null, options)
        } catch (e: FileNotFoundException) {
            Toast.makeText(
                contexto, "Fichero/recurso de imagen no encontrado",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            Toast.makeText(
                contexto, "Error accediendo a imagen",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return null
        }
    }

}