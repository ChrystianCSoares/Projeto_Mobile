package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Importações da biblioteca de gráficos
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ListarGastosActivity extends AppCompatActivity {

    private GastoAdapter adapter;
    private TextView txtTotal;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_gastos);

        txtTotal = findViewById(R.id.txt_total_gastos);
        pieChart = findViewById(R.id.grafico_pizza);

        RecyclerView recyclerView = findViewById(R.id.recycler_gastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Gasto> listaDoBanco = AppDatabase.getInstance(this).gastoDao().listarTodos();
        adapter = new GastoAdapter(listaDoBanco);
        recyclerView.setAdapter(adapter);

        atualizarTela();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Gasto gastoDeletado = adapter.getGastoAt(position);

                AppDatabase.getInstance(ListarGastosActivity.this).gastoDao().deletar(gastoDeletado);

                adapter.removerItem(position);

                atualizarTela();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void atualizarTela() {
        Double total = AppDatabase.getInstance(this).gastoDao().somarTotal();
        if (total == null) {
            total = 0.0;
        }
        txtTotal.setText(String.format("Total: R$ %.2f", total));

        List<GastoDAO.CategoriaSoma> dadosGrafico = AppDatabase.getInstance(this).gastoDao().somarPorCategoria();

        List<PieEntry> entradas = new ArrayList<>();
        for (GastoDAO.CategoriaSoma dado : dadosGrafico) {
            entradas.add(new PieEntry((float) dado.total, dado.categoria));
        }

        PieDataSet dataSet = new PieDataSet(entradas, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Despesas");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}