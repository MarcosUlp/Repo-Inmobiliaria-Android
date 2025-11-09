package com.example.practicoinmobiliariaandroid.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.data.model.Contrato;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.data.model.Pago;
import com.example.practicoinmobiliariaandroid.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoRepository {
    private final ApiClient.ApiService apiService;
    private final SessionManager sessionManager;
    private static final String TAG = "ContratoRepo";

    public ContratoRepository(Context context) {
        this.apiService = ApiClient.getClient();
        this.sessionManager = new SessionManager(context);
    }

    // ----------------------------------------
    // Listar Inmuebles con Contrato Vigente
    // ----------------------------------------
    public LiveData<List<Inmueble>> getInmueblesConContrato() {
        MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>();
        String token = sessionManager.getToken();

        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token no encontrado.");
            inmueblesLiveData.postValue(null);
            return inmueblesLiveData;
        }

        apiService.getInmuebleContratoVigente("Bearer " + token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmueblesLiveData.postValue(response.body());
                } else {
                    Log.e(TAG, "Error al obtener inmuebles con contrato: Código " + response.code() + ", Mensaje: " + response.message());
                    inmueblesLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e(TAG, "Fallo de conexión al listar contratos", t);
                inmueblesLiveData.postValue(null);
            }
        });
        return inmueblesLiveData;
    }

    // ----------------------------------------
    // Obtener Contrato por ID de Inmueble (Para Detalle)
    // ----------------------------------------
    public LiveData<Contrato> getContratoPorInmueble(int idInmueble) {
        MutableLiveData<Contrato> contratoLiveData = new MutableLiveData<>();
        String token = sessionManager.getToken();

        // ... (lógica API similar)
        if (token == null || token.isEmpty()) {
            contratoLiveData.postValue(null);
            return contratoLiveData;
        }

        apiService.getContratoPorInmueble("Bearer " + token, idInmueble).enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contratoLiveData.postValue(response.body());
                } else {
                    Log.e(TAG, "Error al obtener contrato por inmueble: Código " + response.code());
                    contratoLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Log.e(TAG, "Fallo de conexión al obtener contrato", t);
                contratoLiveData.postValue(null);
            }
        });
        return contratoLiveData;
    }

    // ----------------------------------------
    // Obtener Pagos por ID de Contrato (Para Listado)
    // ----------------------------------------
    public LiveData<List<Pago>> getPagosPorContrato(int idContrato) {
        MutableLiveData<List<Pago>> pagosLiveData = new MutableLiveData<>();
        String token = sessionManager.getToken();

        // ... (lógica API similar)
        if (token == null || token.isEmpty()) {
            pagosLiveData.postValue(null);
            return pagosLiveData;
        }

        apiService.getPagosPorContrato("Bearer " + token, idContrato).enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pagosLiveData.postValue(response.body());
                } else {
                    Log.e(TAG, "Error al obtener pagos: Código " + response.code());
                    pagosLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                Log.e(TAG, "Fallo de conexión al obtener pagos", t);
                pagosLiveData.postValue(null);
            }
        });
        return pagosLiveData;
    }
}