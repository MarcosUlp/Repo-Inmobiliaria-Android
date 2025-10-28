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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // vincular e layout
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 1nicializar ViewModel
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        // configurar boton flotante
        fabEdit = binding.fabEdit;
        fabEdit.setOnClickListener(v -> toggleEditMode());

        // observa  datos del perfil
        viewModel.getPropietario().observe(getViewLifecycleOwner(), propietario -> {
            //el if es irrelevante porque se maneja del lado del repository
            if (propietario != null) {
                updateUI(propietario);
                //el contenido de updateUI tendria que estar acá
            } else {
                Toast.makeText(requireContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
            }
        });

        // 5️⃣ Iniciar en modo lectura
        setEditable(false);

        return root;
    }

    // Actualiza los campos de texto en la vista

    private void updateUI(Propietario propietario) {
        binding.etNombre.setText(propietario.getNombre());
        binding.etApellido.setText(propietario.getApellido());
        binding.etDni.setText(propietario.getDni());
        binding.etTelefono.setText(propietario.getTelefono());
        binding.etEmail.setText(propietario.getEmail());
    }

    // botoncito activar/desactivar modo edición
    // el toggle se podria manejar del lado del viewmodel?

    private void toggleEditMode() {
        if (isEditing) {
            // Guardar cambios
            saveChanges();
            fabEdit.setImageResource(android.R.drawable.ic_menu_edit);
        } else {
            // Cambiar a modo edición
            fabEdit.setImageResource(android.R.drawable.ic_menu_save);
        }

        isEditing = !isEditing;
        setEditable(isEditing);
    }

    // --------------------------------------------------
    // habilita o bloquea campos editables
    // --------------------------------------------------
    private void setEditable(boolean editable) {
        binding.etNombre.setEnabled(editable);
        binding.etApellido.setEnabled(editable);
        binding.etTelefono.setEnabled(editable);
        // DNI y Email quedan bloqueados (no deberian poder cambiarse)
    }

    // --------------------------------------------------
    // Guarda los cambios realizados
    // --------------------------------------------------
    private void saveChanges() {
        Propietario currentPropietario = viewModel.getPropietario().getValue();

        if (currentPropietario == null) {
            Toast.makeText(requireContext(), "No se pudo guardar, perfil vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tomar datos del formulario
        String nombre = binding.etNombre.getText().toString().trim();
        String apellido = binding.etApellido.getText().toString().trim();
        String telefono = binding.etTelefono.getText().toString().trim();

        // Crear nuevo objeto con los cambios
        Propietario updatedPropietario = new Propietario(
                currentPropietario.getId(),
                nombre,
                apellido,
                currentPropietario.getDni(),
                telefono,
                currentPropietario.getEmail()
        );

        // Guardar usando ViewModel
        viewModel.saveProfile(updatedPropietario);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
