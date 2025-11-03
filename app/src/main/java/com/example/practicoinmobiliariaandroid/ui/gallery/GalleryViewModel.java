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

    private final ProfileRepository repository;
    private final MutableLiveData<Propietario> propietarioLiveData = new MutableLiveData<>();

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        repository = new ProfileRepository(application);
        loadProfile();
    }

    public LiveData<Propietario> getPropietario() {
        return propietarioLiveData;
    }

    // ---------------------------------------------------
    // Carga inicial del perfil
    // ---------------------------------------------------
    public void loadProfile() {
        repository.getProfile(new ProfileRepository.ProfileCallback() {

            @Override
            public void onSuccess(Propietario propietario) {
                propietarioLiveData.postValue(propietario);
            }

            @Override
            public void onError(String error) {
                propietarioLiveData.postValue(null);
                Toast.makeText(getApplication(), "Error al cargar perfil: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    // ---------------------------------------------------
    // Guardar cambios en el perfil
    // ---------------------------------------------------
    public void saveProfile(Propietario propietario) {
        repository.updateProfile(propietario, new ProfileRepository.ProfileCallback() {
            @Override
            public void onSuccess(Propietario propietarioActualizado) {
                propietarioLiveData.postValue(propietarioActualizado);
                Toast.makeText(getApplication(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al actualizar: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
