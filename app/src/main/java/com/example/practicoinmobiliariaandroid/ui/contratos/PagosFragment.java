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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicoinmobiliariaandroid.data.model.Pago;
import com.example.practicoinmobiliariaandroid.databinding.FragmentPagosBinding;

import java.util.List;

public class PagosFragment extends Fragment {

    private FragmentPagosBinding binding;
    private PagosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        binding = FragmentPagosBinding.inflate(inflater, container, false);

        int idContrato = -1;

        // 1. Obtener el idContrato pasado por argumento
        if (getArguments() != null) {
            idContrato = getArguments().getInt("idContrato", -1);
            if (idContrato != -1) {
                // 2. Cargar los Pagos
                viewModel.loadPagos(idContrato);
            } else {
                Toast.makeText(getContext(), "Error: ID de Contrato no encontrado para listar pagos.", Toast.LENGTH_LONG).show();
            }
        }

        // 3. Observar la lista de Pagos
        viewModel.getPagosLiveData().observe(getViewLifecycleOwner(), this::setupRecyclerView);

        return binding.getRoot();
    }

    private void setupRecyclerView(List<Pago> pagos) {
        if (pagos == null || pagos.isEmpty()) {
            binding.tvEmptyState.setVisibility(View.VISIBLE);
            binding.rvPagos.setVisibility(View.GONE);
            return;
        }

        binding.tvEmptyState.setVisibility(View.GONE);
        binding.rvPagos.setVisibility(View.VISIBLE);

        PagosAdapter adapter = new PagosAdapter(pagos, getContext());
        RecyclerView rv = binding.rvPagos;

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}