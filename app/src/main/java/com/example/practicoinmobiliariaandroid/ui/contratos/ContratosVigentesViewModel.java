package com.example.practicoinmobiliariaandroid.ui.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.data.repository.ContratoRepository;

import java.util.List;

public class ContratosVigentesViewModel extends AndroidViewModel {

    private final ContratoRepository repository;
    private final LiveData<List<Inmueble>> inmueblesConContrato;
    //aca solo llamamos al repo de contrato para obtener la lista de inmueble
    //trae los contratos activos
    public ContratosVigentesViewModel(@NonNull Application application) {
        super(application);
        repository = new ContratoRepository(application.getApplicationContext());
        inmueblesConContrato = repository.getInmueblesConContrato();
    }
    //expone con livedata
    public LiveData<List<Inmueble>> getInmueblesConContrato() {
        return inmueblesConContrato;
    }
}