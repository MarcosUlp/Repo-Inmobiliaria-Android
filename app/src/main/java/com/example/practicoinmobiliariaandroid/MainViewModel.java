package com.example.practicoinmobiliariaandroid;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.utils.SessionManager;

public class MainViewModel extends AndroidViewModel {

    // LiveData que la Activity observará para saber cuándo navegar al login
    private final MutableLiveData<Boolean> logoutCompleted;
    private final SessionManager sessionManager;

    public MainViewModel(@NonNull Application application) {
        super(application);
        // Inicializamos SessionManager usando el contexto de la aplicación
        sessionManager = new SessionManager(application.getApplicationContext());
        logoutCompleted = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLogoutCompleted() {
        return logoutCompleted;
    }

    /**
     * Lógica de cierre de sesión: limpia el token y notifica a la Activity.
     */
    public void logout() {
        // 1. Limpieza del token de sesión
        sessionManager.clearSession();

        // 2. Mensaje de feedback
        Toast.makeText(getApplication(), "Sesión cerrada con éxito.", Toast.LENGTH_SHORT).show();

        // 3. Notificar a la Activity que debe realizar la navegación (debe usarse postValue si se llama desde un hilo secundario, pero setValue si se llama desde el hilo principal, como es el caso de onNavigationItemSelected)
        logoutCompleted.setValue(true);
    }
}