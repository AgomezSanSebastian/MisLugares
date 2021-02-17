package com.example.mislugares

import android.os.Bundle
import android.preference.PreferenceFragment

class PreferenciasFragment  : PreferenceFragment() { //Deprecated
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferencias)
    }
}