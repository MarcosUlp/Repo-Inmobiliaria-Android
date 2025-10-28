package com.example.practicoinmobiliariaandroid.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.data.api.ApiClient.ApiService;
import com.example.practicoinmobiliariaandroid.data.model.Propietario;
import com.example.practicoinmobiliariaandroid.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private final ApiService apiService;
    private final SessionManager sessionManager;

    public interface ProfileCallback {
        void onSuccess(Propietario propietario);
        void onError(String error);
    }

    public ProfileRepository(Context context) {
        this.apiService = ApiClient.getClient();
        this.sessionManager = new SessionManager(context);
    }

    // ----------------------------------------
    // Obtener perfil
    // ----------------------------------------

    public void getProfile(ProfileCallback callback) {
        String token = sessionManager.getToken();
        if (token == null) {
            callback.onError("Token no encontrado.");
            return;
        }

        apiService.getProfile("Bearer " + token).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener perfil: código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("API_PROFILE", "Fallo al obtener perfil", t);
                callback.onError("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    // Actualizar perfil
    public void updateProfile(Propietario propietario, ProfileCallback callback) {
        String token = sessionManager.getToken();
        if (token == null) {
            callback.onError("Token no encontrado.");
            return;
        }

        apiService.updateProfile("Bearer " + token, propietario).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al actualizar perfil: código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("API_UPDATE", "Fallo al actualizar perfil", t);
                callback.onError("Fallo de conexión: " + t.getMessage());
            }
        });
    }
}
