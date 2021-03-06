package com.example.mislugares.casos_uso

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.mislugares.R
import com.example.mislugares.datos.AdaptadorLugaresBD
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.DialogoSelectorFecha
import java.text.DateFormat.getTimeInstance

import java.util.*


class CasosUsoLugarFecha(
        override val actividad: FragmentActivity,
        override val fragment: Fragment,
        override val lugares: LugaresBD,
        override val adaptador: AdaptadorLugaresBD
) : CasosUsosLugar(actividad, fragment, lugares, adaptador), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    var pos: Int = -1
    lateinit var lugar: Lugar
    fun cambiarHora(pos: Int, textView: TextView) {
        lugar = adaptador.lugarPosicion(pos)
        this.pos = pos
        val dialogo = DialogoSelectorHora()
        dialogo.setOnTimeSetListener(this)
        val args = Bundle();
        args.putLong("fecha", lugar.fecha)
        dialogo.setArguments(args)
        dialogo.show(actividad.supportFragmentManager, "selectorHora")
    }


    class DialogoSelectorHora : DialogFragment() {
        private var escuchador: TimePickerDialog.OnTimeSetListener? = null
        fun setOnTimeSetListener(escuchador: TimePickerDialog.OnTimeSetListener) {
            this.escuchador = escuchador
        }
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendario = Calendar.getInstance()
            val fecha = arguments?.getLong("fecha")?:System.currentTimeMillis()
            calendario.setTimeInMillis(fecha)
            val hora = calendario.get(Calendar.HOUR_OF_DAY)
            val minuto = calendario.get(Calendar.MINUTE)
            return TimePickerDialog(
                    getActivity(), escuchador, hora,
                    minuto, DateFormat.is24HourFormat(getActivity())
            )
        }
    }

    override fun onTimeSet(vista: TimePicker?, hora: Int, minuto: Int) {
        val calendario = Calendar.getInstance()
        calendario.setTimeInMillis(lugar.fecha)
        calendario.set(Calendar.HOUR_OF_DAY, hora)
        calendario.set(Calendar.MINUTE, minuto)
        lugar.fecha = calendario.getTimeInMillis()
        actualizaPosLugar(pos, lugar)
        val textView = actividad.findViewById<TextView>(R.id.hora)
        textView.text= DateFormat.getTimeInstance().format(Date(lugar.fecha))
    }

    fun cambiarFecha(pos: Int) {
        lugar = adaptador.lugarPosicion(pos)
        this.pos = pos
        val dialogo = DialogoSelectorFecha()
        dialogo.setOnDateSetListener(this)
        val args = Bundle()
        args.putLong("fecha", lugar.fecha)
        dialogo.setArguments(args)
        dialogo.show(actividad.supportFragmentManager, "selectorFecha")
    }

    override fun onDateSet(view: DatePicker, anyo: Int, mes: Int, dia: Int) {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = lugar.fecha
        calendario.set(Calendar.YEAR, anyo)
        calendario.set(Calendar.MONTH, mes)
        calendario.set(Calendar.DAY_OF_MONTH, dia)
        lugar.fecha = calendario.timeInMillis
        actualizaPosLugar(pos, lugar)
        val textView = actividad.findViewById<TextView>(R.id.fecha)
        textView.text = java.text.DateFormat.getDateInstance().format(Date(lugar.fecha))
    }
}
