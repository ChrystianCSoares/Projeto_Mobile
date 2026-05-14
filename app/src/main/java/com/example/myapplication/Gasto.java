package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gastos_table")
public class Gasto {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String descricao;
    private double valor;
    private String categoria;
    private long dataTimestamp;

    public Gasto(String descricao, double valor, String categoria) {
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.dataTimestamp = System.currentTimeMillis();
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public long getDataTimestamp() { return dataTimestamp; }
    public void setDataTimestamp(long dataTimestamp) { this.dataTimestamp = dataTimestamp; }
}