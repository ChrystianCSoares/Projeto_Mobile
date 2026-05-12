package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
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

                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Insira um valor numérico válido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}