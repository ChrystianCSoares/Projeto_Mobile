package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<Gasto> listaGastos;

    public GastoAdapter(List<Gasto> listaGastos) {
        this.listaGastos = listaGastos;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = listaGastos.get(position);

        holder.txtDescricao.setText(gasto.getDescricao());
        holder.txtValor.setText("R$ " + String.format("%.2f", gasto.getValor()));
        holder.txtCategoria.setText(gasto.getCategoria());
    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }


    public Gasto getGastoAt(int position) {
        return listaGastos.get(position);
    }


    public void removerItem(int position) {
        listaGastos.remove(position);
        notifyItemRemoved(position);
    }


    public static class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtValor, txtCategoria;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txt_item_descricao);
            txtValor = itemView.findViewById(R.id.txt_item_valor);
            txtCategoria = itemView.findViewById(R.id.txt_item_categoria);
        }
    }
}