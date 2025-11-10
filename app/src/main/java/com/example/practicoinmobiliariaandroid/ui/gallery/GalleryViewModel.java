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
    // Guardar cambios en el perfil (NUEVA FIRMA)
    // ---------------------------------------------------
    public void saveProfile(int id, String nombre, String apellido, String telefono) {

        // 1. Validaciones de datos (Lógica en el ViewModel)
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(getApplication(), "Nombre, Apellido y Teléfono no pueden estar vacíos.", Toast.LENGTH_LONG).show();
            return;
        }

        Propietario currentPropietario = propietarioLiveData.getValue();
        if (currentPropietario == null) {
            Toast.makeText(getApplication(), "Error interno al obtener datos de perfil.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Construir el objeto Propietario (Lógica del Modelo/ViewModel)
        Propietario updatedPropietario = new Propietario(
                id,
                nombre,
                apellido,
                currentPropietario.getDni(),
                telefono,
                currentPropietario.getEmail(),
                currentPropietario.getClave() // Aseguramos que la clave se mantenga si es necesaria para la petición
        );

        // 3. Llamar al Repository
        repository.updateProfile(updatedPropietario, new ProfileRepository.ProfileCallback() {
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
