package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListarGastosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_gastos);

        RecyclerView recyclerView = findViewById(R.id.recycler_gastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Busca os dados e configura a lista
        List<Gasto> listaDoBanco = AppDatabase.getInstance(this).gastoDao().listarTodos();
        GastoAdapter adapter = new GastoAdapter(listaDoBanco);
        recyclerView.setAdapter(adapter);

        // Configura o texto do Total inicial
        TextView txtTotal = findViewById(R.id.txt_total_gastos);
        Double total = AppDatabase.getInstance(this).gastoDao().somarTotal();
        if (total == null) {
            total = 0.0;
        }
        txtTotal.setText(String.format("Total: R$ %.2f", total));

        // --- LÓGICA: SWIPE TO DELETE ---
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Gasto gastoDeletado = adapter.getGastoAt(position);

                // Deleta do banco
                AppDatabase.getInstance(ListarGastosActivity.this).gastoDao().deletar(gastoDeletado);

                // Remove da tela
                adapter.removerItem(position);

                // Atualiza o Total
                Double novoTotal = AppDatabase.getInstance(ListarGastosActivity.this).gastoDao().somarTotal();
                if (novoTotal == null) {
                    novoTotal = 0.0;
                }
                txtTotal.setText(String.format("Total: R$ %.2f", novoTotal));
            }
        }).attachToRecyclerView(recyclerView);
    }
}