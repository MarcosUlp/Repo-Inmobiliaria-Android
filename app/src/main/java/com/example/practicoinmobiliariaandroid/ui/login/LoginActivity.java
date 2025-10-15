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

    // NO necesitamos SessionManager ni ninguna otra variable de lógica aquí.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // 1. Observar el estado de login
        observeLoginState();

        // 2. Revisar si ya hay sesión activa
        viewModel.checkSession();

        binding.btnLogin.setOnClickListener(v -> {
            String usuario = binding.etEmail.getText().toString().trim();
            String clave = binding.etPassword.getText().toString().trim();

            // 3. Pasar datos al ViewModel
            viewModel.login(usuario, clave);
        });
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void observeLoginState() {
        // La Activity solo observa un booleano para navegar.
        // El ViewModel ya decidió, guardó la sesión y mostró el Toast.
        viewModel.getLogin().observe(this, loggedIn -> {
            // Este 'if' es solo control de flujo para ejecutar la navegación.
            if (loggedIn) {
                navigateToMain();
            }
        });
    }
}