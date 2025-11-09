package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.practicoinmobiliariaandroid.data.api.ApiClient;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.utils.SessionManager;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoInmuebleViewModel extends AndroidViewModel {

    private final ApiClient.ApiService apiService;
    private final SessionManager sessionManager;
    private static final String TAG = "NuevoInmuebleVM";

    private final MutableLiveData<Boolean> uploadSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorMessage = new MutableLiveData<>();

    public NuevoInmuebleViewModel(@NonNull Application application) {
        super(application);
        this.apiService = ApiClient.getClient();
        this.sessionManager = new SessionManager(application.getApplicationContext());
    }

    public MutableLiveData<Boolean> getUploadSuccess() {
        return uploadSuccess;
    }

    public MutableLiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Lógica de subida de archivos robusta
     */
    public void createInmueble(String direccion, String uso, String tipo, String ambientesStr, String valorStr, Uri imageUri) {

        // 1. VALIDACIÓN DE CAMPOS Y PARSEO (Se mantiene en el ViewModel)
        if (direccion.isEmpty() || uso.isEmpty() || tipo.isEmpty() || ambientesStr.isEmpty() || valorStr.isEmpty()) {
            mErrorMessage.setValue("Por favor, complete todos los campos de texto.");
            return;
        }

        if (imageUri == null) {
            mErrorMessage.setValue("Por favor, seleccione una imagen para el inmueble.");
            return;
        }

        int ambientes;
        double valor;

        try {
            ambientes = Integer.parseInt(ambientesStr);
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            mErrorMessage.setValue("El valor o los ambientes no son números válidos.");
            return;
        }

        // 2. CONSTRUCCIÓN DEL OBJETO INMUEBLE
        Inmueble nuevoInmueble = new Inmueble(0);
        nuevoInmueble.setDireccion(direccion);
        nuevoInmueble.setUso(uso);
        nuevoInmueble.setTipo(tipo);
        nuevoInmueble.setAmbientes(ambientes);
        nuevoInmueble.setValor(valor);
        nuevoInmueble.setLatitud(0.0);
        nuevoInmueble.setLongitud(0.0);
        nuevoInmueble.setSuperficie(0);
        nuevoInmueble.setDisponible(true);

        String token = sessionManager.getToken();

        if (token == null || token.isEmpty()) {
            mErrorMessage.setValue("Error de autenticación. Vuelva a iniciar sesión.");
            return;
        }

        // 3. PREPARACIÓN MULTIPART
        Gson gson = new Gson();
        String inmuebleJsonString = gson.toJson(nuevoInmueble);
        RequestBody inmuebleJsonPart = RequestBody.create(MediaType.parse("application/json"), inmuebleJsonString);

        MultipartBody.Part imagePart = null;

        try {
            // ✨ CAMBIO CRÍTICO: Usar un buffer para leer todo el stream de forma robusta
            InputStream is = getApplication().getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            byte[] imageBytes = bos.toByteArray();
            is.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            String fileName = "inmueble_" + System.currentTimeMillis() + ".jpg";

            // Usamos 'imagen' como nombre de campo (coincide con la API)
            imagePart = MultipartBody.Part.createFormData("imagen", fileName, requestFile);

        } catch (Exception e) {
            Log.e(TAG, "Error al procesar la imagen", e);
            mErrorMessage.setValue("Error de lectura del archivo: " + e.getMessage());
            return;
        }

//
        apiService.createInmueble("Bearer " + token, imagePart, inmuebleJsonPart).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Inmueble creado con éxito.", Toast.LENGTH_LONG).show();
                    uploadSuccess.setValue(true);
                } else {
                    String errorMsg = "Error al cargar inmueble: Código " + response.code();

                    try {
                        // Intentamos obtener el detalle del error (útil si es 400)
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Detalles del error API: " + errorBody);
                        mErrorMessage.setValue(errorMsg + ". Detalles: " + errorBody.substring(0, Math.min(errorBody.length(), 50)));
                    } catch (Exception e) {
                        mErrorMessage.setValue(errorMsg + ".");
                    }
                    uploadSuccess.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) { // ✨ CORRECCIÓN: Tipo de Call
                Log.e(TAG, "Fallo de conexión al crear inmueble", t);
                mErrorMessage.setValue("Fallo de conexión al servidor: " + t.getMessage());
                uploadSuccess.setValue(false);
            }
        });
    }
}