package com.example.practicoinmobiliariaandroid.ui.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.model.Contrato;
import com.example.practicoinmobiliariaandroid.data.repository.ContratoRepository;

public class DetalleContratoViewModel extends AndroidViewModel {
    private final ContratoRepository repository;
    private final MutableLiveData<Contrato> contratoLiveData = new MutableLiveData<>();

    public DetalleContratoViewModel(@NonNull Application application) {
        super(application);
        repository = new ContratoRepository(application.getApplicationContext());
    }

    public LiveData<Contrato> getContratoLiveData() {
        return contratoLiveData;
    }

    public void loadContrato(int idInmueble) {
        // Se carga el contrato desde el repositorio y se observa internamente.
        // Si tienes una dependencia de LiveData, puedes hacer:
        repository.getContratoPorInmueble(idInmueble).observeForever(contratoLiveData::postValue);
    }
}