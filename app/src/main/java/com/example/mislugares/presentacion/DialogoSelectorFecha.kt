package com.example.mislugares.presentacion

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*
import java.util.Calendar.*

class DialogoSelectorFecha : DialogFragment() {
    private var escuchador: DatePickerDialog.OnDateSetListener? = null
    fun setOnDateSetListener(escuchador: DatePickerDialog.OnDateSetListener) {
        this.escuchador = escuchador
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendario = getInstance()
        val fecha = getArguments()?.getLong("fecha")?:0L
        calendario.setTimeInMillis(fecha)
        val año = calendario.get(YEAR)
        val mes = calendario.get(MONTH)
        val dia = calendario.get(DAY_OF_MONTH)
        return DatePickerDialog(getActivity(), escuchador, año, mes, dia)
    }
}