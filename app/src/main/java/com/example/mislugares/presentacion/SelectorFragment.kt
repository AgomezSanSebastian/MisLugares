package com.example.mislugares.presentacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mislugares.Aplicacion
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsosLugar

class SelectorFragment : Fragment() {
    val lugares by lazy { (activity!!.application as Aplicacion).lugares }
    val adaptador by lazy { (activity!!.application as Aplicacion).adaptador}
    val usoLugar by lazy { CasosUsosLugar(activity!!,null ,lugares, adaptador) }
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(inflador: LayoutInflater, contenedor:
    ViewGroup?, savedInstanceState: Bundle? ): View? {
        val vista =
                inflador.inflate(R.layout.fragment_selector,contenedor,false)
        recyclerView = vista.findViewById(R.id.recyclerView)
        return vista
    }
    override fun onActivityCreated(state: Bundle?) {
        super.onActivityCreated(state)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = adaptador
        }
        adaptador.onClick = {
            val pos = it.tag as Int
            usoLugar.mostrar(pos)
        }
    }
}