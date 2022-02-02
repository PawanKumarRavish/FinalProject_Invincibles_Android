package com.project.taskmanager.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.project.taskmanager.db.entities.AddExpense;

import java.util.List;

/**
 * Created by admin on 07/03/2019.
 */
@Dao
public interface AddExpenseDao {

    @Query("SELECT * FROM AddExpense")
    List<AddExpense> getAll();

    @Insert
    void insert(AddExpense addExpense);

    @Delete
    void delete(AddExpense addExpense);

    @Update
    void update(AddExpense addExpense);
}
