package com.example.practicoinmobiliariaandroid.ui.login;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    // LiveData que la Activity observará para saber cuándo navegar
    private MutableLiveData<Boolean> mLogin;
    private final SessionManager sessionManager;
    private final ApiClient.ApiService apiService;
    private final Context context; // Contexto de la aplicación

    public LoginViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.sessionManager = new SessionManager(context);
        this.apiService = ApiClient.getClient();
    }

    public LiveData<Boolean> getLogin() {
        if (mLogin == null) {
            mLogin = new MutableLiveData<>();
        }
        return mLogin;
    }

    public void checkSession() {
        if (sessionManager.getToken() != null) {
            mLogin.setValue(true); // Indica a la Activity que navegue
        }
    }

    // ✨ MÉTODO QUE CONTIENE TODA LA LÓGICA (Login, Session, Toast)
    public void login(String usuario, String clave) {

        // 1. Validación de campos (Se mantiene la responsabilidad aquí)
        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(getApplication(), "Complete los campos", Toast.LENGTH_SHORT).show();
            mLogin.setValue(false); // Opcional: Para asegurar que no navegue
            return;
        }

        // 2. Llamada de Red
        Call<String> llamada = apiService.login(usuario, clave);

        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();

                    // Lógica de sesión y mensaje (Directo en el ViewModel)
                    sessionManager.saveToken(token);
                    mLogin.postValue(true); // Indica a la Activity que navegue (Éxito)

                    // Mensaje de éxito (Directo en el ViewModel, como en el código del compa)
                    Toast.makeText(getApplication(), "¡Bienvenido!", Toast.LENGTH_SHORT).show();

                } else {
                    // Mensaje de error (Directo en el ViewModel)
                    mLogin.postValue(false); // Indica que el intento falló
                    Toast.makeText(getApplication(), "Usuario y/o clave Incorrecta, reintente", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Manejo de fallo de conexión (Directo en el ViewModel)
                mLogin.postValue(false);
                Toast.makeText(getApplication(), "Error en el servicio o de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }
}