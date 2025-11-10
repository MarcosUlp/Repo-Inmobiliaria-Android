package com.example.practicoinmobiliariaandroid.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.data.model.Contrato;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.databinding.FragmentDetalleContratoBinding;

import java.util.Objects;

public class DetalleContratoFragment extends Fragment {

    private FragmentDetalleContratoBinding binding;
    private DetalleContratoViewModel viewModel;
    private int currentIdContrato = -1;
    //a continuacion vamos a recibir el idinmueble, observar al contrato del viewmodel y actualiazar la ui
    //fecha monto y algunos datos del inquilino
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(DetalleContratoViewModel.class);
        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);

        // 1. Obtener el idInmueble pasado por argumento
        if (getArguments() != null) {
            int idInmueble = getArguments().getInt("idInmueble", -1);
            if (idInmueble != -1) {
                // 2. Cargar el Contrato asociado
                viewModel.loadContrato(idInmueble);
            } else {
                Toast.makeText(getContext(), "Error: ID de Inmueble no encontrado.", Toast.LENGTH_LONG).show();
            }
        }

        // 3. Observar el Contrato
        viewModel.getContratoLiveData().observe(getViewLifecycleOwner(), this::updateUI);

        // 4. Configurar el botón de Pagos (Listener)
        binding.btnVerPagos.setOnClickListener(v -> {
            if (currentIdContrato != -1) {
                Bundle bundle = new Bundle();
                bundle.putInt("idContrato", currentIdContrato);
                // Navegar a la vista de Pagos
                Navigation.findNavController(requireView()).navigate(R.id.nav_pagos, bundle);
            } else {
                Toast.makeText(getContext(), "Cargando contrato, intente de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void updateUI(Contrato contrato) {
        if (contrato == null) {
            Toast.makeText(getContext(), "No se pudo cargar el detalle del contrato.", Toast.LENGTH_LONG).show();
            return;
        }

        // Guardar el ID del contrato para la navegación a Pagos
        currentIdContrato = contrato.getIdContrato();

        // Formatear fechas y estado
        binding.tvContratoId.setText(String.valueOf(contrato.getIdContrato()));
        binding.tvFechaInicio.setText(contrato.getFechaInicio());
        binding.tvFechaFin.setText(contrato.getFechaFinalizacion());
        binding.tvMontoAlquiler.setText("$" + String.format("%,.2f", contrato.getMontoAlquiler()));
        binding.tvEstado.setText(contrato.isEstado() ? "Vigente" : "Finalizado");
        binding.tvEstado.setTextColor(getResources().getColor(contrato.isEstado() ? R.color.blue_primary : R.color.gray_dark));

        // Cargar datos del Inquilino (asumiendo que existe)
        if (contrato.getInquilino() != null) {
            binding.tvInquilinoNombre.setText(contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
            binding.tvInquilinoDni.setText(contrato.getInquilino().getDni());
            binding.tvInquilinoTelefono.setText(contrato.getInquilino().getTelefono());
            binding.tvInquilinoEmail.setText(contrato.getInquilino().getEmail());
        }

        // Asegurar que el botón se habilite al cargar el contrato
        binding.btnVerPagos.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}