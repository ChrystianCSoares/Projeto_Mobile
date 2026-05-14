package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "limites_table")
public class Limite {
    @PrimaryKey
    @NonNull
    private String categoria;
    private double valorLimite;

    public Limite(@NonNull String categoria, double valorLimite) {
        this.categoria = categoria;
        this.valorLimite = valorLimite;
    }

    @NonNull
    public String getCategoria() { return categoria; }
    public double getValorLimite() { return valorLimite; }
}