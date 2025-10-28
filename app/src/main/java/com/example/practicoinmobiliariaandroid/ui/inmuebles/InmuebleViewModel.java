package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.utils.SessionManager; // IMPORTACIÓN NECESARIA
import java.util.List;
import retrofit2.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel{
    private final MutableLiveData<String> mText = new MutableLiveData<>();
    private final MutableLiveData<List<Inmueble>> mInmueble = new MutableLiveData<>();
    private final SessionManager sessionManager; // DECLARACIÓN DEL MANEJADOR DE SESIÓN

public InmuebleViewModel(@NonNull Application application) {
    super(application);
    // INICIALIZACIÓN DEL MANEJADOR DE SESIÓN
    this.sessionManager = new SessionManager(application.getApplicationContext());
    leerInmuebles();
}

public LiveData<String> getmText() {
    return mText;
}

public LiveData<List<Inmueble>> getmInmueble() {
    return mInmueble;
}

public LiveData<String> getText() {
    return mText;
}
public void leerInmuebles(){
    // CAMBIO CLAVE: Obtener el token directamente desde SessionManager
    String token = sessionManager.getToken();

    // FIX: Verificar si el token es nulo o vacío antes de llamar a la API
    if (token == null || token.isEmpty()) {
        Toast.makeText(getApplication(), "Error de autenticación: Token no encontrado. Vuelva a iniciar sesión.", Toast.LENGTH_LONG).show();
        // No hacer la llamada si no hay token
        return;
    }
    ApiClient.ApiService api = ApiClient.getClient();
    Call<List<Inmueble>> llamada = api.getInmueble("Bearer "+token);

    llamada.enqueue(new Callback<List<Inmueble>>() {
        @Override
        public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
            if (response.isSuccessful()){
                mInmueble.postValue(response.body());
            } else if (response.code() == 401) {
                // Manejo explícito del 401 (token inválido o expirado)
                Toast.makeText(getApplication(), "Sesión expirada o token inválido. Código 401.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplication(), "Error al cargar inmuebles: "+response.message() + " (" + response.code() + ")", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onFailure(Call<List<Inmueble>> call, Throwable t) {
            Toast.makeText(getApplication(), "Error de conexión al servidor: "+t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
}