package com.example.practicoinmobiliariaandroid.data.api;

import static com.example.practicoinmobiliariaandroid.utils.SessionManager.KEY_TOKEN;
import static com.example.practicoinmobiliariaandroid.utils.SessionManager.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.practicoinmobiliariaandroid.data.model.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.Cleaner;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class ApiClient {
    //aca vamos a configurar la conexion a la api
    private static final String BASE_URL = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net"; // la que te den
    private static Retrofit retrofit;

    public static ApiService getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Retorna la instancia de la interfaz de servicio
        return retrofit.create(ApiService.class);
    }
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // 📖 Leer token JWT
    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString(KEY_TOKEN, null);
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

    }

}
