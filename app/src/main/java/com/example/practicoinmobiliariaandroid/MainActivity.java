package com.example.practicoinmobiliariaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.practicoinmobiliariaandroid.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practicoinmobiliariaandroid.databinding.ActivityMainBinding;

// Ya no implementamos NavigationView.OnNavigationItemSelectedListener
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        observeLogoutState();

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // ELIMINADO: navigationView.setNavigationItemSelectedListener(this);

        // CORRECCIÓN CLAVE: nav_logout NO es un destino de Fragment, se elimina de aquí
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_inquilinos, R.id.nav_contratos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // ✨ NUEVA SOLUCIÓN ROBUSTA: Enlazar el listener directamente al ítem de Logout
        MenuItem logoutMenuItem = navigationView.getMenu().findItem(R.id.nav_logout);
        if (logoutMenuItem != null) {
            logoutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    // Llamar al ViewModel para iniciar el proceso de cierre de sesión
                    viewModel.logout();

                    // Cerrar el Drawer inmediatamente (la navegación se maneja en el observer)
                    binding.drawerLayout.closeDrawers();

                    // Retorna true para indicar que el evento fue consumido
                    return true;
                }
            });
        }
    }

    private void observeLogoutState() {
        // La Activity observa al ViewModel. Cuando el ViewModel dice "listo", navegamos.
        viewModel.getLogoutCompleted().observe(this, completed -> {
            if (completed) {
                // Navegar al Login y limpiar el historial
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Finaliza MainActivity
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // ELIMINADO: Ya no necesitamos el método onNavigationItemSelected
    // @Override
    // public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //    ... (código anterior) ...
    // }
}
