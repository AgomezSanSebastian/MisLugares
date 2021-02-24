package com.example.mislugares.presentacion

import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsosLugar
import com.example.mislugares.datos.LugaresLista
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar
import kotlinx.android.synthetic.main.edicion_lugar.*


class EdicionLugarActivity : AppCompatActivity(){
    val lugares = LugaresLista()
    var pos = 0
    lateinit var lugar: Lugar
    val usoLugar by lazy { CasosUsosLugar(this, lugares) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edicion_lugar)

        val adaptador = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                lugar.tipoLugar.getNombres()
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipo.adapter = adaptador
        tipo.setSelection(lugar.tipoLugar.ordinal)

        lugares.añadeEjemplos()
        pos = intent.extras?.getInt("pos", 0) ?: 0
        lugar = lugares.elemento(pos)

        actualizaVistas()
    }


    fun actualizaVistas(){
        nombre.setText(lugar.nombre)
        direccion.setText(lugar.direccion)
        telefono.setText (Integer.toString(lugar.telefono))
        url.setText(lugar.url)
        comentario.setText(lugar.comentarios)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edicion_lugar, menu)
        return true
    }


    fun onOptionsItemSelected() {
        val nuevoLugar = Lugar(nombre.text.toString(), direccion.text.toString(),
                lugar.posicion, TipoLugar.values()[tipo.selectedItemPosition],
                lugar.foto, Integer.parseInt(telefono.text.toString()),
                url.text.toString(), comentario.text.toString(),
                lugar.fecha, lugar.valoracion )
        usoLugar.guardar(pos, nuevoLugar)
        finish()

    }
}
