package com.example.demon.mapsandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private GoogleMap mapa;
    private Button btnOpciones;
    private Button btnMover;
    private Button btnAnimar;
    private Button btnPosicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnOpciones = (Button)findViewById(R.id.btnOpciones);
        btnOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarOpciones();
            }
        });
        btnMover = (Button)findViewById(R.id.btnMover);
        btnMover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moverMerida();
            }
        });
        btnAnimar = (Button)findViewById(R.id.btnAnimar);
        btnAnimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarMerida();
            }
        });
        btnPosicion = (Button)findViewById(R.id.btnPosicion);
        btnPosicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerPosicion();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
    }
    private void cambiarOpciones()
    {
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    private void moverMerida()
    {
        CameraUpdate camUpd1 =
                CameraUpdateFactory
                        .newLatLngZoom(new LatLng(20.9802, -89.7029), 10);
        mapa.moveCamera(camUpd1);
    }
    private void animarMerida()
    {
        LatLng merida = new LatLng(20.9896055, -89.6167457);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(merida) //Centramos el mapa en Merida
                .zoom(19) //Establecemos el zoom en 19
                .bearing(45) //Establecemos la orientación con el noreste arriba
                .tilt(80) //Bajamos el punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);
    }

    private void obtenerPosicion()
    {
        CameraPosition camPos = mapa.getCameraPosition();
        LatLng coordenadas = camPos.target;
        double latitud = coordenadas.latitude;
        double longitud = coordenadas.longitude;
        Toast.makeText(this, "Latitud: " + latitud + " | Longitud: " + longitud,
                Toast.LENGTH_SHORT).show();
    }
}