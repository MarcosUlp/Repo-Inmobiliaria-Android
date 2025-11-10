package com.example.practicoinmobiliariaandroid.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeViewModel extends ViewModel {

    // LiveData que contendrá la posición y el marcador para el mapa
    private final MutableLiveData<MarkerOptions> markerOptionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Float> zoomLevelLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        // Lógica de negocio: definir dónde se ubica el mapa por defecto
        double latitud = -33.1506;
        double longitud = -66.3090;
        float zoomLevel = 16.0f;

        LatLng ubicacionFija = new LatLng(latitud, longitud);

        // Crear el MarkerOptions (datos)
        MarkerOptions marker = new MarkerOptions()
                .position(ubicacionFija)
                .title("Ubicación de Inmueble Hardcodeada");

        // Exponer los datos
        markerOptionsLiveData.setValue(marker);
        zoomLevelLiveData.setValue(zoomLevel);
    }

    public LiveData<MarkerOptions> getMarkerOptions() {
        return markerOptionsLiveData;
    }

    public LiveData<Float> getZoomLevel() {
        return zoomLevelLiveData;
    }
}