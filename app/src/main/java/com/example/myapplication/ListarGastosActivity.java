package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
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
        AppDatabase db = AppDatabase.getInstance(this);

        Double total = db.gastoDao().somarTotal();
        total = (total == null) ? 0.0 : total;
        txtTotal.setText(String.format("Total Acumulado: R$ %.2f", total));

        configurarComparativoMensal(db);

        List<GastoDAO.CategoriaSoma> dadosGrafico = db.gastoDao().somarPorCategoria();
        List<PieEntry> entradas = new ArrayList<>();
        for (GastoDAO.CategoriaSoma dado : dadosGrafico) {
            entradas.add(new PieEntry((float) dado.total, dado.categoria));
        }

        PieDataSet dataSet = new PieDataSet(entradas, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.animateY(800);
        pieChart.invalidate();
    }

    private void configurarComparativoMensal(AppDatabase db) {
        TextView txtValorComp = findViewById(R.id.txt_valor_comparativo);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        long inicioMesAtual = cal.getTimeInMillis();
        long fimMesAtual = System.currentTimeMillis();

        cal.add(Calendar.MONTH, -1);
        long inicioMesPassado = cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        long fimMesPassado = cal.getTimeInMillis();

        Double gastoAtual = db.gastoDao().somarNoPeriodo(inicioMesAtual, fimMesAtual);
        Double gastoPassado = db.gastoDao().somarNoPeriodo(inicioMesPassado, fimMesPassado);

        gastoAtual = (gastoAtual == null) ? 0.0 : gastoAtual;
        gastoPassado = (gastoPassado == null) ? 0.0 : gastoPassado;

        double diferenca = gastoAtual - gastoPassado;

        if (gastoPassado == 0) {
            txtValorComp.setText("Primeiro mês de registos");
            txtValorComp.setTextColor(Color.GRAY);
        } else if (diferenca > 0) {
            txtValorComp.setText(String.format("▲ Gastou R$ %.2f a mais", diferenca));
            txtValorComp.setTextColor(Color.RED);
        } else {
            txtValorComp.setText(String.format("▼ Economizou R$ %.2f", Math.abs(diferenca)));
            txtValorComp.setTextColor(Color.parseColor("#2E7D32")); // Verde Escuro
        }
    }
}