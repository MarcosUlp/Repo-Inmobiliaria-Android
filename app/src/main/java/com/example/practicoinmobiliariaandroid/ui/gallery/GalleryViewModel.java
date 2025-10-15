package com.example.practicoinmobiliariaandroid.ui.gallery;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.model.Propietario;
import com.example.practicoinmobiliariaandroid.data.repository.ProfileRepository;

public class GalleryViewModel extends AndroidViewModel {
    private final ProfileRepository profileRepository;
    private final MutableLiveData<Propietario> propietarioLiveData;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
        propietarioLiveData = new MutableLiveData<>();
        loadProfile();
    }

    public LiveData<Propietario> getPropietario() {
        return propietarioLiveData;
    }

    // Carga el perfil al iniciar el Fragment
    public void loadProfile() {
        profileRepository.getProfile().observeForever(propietario -> {
            propietarioLiveData.setValue(propietario);
            if (propietario == null) {
                // Mensaje solo si el valor es nulo (error de carga o sesión)
                Toast.makeText(getApplication(), "Error al cargar el perfil o sesión expirada.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Lógica para guardar los cambios del perfil
    public void saveProfile(Propietario propietario) {
        profileRepository.updateProfile(propietario).observeForever(updatedPropietario -> {
            if (updatedPropietario != null) {
                propietarioLiveData.setValue(updatedPropietario);
                Toast.makeText(getApplication(), "Perfil actualizado con éxito.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "Fallo la actualización del perfil.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
