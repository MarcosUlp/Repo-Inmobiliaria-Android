package com.example.practicoinmobiliariaandroid.ui.contratos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.data.api.ApiClient; // IMPORTADO
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;

import java.util.List;
//en esta clase adaptamos la lista de inmuebles a las tarjetas del RecyclerView.
//
public class ContratoInmuebleAdapter extends RecyclerView.Adapter<ContratoInmuebleAdapter.ContratoInmuebleViewHolder> {
    private final List<Inmueble> lista;
    private final Context context;
    private final ContratosVigentesFragment fragment; // Referencia al fragmento para el click

    // ❌ ELIMINADO: private static final String URL_BASE = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net";

    public ContratoInmuebleAdapter(List<Inmueble> lista, Context context, ContratosVigentesFragment fragment) {
        this.lista = lista;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ContratoInmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos el nuevo layout de tarjeta de contrato
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.contrato_inmueble_card, parent, false);
        return new ContratoInmuebleViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ContratoInmuebleViewHolder holder, int position) {
        Inmueble i = lista.get(position);

        holder.tvDireccion.setText(i.getDireccion());

        // Muestra Tipo y Uso combinados
        String tipoUso = i.getTipo() + " (" + i.getUso() + ")";
        holder.tvTipoUso.setText(tipoUso);

        holder.tvPrecio.setText("$" + String.format("%,.2f", i.getValor()));

        // Manejo de la URL de la imagen (corregido el problema de los backslashes)
        if (i.getImagen() != null && !i.getImagen().isEmpty()) {
            String relativePath = i.getImagen().replace("\\", "/");

            // ✨ CAMBIO CLAVE: Usar el método centralizado y asegurar la barra diagonal
            String baseUrl = ApiClient.getBaseUrl();
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }
            String imageUrl = i.getImagen().startsWith("http") ? i.getImagen() : baseUrl + relativePath;

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.inmueble_defecto)
                    .error(R.drawable.inmueble_defecto)
                    .into(holder.imgInmueble);
        } else {
            holder.imgInmueble.setImageResource(R.drawable.inmueble_defecto);
        }

        // IMPLEMENTACIÓN DEL CLICK LISTENER (llama al método del Fragment)
        holder.itemView.setOnClickListener(v -> {
            fragment.onInmuebleClick(i);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ContratoInmuebleViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvDireccion, tvTipoUso, tvPrecio;
        private final ImageView imgInmueble;

        public ContratoInmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de que estos IDs coincidan con contrato_inmueble_card.xml
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipoUso = itemView.findViewById(R.id.tvTipoUso);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            imgInmueble = itemView.findViewById(R.id.imgInmueble);
        }
    }
}