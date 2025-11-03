package com.example.practicoinmobiliariaandroid.ui.login;

import android.app.Application;
import android.widget.Toast;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.utils.SessionManager;
import com.example.practicoinmobiliariaandroid.data.repository.PropietarioRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private final PropietarioRepository repository;
    private final SessionManager sessionManager;
    private MutableLiveData<Boolean> mLogin = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        this.repository = new PropietarioRepository();
        this.sessionManager = new SessionManager(application.getApplicationContext());
    }
    //si esta logeado es true, si no esta logueado es false
    public LiveData<Boolean> getLogin() {
        return mLogin;
    }
    //chequear si hay sesion activa
    public void checkSession() {
        if (sessionManager.getToken() != null) {
            mLogin.setValue(true);
        }
    }

    public void login(String usuario, String clave) {
        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(getApplication(), "Complete los campos", Toast.LENGTH_SHORT).show();
            mLogin.setValue(false);
            return;
        }

        // LLAMAMOS AL REPOSITORY (no directamente a Retrofit)
        repository.login(usuario, clave).observeForever(token -> {
            if (token != null) {
                sessionManager.saveToken(token);
                Toast.makeText(getApplication(), "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                mLogin.setValue(true);
            } else {
                Toast.makeText(getApplication(), "Usuario o clave incorrecta", Toast.LENGTH_LONG).show();
                mLogin.setValue(false);
            }
        });
    }
}
