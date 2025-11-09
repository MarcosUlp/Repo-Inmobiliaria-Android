package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.databinding.FragmentNuevoInmuebleBinding;

public class NuevoInmuebleFragment extends Fragment {

    private FragmentNuevoInmuebleBinding binding;
    private NuevoInmuebleViewModel viewModel;
    private Uri selectedImageUri; // URI de la imagen seleccionada

    // Launcher para seleccionar la imagen
    private final ActivityResultLauncher<String> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    // Mostrar preview (Lógica de UI)
                    binding.ivImagePreview.setVisibility(View.VISIBLE);
                    Glide.with(this).load(selectedImageUri).placeholder(R.drawable.inmueble_defecto).into(binding.ivImagePreview);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);
        binding = FragmentNuevoInmuebleBinding.inflate(inflater, container, false);

        // Listener para seleccionar imagen
        binding.btnSelectImage.setOnClickListener(v -> selectImageLauncher.launch("image/*"));

        // Listener para cargar inmueble (Solo llama al método de recolección de datos)
        binding.btnCargarInmueble.setOnClickListener(v -> collectAndSendInput());

        // Observar resultado de la carga (para navegación)
        viewModel.getUploadSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                // Navegar de vuelta a la lista de inmuebles si fue exitoso
                Navigation.findNavController(requireView()).navigate(R.id.nav_inmueble);
            }
        });

        // Observar mensajes de error (para mostrar Toast, que es lógica de UI)
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        return binding.getRoot();
    }

    // Método que colecta la información y la pasa al ViewModel
    private void collectAndSendInput() {
        String direccion = binding.etDireccion.getText().toString().trim();
        String uso = binding.etUso.getText().toString().trim();
        String tipo = binding.etTipo.getText().toString().trim();
        String ambientesStr = binding.etAmbientes.getText().toString().trim();
        String valorStr = binding.etValor.getText().toString().trim();

        // Llama al ViewModel con los datos crudos. El ViewModel maneja el resto.
        viewModel.createInmueble(direccion, uso, tipo, ambientesStr, valorStr, selectedImageUri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}