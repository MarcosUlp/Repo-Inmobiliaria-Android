package com.example.practicoinmobiliariaandroid.data.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.utils.SessionManager;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleRepository {
    private final ApiClient.ApiService apiService;
    private final SessionManager sessionManager;
    private static final String TAG = "InmuebleRepo";

    public InmuebleRepository(Context context) {
        this.apiService = ApiClient.getClient();
        this.sessionManager = new SessionManager(context);
    }

    public LiveData<List<Inmueble>> getAllInmuebles() {
        MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>();
        String token = sessionManager.getToken();

        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token no encontrado.");
            inmueblesLiveData.postValue(null);
            return inmueblesLiveData;
        }

        apiService.getInmueble("Bearer " + token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()) {
                    inmueblesLiveData.postValue(response.body());
                } else {
                    Log.e(TAG, "Error al obtener inmuebles: Código " + response.code());
                    inmueblesLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e(TAG, "Fallo de conexión al listar inmuebles", t);
                inmueblesLiveData.postValue(null);
            }
        });
        return inmueblesLiveData;
    }

    // Método para la actualización de disponibilidad
    public void updateInmuebleStatus(Inmueble inmueble, InmuebleUpdateCallback callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token no encontrado.");
            return;
        }

        apiService.updateInmueble("Bearer " + token, inmueble).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al actualizar: código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                callback.onError("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    // Método para crear un nuevo inmueble (Multipart)
    public void createInmueble(MultipartBody.Part imagePart, RequestBody inmuebleJson, InmuebleUpdateCallback callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token no encontrado.");
            return;
        }

        apiService.createInmueble("Bearer " + token, imagePart, inmuebleJson).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Error al cargar inmueble: Código " + response.code();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                callback.onError("Fallo de conexión al servidor: " + t.getMessage());
            }
        });
    }

    public interface InmuebleUpdateCallback {
        void onSuccess(Inmueble inmueble);
        void onError(String error);
    }
}