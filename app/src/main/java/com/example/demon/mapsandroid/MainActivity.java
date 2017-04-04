package com.example.demon.mapsandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private GoogleMap mapa;
    private Button btnOpciones;
    private Button btnMover;
    private Button btnAnimar;
    private Button btnPosicion;
    private TextView txtLaLo;
    private LatLng oldLoc;
    private Marker nosotros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager service = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

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
        txtLaLo = (TextView) findViewById(R.id.ninja);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location
                // provider.
                        makeUseOfNewLocation(location);
            }
            public void onStatusChanged(String provider, int status, Bundle
                    extras) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 2, locationListener);


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

    private void moverDondeSea(double lat,double lon){
        CameraUpdate camUpd1 =
                CameraUpdateFactory
                        .newLatLngZoom(new LatLng(lat, lon), 3);
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

    private  void animarDondeSea(double lat, double lon){
        // Math.round()
        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        Toast.makeText(this, "Siguiendo",Toast.LENGTH_SHORT).show();
        LatLng aqui = new LatLng(lat, lon);
        nosotros = mapa.addMarker(new MarkerOptions()
                .position(aqui)
                .anchor(1/2,1/2)
                .rotation(-10));
        CameraPosition camPos = new CameraPosition.Builder()
                .target(aqui) //Centramos el mapa en Merida
                .zoom(20) //Establecemos el zoom en 19
                .bearing(45) //Establecemos la orientación con el noreste arriba
                .tilt(20) //Bajamos el punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);
        if(oldLoc == null){
            oldLoc = aqui;
        }
        else{
            Polyline line = mapa.addPolyline(new PolylineOptions()
                    .add(new LatLng(oldLoc.latitude, oldLoc.longitude), new LatLng(aqui.latitude,   aqui.longitude))
                    .width(7)
                    .color(Color.BLUE).geodesic(true));
            oldLoc = aqui;
        }

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

    public void makeUseOfNewLocation(Location location) {
        if(nosotros != null) {
            nosotros.setVisible(false);
        }

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        animarDondeSea(latitude, longitude);
        txtLaLo.setText(latitude+" "+ longitude);
    }
}