package com.example.practicoinmobiliariaandroid.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicoinmobiliariaandroid.R; // Importamos el recurso R
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.databinding.FragmentContratosVigentesBinding;

import java.util.List;

public class ContratosVigentesFragment extends Fragment {

    private FragmentContratosVigentesBinding binding;
    private ContratosVigentesViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(ContratosVigentesViewModel.class);
        binding = FragmentContratosVigentesBinding.inflate(inflater, container, false);

        vm.getInmueblesConContrato().observe(getViewLifecycleOwner(), this::setupRecyclerView);

        return binding.getRoot();
    }

    private void setupRecyclerView(List<Inmueble> inmuebles) {
        if (inmuebles == null || inmuebles.isEmpty()) {
            // Manejar caso sin inmuebles o error
            // Podrías mostrar un TextView de "No hay contratos vigentes"
            return;
        }

        ContratoInmuebleAdapter adapter = new ContratoInmuebleAdapter(inmuebles, getContext(), this);
        RecyclerView rv = binding.rvContratosVigentes;

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // Método que será llamado por el Adapter al hacer click
    public void onInmuebleClick(Inmueble inmueble) {
        Bundle bundle = new Bundle();
        // Pasamos el ID del inmueble para que la vista de detalle pueda buscar el Contrato
        bundle.putInt("idInmueble", inmueble.getIdInmueble());

        // Navegar al fragment de Detalle del Contrato
        Navigation.findNavController(requireView()).navigate(R.id.nav_detalle_contrato, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}