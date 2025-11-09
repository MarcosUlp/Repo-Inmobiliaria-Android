package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private final ApiClient.ApiService apiService;
    private final SessionManager sessionManager;
    private static final String TAG = "DetalleInmuebleVM";

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
        this.apiService = ApiClient.getClient();
        this.sessionManager = new SessionManager(application.getApplicationContext());
    }

    public void updateInmuebleStatus(Inmueble inmueble) {
        String token = sessionManager.getToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplication(), "Error de autenticación. Inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            return;
        }

        Inmueble req = new Inmueble();

        // Copiá los campos que el backend necesita (evitá campos problemáticos como 'clave')
        req.setIdInmueble(inmueble.getIdInmueble());
        req.setDireccion(inmueble.getDireccion());
        req.setUso(inmueble.getUso());
        req.setTipo(inmueble.getTipo());
        req.setAmbientes(inmueble.getAmbientes());
        //estuve teniendo problemas me decia que habia un campo invalido(ya solucionado)
        try {
            // Si tu modelo actual devuelve double, castealo a int
            // Si ya es int, esto no cambia nada
            int superficieInt = (int) Math.round(inmueble.getSuperficie());
            req.setSuperficie(superficieInt);
        } catch (Exception e) {
            // Fallback seguro
            req.setSuperficie(inmueble.getSuperficie()); // si el setter acepta int esto compila; si no, adaptá.
        }

        req.setLatitud(inmueble.getLatitud());
        req.setLongitud(inmueble.getLongitud());
        req.setValor(inmueble.getValor());
        req.setImagen(inmueble.getImagen());
        req.setDisponible(inmueble.isDisponible());
        req.setIdPropietario(inmueble.getIdPropietario());

        // Reconstruir DUENIO mínimo (el backend pide objeto, no null)
        com.example.practicoinmobiliariaandroid.data.model.Propietario duenio = new com.example.practicoinmobiliariaandroid.data.model.Propietario();
        duenio.setId(inmueble.getIdPropietario()); // solo el id es suficiente
        req.setDuenio(duenio);

        // Loggear el JSON que vamos a enviar (útil para confirmar)
        String jsonAEnviar = new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(req);
        Log.d(TAG, "JSON que voy a enviar al PUT:\n" + jsonAEnviar);

        // Llamada retrofit
        apiService.updateInmueble("Bearer " + token, req).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().isDisponible() ? "habilitado" : "deshabilitado";
                    Toast.makeText(getApplication(), "Inmueble " + status + " correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "errorBody null";
                        Log.e(TAG, "Error al actualizar inmueble: code=" + response.code() + " bodyError=" + error);
                        Toast.makeText(getApplication(), "Error al actualizar (código " + response.code() + ")", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "No pude leer errorBody", e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e(TAG, "Fallo de conexión al actualizar inmueble", t);
                Toast.makeText(getApplication(), "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}