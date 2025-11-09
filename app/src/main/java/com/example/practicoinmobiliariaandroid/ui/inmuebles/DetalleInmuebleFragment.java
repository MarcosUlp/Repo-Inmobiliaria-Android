package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.databinding.FragmentDetalleInmuebleBinding;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;
    private DetalleInmuebleViewModel viewModel;
    private Inmueble currentInmueble;

    // URL Base para las imágenes
    private static final String URL_BASE = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmuebleData");

            if (inmueble != null) {
                this.currentInmueble = inmueble;

                // Asignar los valores a los TextViews
                binding.tvIdInmueble.setText(String.valueOf(currentInmueble.getIdInmueble()));
                binding.tvDireccionI.setText(currentInmueble.getDireccion());
                binding.tvUsoI.setText(currentInmueble.getUso());
                binding.tvAmbientesI.setText(String.valueOf(currentInmueble.getAmbientes()));
                binding.tvLatitudI.setText(String.valueOf(currentInmueble.getLatitud()));

                // Usamos el campo longitud del modelo
                binding.tvLongitudI.setText(String.valueOf(currentInmueble.getLongitud()));

                binding.tvValorI.setText("$" + String.format("%,.2f", currentInmueble.getValor()));

                // El isChecked ahora usa el isDisponible()
                binding.checkDisponible.setChecked(currentInmueble.isDisponible());

                binding.checkDisponible.setEnabled(true);

                binding.checkDisponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        // 1. Modificar el estado de disponibilidad
                        currentInmueble.setDisponible(isChecked);

                        // SOLUCIÓN AL ERROR 400: Anular el objeto 'duenio' para pasar la validación del servidor.
                        currentInmueble.setDuenio(null);

                        // 2. Llamar al ViewModel
                        viewModel.updateInmuebleStatus(currentInmueble);
                    }
                });

                if (currentInmueble.getImagen() != null && !currentInmueble.getImagen().isEmpty()) {

                    // ✨ FIX CRÍTICO DEL ERROR GLIDE: Normalizar la URL
                    // 1. Reemplazar '\' con '/'
                    String relativePath = currentInmueble.getImagen().replace("\\", "/");

                    // 2. Construir la URL completa, añadiendo una barra ('/') después del dominio si es necesario
                    String baseUrl = URL_BASE;
                    if (!baseUrl.endsWith("/")) {
                        baseUrl += "/";
                    }

                    // 3. Crear la URL final: https://...net/uploads/image.jpg
                    String imageUrl = currentInmueble.getImagen().startsWith("http") ? currentInmueble.getImagen() : baseUrl + relativePath;

                    Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.inmueble_defecto)
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