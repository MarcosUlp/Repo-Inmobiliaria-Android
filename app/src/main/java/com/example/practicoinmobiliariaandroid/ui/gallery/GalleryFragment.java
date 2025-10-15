package com.example.practicoinmobiliariaandroid.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.practicoinmobiliariaandroid.data.model.Propietario;
import com.example.practicoinmobiliariaandroid.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private GalleryViewModel viewModel;
    private boolean isEditing = false;
    private FloatingActionButton fabEdit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inicializa el ViewModel
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fabEdit = binding.fabEdit;

        // 1. Configurar Observador
        viewModel.getPropietario().observe(getViewLifecycleOwner(), this::updateUI);

        // 2. Configurar el botón de Edición
        fabEdit.setOnClickListener(v -> toggleEditMode());

        // Inicia en modo lectura
        setEditable(false);

        return root;
    }

    private void updateUI(Propietario propietario) {
        if (propietario != null) {
            // Rellenar campos con los datos del propietario
            binding.etNombre.setText(propietario.getNombre());
            binding.etApellido.setText(propietario.getApellido());
            binding.etDni.setText(propietario.getDni()); // DNI siempre se muestra
            binding.etTelefono.setText(propietario.getTelefono());
            binding.etEmail.setText(propietario.getEmail()); // Email siempre se muestra
        } else {
            // Manejo de error o perfil vacío (el ViewModel ya muestra un Toast)
        }
    }

    private void toggleEditMode() {
        if (isEditing) {
            // Estábamos editando, ahora GUARDAR
            saveChanges();
            fabEdit.setImageResource(android.R.drawable.ic_menu_edit); // Cambia a ícono de edición
        } else {
            // Estábamos en lectura, ahora EDITAR
            fabEdit.setImageResource(android.R.drawable.ic_menu_save); // Cambia a ícono de guardar
        }
        isEditing = !isEditing;
        setEditable(isEditing);
    }

    private void setEditable(boolean editable) {
        // Habilita/deshabilita los campos editables
        binding.etNombre.setEnabled(editable);
        binding.etApellido.setEnabled(editable);
        binding.etTelefono.setEnabled(editable);
        // DNI y Email permanecen inalterables (por lo general, son las claves de usuario)
    }

    private void saveChanges() {
        Propietario currentPropietario = viewModel.getPropietario().getValue();

        if (currentPropietario != null) {
            // 1. Recolectar datos editables de la UI
            String nombre = binding.etNombre.getText().toString();
            String apellido = binding.etApellido.getText().toString();
            String telefono = binding.etTelefono.getText().toString();

            // 2. Crear un objeto Propietario con los datos editados
            Propietario updatedPropietario = new Propietario(
                    currentPropietario.getId(),
                    nombre,
                    apellido,
                    currentPropietario.getDni(), // Mantiene el DNI
                    telefono,
                    currentPropietario.getEmail() // Mantiene el Email
            );

            // 3. Llamar a la lógica del ViewModel
            viewModel.saveProfile(updatedPropietario);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
