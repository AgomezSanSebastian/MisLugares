package com.example.mislugares

import android.app.Application
import com.example.mislugares.datos.AdaptadorLugaresBD
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.datos.LugaresLista
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.presentacion.AdaptadorLugares

class Aplicacion : Application() {

    val lugares = LugaresBD(this)
    val adaptador by lazy { AdaptadorLugaresBD(lugares, lugares.extraeCursor())  }
    val posicionActual = GeoPunto.SIN_POSICION


    var saldo: Int = 0
    override fun onCreate() {
        super.onCreate()
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        saldo = pref.getInt("saldo_inicial", -1)
    }

}