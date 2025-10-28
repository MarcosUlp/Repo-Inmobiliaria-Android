package com.example.practicoinmobiliariaandroid.data.api;


import com.example.practicoinmobiliariaandroid.data.model.Inmueble;
import com.example.practicoinmobiliariaandroid.data.model.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class ApiClient {
    //aca vamos a configurar la conexion a la api
    private static final String BASE_URL = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net"; // la que te den
    private static Retrofit retrofit;

    //constructor: al implementar "static" hace que puedas llamar al metodo directamente con (apiClient.getClient)
    //sin necesidad de crear un objeto ApiClient

    public static ApiService getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

                 retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Retorna la instancia de la interfaz de servicio
        return retrofit.create(ApiService.class);
    }

    public interface ApiService {
        // --- AUTENTICACIÓN ---
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(
                @Field("Usuario") String usuario,
                @Field("Clave") String clave
        );

        // --- PERFIL ---
        @GET("api/Propietarios")
        Call<Propietario> getProfile(
                @Header("Authorization") String token
        );

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> updateProfile(
                @Header("Authorization") String token,
                @Body Propietario propietario
        );

        // --- CAMBIO DE CLAVE/RESET ---
        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> changePassword(
                @Header("Authorization") String token,
                @Field("currentPassword") String currentPassword,
                @Field("newPassword") String newPassword
        );

        @FormUrlEncoded
        @POST("api/Propietarios/email")
        Call<String> resetPassword(
                @Field("email") String email
        );

        //Obtener todos inmuebles
        @GET("/api/Inmuebles")
        Call<List<Inmueble>>getInmueble(
                @Header("Authorization") String token
        );

        //obtener inmueble con contrato vigente
        @GET("/api/Inmuebles/GetContratoVigente")
        Call<List<Inmueble>>getInmuebleContratoVigente(
                @Header("Authorization") String token
        );

        //Cargar inmueble (Con imagen)
        @Multipart
        @POST("/api/Inmuebles/cargar")
        Call<List<Inmueble>>createInmueble(
                @Header("token") String token,
                @Part MultipartBody.Part imagen,
                @Part("inmueble")RequestBody inmuebleJson
                //La respuesta es ´Inmueble´ no es una lista
        );

        //Actualizar inmueble
        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> updateInmueble(
                @Header("Authorization") String token,
                @Body Inmueble inmueble
        );

//        @GET("api/contratos/inmueble/{id}")
//        Call<Contrato> getContratoPorInmueble(
//                @Header("Authorization") String token, // Asumiendo que pasarás "Bearer <tu_token>"
//                @Path("id") int idInmueble // El ID del inmueble
//        );

//        // Obtener Pagos por Contrato

//        @GET("api/pagos/contrato/{id}")
//        Call<List<Pago>> getPagosPorContrato(
//                @Header("Authorization") String token, // Asumiendo que pasarás "Bearer <tu_token>"
//                @Path("id") int idContrato // El ID del contrato
//        );
    }

}
