// Archivo: PropietarioRepository.java

package com.example.practicoinmobiliariaandroid.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropietarioRepository {
    // Ya no necesitas 'private ApiService apiService;'
    // Lo obtenemos en cada llamada o lo inicializamos en el constructor.

    // 💡 Inicializamos la interfaz de servicio directamente
    private final ApiClient.ApiService apiService;

    public PropietarioRepository() {
        // CORRECCIÓN: Usamos el método corregido para obtener la interfaz
        this.apiService = ApiClient.getClient();
    }

    public MutableLiveData<String> login(String usuario, String clave) {
        // ... (el resto del método login está correcto)
        MutableLiveData<String> tokenLiveData = new MutableLiveData<>();

        apiService.login(usuario, clave).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // El token JWT
                    tokenLiveData.postValue(response.body());
                } else {
                    Log.e("API_LOGIN", "Error: " + response.code() + (response.errorBody() != null ? " - " + response.errorBody().toString() : ""));
                    tokenLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API_LOGIN", "Fallo de conexión: " + t.getMessage(), t);
                tokenLiveData.postValue(null);
            }
        });

        return tokenLiveData;
    }
}