package com.example.mislugares.presentacion

import android.app.Activity
import android.os.Bundle
import com.example.mislugares.PreferenciasFragment

class PreferenciasActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, PreferenciasFragment())
            .commit()
    }
}