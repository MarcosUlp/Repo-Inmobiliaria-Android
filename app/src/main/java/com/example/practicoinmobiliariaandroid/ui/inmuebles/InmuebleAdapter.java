package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {

    private List<Inmueble> lista;
    private Context context;

    // URL base del backend
    private static final String URL_BASE = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";

    public InmuebleAdapter(List<Inmueble> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inmueble_card, parent, false);
        return new InmuebleViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        Inmueble i = lista.get(position);

        holder.tvDireccion.setText(i.getDireccion());
        holder.tvTipo.setText(i.getTipo());
        holder.tvPrecio.setText("$" + String.valueOf(i.getValor()));

        // ✅ Normalizar URL de imagen (idéntico al detalle)
        String imageUrl = null;
        if (i.getImagen() != null && !i.getImagen().isEmpty()) {
            // 1. Reemplazar '\' por '/'
            String relativePath = i.getImagen().replace("\\", "/");

            // 2. Construir URL final
            if (relativePath.startsWith("http")) {
                imageUrl = relativePath;
            } else {
                // Asegurar que la base tenga '/' al final
                String baseUrl = URL_BASE.endsWith("/") ? URL_BASE : URL_BASE + "/";
                imageUrl = baseUrl + relativePath;
            }
        }

        // 3. Cargar imagen con Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.inmueble_defecto)
                .error(R.drawable.inmueble_defecto)
                .into(holder.imgInmueble);

        // 🔗 Click: navegar al detalle
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmuebleData", i);
            Navigation.findNavController(v).navigate(R.id.nav_detalle_inmueble, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class InmuebleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDireccion, tvTipo, tvPrecio;
        private ImageView imgInmueble;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            imgInmueble = itemView.findViewById(R.id.imgInmueble);
        }
    }
}
