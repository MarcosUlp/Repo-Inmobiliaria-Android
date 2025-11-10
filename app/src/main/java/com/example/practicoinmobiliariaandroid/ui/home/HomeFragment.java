package com.example.practicoinmobiliariaandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.practicoinmobiliariaandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HomeViewModel viewModel; // Nueva declaración

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Inflar el layout
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Obtener el SupportMapFragment y llamar al callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_home);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Observar los datos del marcador y el zoom del ViewModel
        viewModel.getMarkerOptions().observe(getViewLifecycleOwner(), markerOptions -> {
            if (markerOptions != null) {
                // 1a. Agregar el marcador
                mMap.addMarker(markerOptions);
            }
        });

        viewModel.getZoomLevel().observe(getViewLifecycleOwner(), zoomLevel -> {
            if (viewModel.getMarkerOptions().getValue() != null) {
                // 1b. Mover la cámara a la posición (debe ejecutarse después del marcador)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.getMarkerOptions().getValue().getPosition(), zoomLevel));
            }
        });
    }
}