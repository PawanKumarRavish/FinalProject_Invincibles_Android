package com.project.taskmanager.db.dao;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.project.taskmanager.db.entities.AddIncome;

import java.util.List;

/**
 * Created by admin on 07/03/2019.
 */
@Dao
public interface AddIncomeDao {

    @Query("SELECT * FROM AddIncome")
    List<AddIncome> getAll();

    @Insert
    void insert(AddIncome addIncome);

    @Delete
    void delete(AddIncome addIncome);

    @Update
    void update(AddIncome addIncome);

    @Query("SELECT * FROM AddIncome ORDER BY created_at DESC")
    List<AddIncome> getRecentTxn();


    @Query("SELECT * FROM AddIncome WHERE type = :type ORDER BY created_at DESC")
    List<AddIncome> getTxnListByType(int type);


    @Query("SELECT SUM(amount) AS Amount FROM AddIncome WHERE type =:type")
    int getTotalExpenseBalance(int type);

    @Query("SELECT id,fromm,mode,description,type,account_id,actual_mode,created_at,date, SUM(amount) AS amount FROM AddIncome WHERE type=:type AND date(date)>=date('now','start of month') group by date order by date desc")
    List<AddIncome> getLatestTxns(int type);


   /* @Query("SELECT id,fromm,mode,description,type,account_id,created_at,date, SUM(amount) AS amount FROM AddIncome WHERE type=:type AND date(date)>=date('now','start of year',':2 month') AND date(date)<=date('now','start of year',':3 month','-1 day') group by date order by date desc")
    List<AddIncome> getDropDownMonthTxns(int type);*/

    @RawQuery
    List<AddIncome> getDropDownMonthTxns(SupportSQLiteQuery supportSQLiteQuery);

    @Query("SELECT id,type,account_id,fromm,date,actual_mode,description,SUM(amount) AS amount FROM AddIncome WHERE type=:type  GROUP BY fromm ORDER BY id DESC limit :numOfRows")
    List<AddIncome> getLastNDaysReport(int type,int numOfRows);



}
