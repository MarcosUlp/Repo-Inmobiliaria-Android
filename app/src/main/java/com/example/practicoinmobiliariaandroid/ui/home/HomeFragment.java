package com.example.practicoinmobiliariaandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Infla el layout que contiene el mapa
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Obtiene el SupportMapFragment (es el elemento clave para mostrar Google Maps)
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_home);

        if (mapFragment != null) {
            // Llama al callback cuando el mapa esté listo
            mapFragment.getMapAsync(this);
        }

        return root;
    }

    /**
     * Este método se ejecuta cuando el mapa está listo.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //  COORDENADAS (Sede central de la ULP)
        double latitud = -33.1506;
        double longitud = -66.3090;
        float zoomLevel = 16.0f;

        LatLng ubicacionFija = new LatLng(latitud, longitud);

        // 1. Agregar el marcador
        mMap.addMarker(new MarkerOptions()
                .position(ubicacionFija)
                .title("Ubicación de Inmueble Hardcodeada"));

        // 2. Mover la cámara al marcador
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionFija, zoomLevel));
    }
}