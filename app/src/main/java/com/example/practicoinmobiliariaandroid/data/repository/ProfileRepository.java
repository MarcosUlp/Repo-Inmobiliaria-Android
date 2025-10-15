package com.example.practicoinmobiliariaandroid.data.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.data.model.Propietario;
import com.example.practicoinmobiliariaandroid.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Call;

public class ProfileRepository {
    private final ApiClient.ApiService apiService;
    private final SessionManager sessionManager;

    public ProfileRepository(Context context) {
        this.apiService = ApiClient.getClient();
        this.sessionManager = new SessionManager(context);
    }

    // ----------------------------------------------------
    // LÓGICA DE PERFIL
    // ----------------------------------------------------

    public MutableLiveData<Propietario> getProfile() {
        MutableLiveData<Propietario> profileLiveData = new MutableLiveData<>();
        String token = sessionManager.getToken();

        if (token == null) {
            profileLiveData.postValue(null);
            return profileLiveData;
        }

        // Se usa el formato Bearer TOKEN
        Call<Propietario> call = apiService.getProfile("Bearer " + token);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    profileLiveData.postValue(response.body());
                } else {
                    Log.e("API_PROFILE", "Error al obtener perfil: " + response.code());
                    profileLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("API_PROFILE", "Fallo de conexión al obtener perfil: " + t.getMessage(), t);
                profileLiveData.postValue(null);
            }
        });
        return profileLiveData;
    }

    public MutableLiveData<Propietario> updateProfile(Propietario propietario) {
        MutableLiveData<Propietario> updateLiveData = new MutableLiveData<>();
        String token = sessionManager.getToken();

        if (token == null) {
            updateLiveData.postValue(null);
            return updateLiveData;
        }

        Call<Propietario> call = apiService.updateProfile("Bearer " + token, propietario);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateLiveData.postValue(response.body());
                } else {
                    Log.e("API_UPDATE", "Error al actualizar perfil: " + response.code());
                    updateLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("API_UPDATE", "Fallo de conexión al actualizar perfil: " + t.getMessage(), t);
                updateLiveData.postValue(null);
            }
        });
        return updateLiveData;
    }
}
