package com.example.mislugares.presentacion

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mislugares.Aplicacion
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsosActividades
import com.example.mislugares.casos_uso.CasosUsosLugar
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    val usoActividades by lazy { CasosUsosActividades(this) }
    val RESULTADO_PREFERENCIAS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        button02.setOnClickListener {
            lanzarPreferencias()
        }
        button03.setOnClickListener {
            lanzarAcercaDe()
        }

        button04.setOnClickListener{
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {

            R.id.acercaDe -> {
                lanzarAcercaDe()
                true
            }
            R.id.action_settings -> {
                usoActividades.lanzarPreferencias(RESULTADO_PREFERENCIAS)
                //lanzarPreferencias()
                true
            }
            R.id.menu_buscar -> {
                lanzarVistaLugar()
                true
            }

            /*R.id.menu_mapa -> {
                usoActividades.lanzarMapa()
                true
            }*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun mostrarPreferencias(view: View?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val s = ("notificaciones: " + pref.getBoolean("notificaciones", true)
                + ", máximo a listar: " + pref.getString("maximo", "?"))
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun lanzarAcercaDe(view: View? = null) {
        val i = Intent(this, AcercaDeActivity::class.java)
        startActivity(i)
    }

    fun lanzarPreferencias(view: View? = null) {
        val i = Intent(this, PreferenciasActivity::class.java)
        startActivity(i)
    }

    fun salir(view: View?) {
        finish();
    }

    fun lanzarVistaLugar(view: View? = null) {
        usoLugar.mostrar(0)
    }


    val lugares by lazy { (application as Aplicacion).lugares }
    val usoLugar by lazy { CasosUsosLugar(this, lugares) }


}