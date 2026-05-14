package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LimiteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void salvarLimite(Limite limite);

    @Query("SELECT * FROM limites_table WHERE categoria = :cat")
    Limite buscarLimitePorCategoria(String cat);
}