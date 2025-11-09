package com.example.practicoinmobiliariaandroid.ui.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.model.Pago;
import com.example.practicoinmobiliariaandroid.data.repository.ContratoRepository;

import java.util.List;

public class PagosViewModel extends AndroidViewModel {

    private final ContratoRepository repository;
    private final MutableLiveData<List<Pago>> pagosLiveData = new MutableLiveData<>();

    public PagosViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContratoRepository(application.getApplicationContext());
    }

    public LiveData<List<Pago>> getPagosLiveData() {
        return pagosLiveData;
    }

    /**
     * Carga los pagos asociados a un contrato específico.
     * @param idContrato ID del contrato cuyos pagos se desean listar.
     */
    public void loadPagos(int idContrato) {
        // Observamos el LiveData del repositorio
        repository.getPagosPorContrato(idContrato).observeForever(pagosLiveData::postValue);
    }
}