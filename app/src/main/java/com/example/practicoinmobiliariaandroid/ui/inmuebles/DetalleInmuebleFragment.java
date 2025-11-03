package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.databinding.FragmentDetalleInmuebleBinding;
// Importa tu librería de carga de imágenes (ej: Glide o Picasso) si la usas
// import com.bumptech.glide.Glide;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inicialización del View Binding
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Verificar si hay argumentos de navegación
        if (getArguments() != null) {
            // Se asume que el objeto Inmueble se pasó como Serializable con la clave "inmueble"
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmuebleData");

            if (inmueble != null) {
                // Asignar los valores a los TextViews
                binding.tvIdInmueble.setText(String.valueOf(inmueble.getIdInmueble()));
                binding.tvDireccionI.setText(inmueble.getDireccion());
                binding.tvUsoI.setText(inmueble.getUso());
                binding.tvAmbientesI.setText(String.valueOf(inmueble.getAmbientes()));
                binding.tvLatitudI.setText(String.valueOf(inmueble.getLatitud()));


                // Formatear el valor con separador de miles si es necesario
                binding.tvValorI.setText("$" + String.format("%,.2f", inmueble.getValor()));

                binding.checkDisponible.setChecked(inmueble.isDisponible());

                // Deshabilitar el CheckBox para que sea solo de lectura
                binding.checkDisponible.setEnabled(false);


                if (inmueble.getImagen() != null && !inmueble.getImagen().isEmpty()) {
                    Glide.with(this)
                        .load(inmueble.getImagen())
                        .placeholder(R.drawable.inmueble_defecto) // Placeholder si la imagen no carga
                        .into(binding.imgInmueble);
                }

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
