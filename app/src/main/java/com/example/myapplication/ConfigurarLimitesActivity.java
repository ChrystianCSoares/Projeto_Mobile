package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigurarLimitesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_limites);

        EditText editLimiteTotal = findViewById(R.id.edit_limite_total);
        EditText editLimiteCategoria = findViewById(R.id.edit_limite_categoria);
        Spinner spinnerCategoria = findViewById(R.id.spinner_limite_categoria);
        Button btnSalvar = findViewById(R.id.btn_salvar_configuracoes);

        btnSalvar.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(this);
            String totalStr = editLimiteTotal.getText().toString();
            String categoriaStr = editLimiteCategoria.getText().toString();
            String categoriaSelecionada = spinnerCategoria.getSelectedItem().toString();

            boolean algoSalvo = false;

            if (!totalStr.isEmpty()) {
                double valorTotal = Double.parseDouble(totalStr);
                db.limiteDao().salvarLimite(new Limite("Total", valorTotal));
                algoSalvo = true;
            }

            if (!categoriaStr.isEmpty()) {
                double valorCategoria = Double.parseDouble(categoriaStr);
                db.limiteDao().salvarLimite(new Limite(categoriaSelecionada, valorCategoria));
                algoSalvo = true;
            }

            if (algoSalvo) {
                Toast.makeText(this, "Limites guardados com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Preencha pelo menos um dos limites", Toast.LENGTH_SHORT).show();
            }
        });
    }
}