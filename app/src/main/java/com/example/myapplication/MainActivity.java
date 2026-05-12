package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdicionar = findViewById(R.id.btn_menu_adicionar);
        Button btnListar = findViewById(R.id.btn_menu_listar);

        btnAdicionar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdicionarGastoActivity.class);
            startActivity(intent);
        });

        btnListar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListarGastosActivity.class);
            startActivity(intent);
        });
    }
}