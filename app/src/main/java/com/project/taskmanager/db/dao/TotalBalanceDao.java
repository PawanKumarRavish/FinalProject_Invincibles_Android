package com.project.taskmanager.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.project.taskmanager.db.entities.TotalBalance;

import java.util.List;

/**
 * Created by admin on 14/03/2019.
 */
@Dao
public interface TotalBalanceDao {

    @Query("SELECT * FROM TotalBalance")
    List<TotalBalance> getAll();

    @Insert
    void insert(TotalBalance totalBalance);

    @Insert
    void insertList(List<TotalBalance> totalBalanceList);

    @Delete
    void delete(TotalBalance totalBalance);

    @Update
    void update(TotalBalance totalBalance);


    @Query("SELECT * FROM TotalBalance WHERE mode = :mode")
    TotalBalance findByMode(String mode);

    @Query("SELECT id,mode,SUM(amount) as amount FROM TotalBalance")
    TotalBalance getTotalBalance();

    @Query("UPDATE totalbalance SET amount = (amount + (:amount)) WHERE mode=:mode")
    int updateDeletedBalance(Integer amount,String mode);




}
