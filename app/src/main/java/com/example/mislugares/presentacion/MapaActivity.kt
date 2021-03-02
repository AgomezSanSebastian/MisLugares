package com.example.mislugares.presentacion

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.mislugares.Aplicacion
import com.example.mislugares.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity: FragmentActivity(), OnMapReadyCallback {
    lateinit var mapa: GoogleMap
    val adaptador by lazy { (application as Aplicacion).adaptador }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa)
                as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true)
            mapa.getUiSettings().setZoomControlsEnabled(true)
            mapa.getUiSettings().setCompassEnabled(true)
        }
        if ( adaptador.itemCount > 0) {
            val p = adaptador.lugarPosicion(0).posicion
            mapa.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                LatLng(p.latitud, p.longitud), 12F))
        }
        for (n in 0 until adaptador.itemCount) {
            val lugar = adaptador.lugarPosicion(n)
            val p = lugar.posicion
            if (p != null && p.latitud != 0.0) {
                val iGrande = BitmapFactory.decodeResource(
                    getResources(), lugar.tipoLugar.recurso)
                val icono = Bitmap.createScaledBitmap(iGrande,
                    iGrande.getWidth() / 7, iGrande.getHeight() / 7, false)
                mapa.addMarker(
                    MarkerOptions()
                        .position(LatLng(p.latitud, p.longitud))
                        .title(lugar.nombre).snippet(lugar.direccion)
                        .icon(BitmapDescriptorFactory.fromBitmap(icono)))
            }
        }
        mapa.setOnInfoWindowClickListener(this);
    }

    fun onInfoWindowClick(marker: Marker) {
        for (id in 0 until adaptador.itemCount) {
            if (adaptador.lugarPosicion(id).nombre == marker.title) {
                val intent = Intent(this, VistaLugarActivity::class.java)
                intent.putExtra("pos", id)
                startActivity(intent)
                break
            }
        }
    }
}

private fun GoogleMap.setOnInfoWindowClickListener(mapaActivity: MapaActivity) {

}


