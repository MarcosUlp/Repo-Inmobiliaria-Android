package com.example.practicoinmobiliariaandroid.ui.contratos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicoinmobiliariaandroid.R;
import com.example.practicoinmobiliariaandroid.data.model.Pago;

import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.PagoViewHolder> {

    private final List<Pago> lista;
    private final Context context;

    public PagosAdapter(List<Pago> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }
    //adapta la lista de pago a la tarjeta de pago (pago_card.xml) contiene logica para mostrar estado como
    //registrado o pendiente con colores
    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usaremos un layout simple para la tarjeta de pago (debes crearlo)
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.pago_card, parent, false);
        return new PagoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pago p = lista.get(position);

        holder.tvMontoPago.setText("$" + String.format("%,.2f", p.getMonto()));
        holder.tvFechaPago.setText("Fecha: " + p.getFechaPago());
        holder.tvDetallePago.setText(p.getDetalle());

        // Determinar el estado y color (asumiendo que 'estado' se usa para pagado/pendiente)
        // Usaremos el campo 'estado' del pago para determinar si está 'Registrado' o pendiente.
        // Si la API usa 'estado: false' para indicar 'No registrado/pendiente', ajustamos.
        // Asumimos que 'true' es PAGO y 'false' es PENDIENTE (o sin registrar).

        if (p.isEstado()) {
            holder.tvEstadoPago.setText("REGISTRADO");
            holder.tvEstadoPago.setTextColor(Color.parseColor("#4CAF50")); // Verde para pagado/registrado
        } else {
            holder.tvEstadoPago.setText("PENDIENTE");
            holder.tvEstadoPago.setTextColor(Color.RED); // Rojo para pendiente
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class PagoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMontoPago, tvFechaPago, tvDetallePago, tvEstadoPago;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMontoPago = itemView.findViewById(R.id.tvMontoPago);
            tvFechaPago = itemView.findViewById(R.id.tvFechaPago);
            tvDetallePago = itemView.findViewById(R.id.tvDetallePago);
            tvEstadoPago = itemView.findViewById(R.id.tvEstadoPago);
        }
    }
}