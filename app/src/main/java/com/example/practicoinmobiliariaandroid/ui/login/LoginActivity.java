package com.example.practicoinmobiliariaandroid.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.practicoinmobiliariaandroid.MainActivity;
import com.example.practicoinmobiliariaandroid.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //Observa estado de login
        observeLoginState();

        //Revisa si hay sesión activa
        viewModel.checkSession();

        binding.btnLogin.setOnClickListener(v -> {
            String usuario = binding.etEmail.getText().toString().trim();
            String clave = binding.etPassword.getText().toString().trim();

            //Pasa datos al ViewModel
            viewModel.login(usuario, clave);
        });
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void observeLoginState() {
        // La Activity observa un booleano para navegar.
        // El ViewModel decidió, guardó sesión y mostró Toast.
        viewModel.getLogin().observe(this, loggedIn -> {

            //control de flujo para ejecutar la navegación.
            if (loggedIn) {
                navigateToMain();
            }
        });
    }
}