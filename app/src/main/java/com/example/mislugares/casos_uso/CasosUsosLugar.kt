package com.example.mislugares.casos_uso

import android.app.Activity
import android.content.Intent
import com.example.mislugares.datos.RepositorioLugares
import com.example.mislugares.presentacion.VistaLugarActivity

class CasosUsosLugar (val actividad: Activity, val lugares: RepositorioLugares) {
    // OPERACIONES BÁSICAS
    fun mostrar(pos: Int) {
        val i = Intent(actividad, VistaLugarActivity::class.java)
        i.putExtra("pos", pos);
        actividad.startActivity(i);
    }
}