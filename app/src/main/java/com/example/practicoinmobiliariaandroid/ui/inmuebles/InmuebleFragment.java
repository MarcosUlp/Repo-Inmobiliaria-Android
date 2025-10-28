package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.databinding.FragmentInmuebleBinding;

import java.util.List;

public class InmuebleFragment extends Fragment {

    private FragmentInmuebleBinding binding;
    private InmuebleViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inicialización del ViewModel y el Binding
        vm = new ViewModelProvider(this).get(InmuebleViewModel.class);
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);

        // Observa la lista de inmuebles del ViewModel
        vm.getmInmueble().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                // Configura el RecyclerView con el adaptador y el LayoutManager
                InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, getContext());
                // Usa GridLayoutManager para una mejor visualización de tarjetas
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
                RecyclerView rv = binding.rvListaInmueble;

                rv.setAdapter(adapter);
                rv.setLayoutManager(glm);
            }
        });

        // La llamada a vm.leerInmuebles() ya está en el constructor del ViewModel

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
