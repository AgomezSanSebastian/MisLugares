package com.example.mislugares.presentacion

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mislugares.Aplicacion
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsosLugar
import com.example.mislugares.modelo.Lugar
import kotlinx.android.synthetic.main.vista_lugar.*
import java.text.DateFormat
import java.util.*

class VistaLugarActivity : AppCompatActivity() {
    val lugares by lazy { (application as Aplicacion).lugares }
    val adaptador by lazy { (application as Aplicacion).adaptador }
    val usoLugar by lazy { CasosUsosLugar(this, lugares, adaptador) }
    var pos = 0
    val RESULTADO_EDITAR = 1
    val RESULTADO_GALERIA = 2 //poner antes de la clase
    val RESULTADO_FOTO = 3
    private var _id : Int = -1
    private lateinit var uriUltimaFoto: Uri


    lateinit var lugar: Lugar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_lugar)
        pos = intent.extras?.getInt("pos", 0) ?: 0
        _id = adaptador.idPosicion(pos)
        lugar = adaptador.lugarPosicion (pos)
        actualizaVistas()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.vista_lugar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.accion_compartir ->{
                usoLugar.compartir(lugar)
                return true
            }
            R.id.accion_llegar -> {
                usoLugar.verMapa(lugar)
                return true
            }
            R.id.accion_editar -> return true
            R.id.accion_borrar -> {
                val _id = adaptador.idPosicion(pos)
                usoLugar.borrar(_id)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun actualizaVistas(){
        nombre.text = lugar.nombre
        logo_tipo.setImageResource(lugar.tipoLugar.recurso)
        tipo.text = lugar.tipoLugar.texto
        direccion.text = lugar.direccion
        telefono.text = Integer.toString(lugar.telefono)
        url.text = lugar.url
        comentario.text = lugar.comentarios
        fecha.text = DateFormat.getDateInstance().format(Date(lugar.fecha))
        hora.text = DateFormat.getTimeInstance().format(Date(lugar.fecha))
        valoracion.rating = lugar.valoracion
        valoracion.setOnRatingBarChangeListener { _, _, _ -> }
        valoracion.setRating(lugar.valoracion)
        valoracion.setOnRatingBarChangeListener {  _, valor, _ ->
            lugar.valoracion = valor
            usoLugar.actualizaPosLugar(pos, lugar)
            pos = adaptador.posicionId(_id)
        }
        usoLugar.visualizarFoto(lugar, foto);
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (requestCode == RESULTADO_EDITAR) {
            lugar = lugares.elemento(_id)
            pos = adaptador.posicionId(_id)
            actualizaVistas()
            actualizaVistas()
            scrollView1.invalidate()
        } else if (requestCode == RESULTADO_GALERIA) {
            if (resultCode == RESULT_OK) {
                usoLugar.ponerFoto(pos, data?.dataString ?: "", foto)
            } else {
                Toast.makeText(this, "Foto no cargada", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_FOTO) {
            if (resultCode == Activity.RESULT_OK && uriUltimaFoto != null) {
                lugar.foto = uriUltimaFoto.toString()
                usoLugar.ponerFoto(pos, lugar.foto, foto);
            } else {
                Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun verMapa(view: View) = usoLugar.verMapa(lugar)
    fun llamarTelefono(view: View) = usoLugar.llamarTelefono(lugar)
    fun verPgWeb(view: View) = usoLugar.verPgWeb(lugar)
    fun ponerDeGaleria(view: View) = usoLugar.ponerDeGaleria(RESULTADO_GALERIA)
    fun tomarFoto(view: View) {
        uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO)!!
    }
    fun eliminarFoto(view: View) = usoLugar.ponerFoto(pos, "", foto)
}