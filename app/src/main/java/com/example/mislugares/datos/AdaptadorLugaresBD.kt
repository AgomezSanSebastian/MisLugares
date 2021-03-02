package com.example.mislugares.datos

import android.database.Cursor
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.AdaptadorLugares

class AdaptadorLugaresBD(lugares: LugaresBD, var cursor: Cursor):
    AdaptadorLugares(lugares){
    fun lugarPosicion(posicion: Int): Lugar {
        cursor.moveToPosition(posicion)
        return (lugares as LugaresBD).extraeLugar(cursor)
    }
    fun idPosicion(posicion: Int): Int {
        cursor.moveToPosition(posicion)
        if (cursor.count>0) return cursor.getInt(0)
        else return -1
    }
    override fun onBindViewHolder(holder: AdaptadorLugares.ViewHolder,
                                  posicion: Int) {
        val lugar = lugarPosicion(posicion)
        holder.personaliza(lugar, onClick)
        holder.view.tag = posicion
    }
    override fun getItemCount(): Int {
        return cursor.getCount()
    }

    fun posicionId(id: Int): Int {
        var pos = 0
        while (pos < itemCount && idPosicion(pos) != id) pos++
        return if (pos >= itemCount) -1
        else pos
    }
}
