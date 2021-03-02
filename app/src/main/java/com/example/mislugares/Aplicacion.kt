package com.example.mislugares

import android.app.Application
import com.example.mislugares.datos.LugaresLista
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.presentacion.AdaptadorLugares

class Aplicacion : Application() {
    val lugares = LugaresLista()
    val adaptador = AdaptadorLugares(lugares)
    val posicionActual = GeoPunto.SIN_POSICION


    var saldo: Int = 0
    override fun onCreate() {
        super.onCreate()
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        saldo = pref.getInt("saldo_inicial", -1)
    }

}