package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.data.model.Propietario; // Importado
import com.example.practicoinmobiliariaandroid.data.repository.InmuebleRepository; // Nuevo Repository
import com.example.practicoinmobiliariaandroid.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// ... (Imports omitidos)

public class DetalleInmuebleViewModel extends AndroidViewModel {

    // Reemplazamos ApiService por el Repository
    private final InmuebleRepository repository;
    private static final String TAG = "DetalleInmuebleVM";

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
        // Inicialización con el nuevo Repository
        this.repository = new InmuebleRepository(application.getApplicationContext());
    }

    public void updateInmuebleStatus(Inmueble inmueble) {

        // El Fragment pasa el objeto actualizado, ahora el ViewModel prepara el objeto para la API

        // 1. Clonar el objeto para la request (para evitar modificar el LiveData antes de tiempo)
        Inmueble req = new Inmueble(inmueble.getIdInmueble());

        // 2. Copiar todos los campos necesarios. Es más seguro pasar el ID.
        req.setDireccion(inmueble.getDireccion());
        req.setUso(inmueble.getUso());
        req.setTipo(inmueble.getTipo());
        req.setAmbientes(inmueble.getAmbientes());
        req.setSuperficie(inmueble.getSuperficie());
        req.setLatitud(inmueble.getLatitud());
        req.setLongitud(inmueble.getLongitud());
        req.setValor(inmueble.getValor());
        req.setImagen(inmueble.getImagen());
        req.setDisponible(inmueble.isDisponible());
        req.setIdPropietario(inmueble.getIdPropietario());

        // 3. Solución de compatibilidad API (mantenida en el ViewModel/Capa de Lógica)
        Propietario duenio = new Propietario();
        duenio.setId(inmueble.getIdPropietario());
        req.setDuenio(duenio); // Solo el ID es suficiente para la validación

        // Loggear el JSON que vamos a enviar (útil para confirmar)
        String jsonAEnviar = new GsonBuilder().setPrettyPrinting().create().toJson(req);
        Log.d(TAG, "JSON que voy a enviar al PUT:\n" + jsonAEnviar);

        // 4. Llamar al Repository
        repository.updateInmuebleStatus(req, new InmuebleRepository.InmuebleUpdateCallback() {
            @Override
            public void onSuccess(Inmueble inmuebleActualizado) {
                String status = inmuebleActualizado.isDisponible() ? "habilitado" : "deshabilitado";
                Toast.makeText(getApplication(), "Inmueble " + status + " correctamente.", Toast.LENGTH_SHORT).show();
                // Opcional: actualizar LiveData si existiera una en este VM
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error al actualizar inmueble: " + error);
                Toast.makeText(getApplication(), "Error al actualizar: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}