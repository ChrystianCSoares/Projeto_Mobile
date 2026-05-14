package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdicionarGastoActivity extends AppCompatActivity {

    private Spinner spinnerCategoria;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_gasto);

        EditText editDescricao = findViewById(R.id.edit_descricao);
        EditText editValor = findViewById(R.id.edit_valor);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        Button btnSalvar = findViewById(R.id.btn_salvar_gasto);

        btnSalvar.setOnClickListener(v -> {
            String descricao = editDescricao.getText().toString();
            String valorString = editValor.getText().toString();
            String categoria = spinnerCategoria.getSelectedItem().toString();

            if (descricao.isEmpty() || valorString.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double valor = Double.parseDouble(valorString);
                Gasto novoGasto = new Gasto(descricao, valor, categoria);

                AppDatabase.getInstance(this).gastoDao().inserir(novoGasto);
                Toast.makeText(this, "Gasto guardado em " + categoria, Toast.LENGTH_SHORT).show();

                verificarLimitesEFechar(categoria);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Insira um valor numérico válido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verificarLimitesEFechar(String categoriaInserida) {
        AppDatabase db = AppDatabase.getInstance(this);
        boolean ultrapassouLimite = false;
        String mensagemAlerta = "";

        Limite limiteCat = db.limiteDao().buscarLimitePorCategoria(categoriaInserida);
        if (limiteCat != null) {
            Double gastoNaCategoria = db.gastoDao().somarPorUmaCategoria(categoriaInserida);
            if (gastoNaCategoria != null && gastoNaCategoria > limiteCat.getValorLimite()) {
                ultrapassouLimite = true;
                mensagemAlerta = "Você ultrapassou o limite definido para a categoria: " + categoriaInserida + "!\n(Limite: R$ " + limiteCat.getValorLimite() + ")";
            }
        }

        Limite limiteTotal = db.limiteDao().buscarLimitePorCategoria("Total");
        if (limiteTotal != null) {
            Double totalGeral = db.gastoDao().somarTotal();
            if (totalGeral != null && totalGeral > limiteTotal.getValorLimite()) {
                ultrapassouLimite = true;

                if (!mensagemAlerta.isEmpty()) {
                    mensagemAlerta += "\n\nE também gastou mais do que o seu limite mensal total! (Limite: R$ " + limiteTotal.getValorLimite() + ")";
                } else {
                    mensagemAlerta = "Atenção: Você gastou mais do que o seu limite mensal total!\n(Limite: R$ " + limiteTotal.getValorLimite() + ")";
                }
            }
        }

        if (ultrapassouLimite) {
            new AlertDialog.Builder(this)
                    .setTitle("⚠️ Alerta de Orçamento!")
                    .setMessage(mensagemAlerta)
                    .setPositiveButton("Entendi", (dialog, which) -> {
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        } else {
            finish();
        }
    }
}