package com.example.practicoinmobiliariaandroid.ui.inmuebles;

import android.content.Context;
import android.os.Bundle; // Necesario para argumentos
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation; // Necesario para la navegación
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.data.model.Inmueble;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {
    private List<Inmueble> lista;
    private Context context;

    public InmuebleAdapter(List<Inmueble> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.inmueble_card, parent, false);
        return new InmuebleViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        String urlBase = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net";
        Inmueble i = lista.get(position);
        holder.tvDireccion.setText(i.getDireccion());
        holder.tvTipo.setText(i.getTipo());
        holder.tvPrecio.setText("$"+String.valueOf(i.getValor()));

        Glide.with(context)
                .load(urlBase + i.getImagen())
                .placeholder(R.drawable.inmueble_defecto)
                // Usar un recurso para el error es mejor que un string "null"
                .error("null")
                .into(holder.imgInmueble);

        // IMPLEMENTACIÓN DEL CLICK LISTENER
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            // El nombre de la clave debe coincidir con el argType definido en mobile_navigation.xml
            bundle.putSerializable("inmuebleData", i);

            // Navega a la ID del destino de detalle
            Navigation.findNavController(v).navigate(R.id.nav_detalle_inmueble, bundle);
        });
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class InmuebleViewHolder extends RecyclerView.ViewHolder{
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