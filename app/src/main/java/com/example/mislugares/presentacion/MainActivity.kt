package com.example.mislugares.presentacion

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mislugares.Aplicacion
import com.example.mislugares.R
import com.example.mislugares.casos_uso.CasosUsosActividades
import com.example.mislugares.casos_uso.CasosUsosLocalizacion
import com.example.mislugares.casos_uso.CasosUsosLugar
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Integer.parseInt


class MainActivity : AppCompatActivity() {


    val usoActividades by lazy { CasosUsosActividades(this) }
    val RESULTADO_PREFERENCIAS = 0
    val adaptador by lazy { (application as Aplicacion).adaptador }
    val usoLugar by lazy { CasosUsosLugar(this, lugares , adaptador) }
    val lugares by lazy { (application as Aplicacion).lugares }
    val SOLICITUD_PERMISO_LOCALIZACION = 1
    val usoLocalizacion by lazy {
        CasosUsosLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
                view -> usoLugar.nuevo()
        }

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show()

        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adaptador
        }

        adaptador.onClick = {
            val pos = it.tag as Int
            usoLugar.mostrar(pos)
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
            R.id.menu_mapa -> {
                startActivity(Intent(this, MapaActivity::class.java))
                true;

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun mostrarPreferencias(view: View? ) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val s = ("notificaciones: " + pref.getBoolean("notificaciones", true)
                + ", máximo a listar: " + pref.getString("maximo", "?"))
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun lanzarAcercaDe(view: View? = null) {
        val i = Intent(this, AcercaDeActivity::class.java)
        startActivity(i)
    }



    fun salir(view: View?) {
        finish();
    }


    fun lanzarPreferencias(view: View? = null) = startActivityForResult(
        Intent(this, PreferenciasActivity::class.java), RESULTADO_PREFERENCIAS)

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULTADO_PREFERENCIAS) {
            adaptador.cursor = lugares.extraeCursor()
            adaptador.notifyDataSetChanged()
        }
    }



    fun lanzarVistaLugar(view: View? = null) {
        val entrada = EditText(this)
        entrada.setText("0")
        AlertDialog.Builder(this)
                .setTitle("Selección de lugar")
                .setMessage("indica su id:")
                .setView(entrada)
                .setPositiveButton("Ok") { dialog, whichButton ->
                    val id = parseInt(entrada.text.toString())
                    usoLugar.mostrar(id);
                }
                .setNegativeButton("Cancelar", null)
                .show()
    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray ) {
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            usoLocalizacion.permisoConcedido()
    }


    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        usoLocalizacion.activar()
    }
    override fun onPause() {
        super.onPause()
        usoLocalizacion.desactivar()

    }
    override fun onStop() {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show()
        super.onStop()
    }
    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show()
    }
    override fun onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }


}