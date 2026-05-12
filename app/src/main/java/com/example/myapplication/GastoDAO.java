package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

    @Dao
    public interface GastoDAO {

        @Insert
        void inserir(Gasto gasto);

        @Query("SELECT * FROM gastos_table ORDER BY id DESC")
        List<Gasto> listarTodos();
        @Query("SELECT SUM(valor) FROM gastos_table")
        Double somarTotal();
        @Delete
        void deletar(Gasto gasto);
    }
