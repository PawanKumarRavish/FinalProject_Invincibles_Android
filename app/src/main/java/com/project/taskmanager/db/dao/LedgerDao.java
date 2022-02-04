package com.project.taskmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;
import com.project.taskmanager.db.entities.Ledger;

import java.util.List;

/**
 * Created by admin on 11/03/2019.
 */

@Dao
public interface LedgerDao {

    @Query("SELECT * FROM Ledger")
    List<Ledger> getAll();

    @Insert
    void insert(Ledger ledger);

    @Delete
    int delete(Ledger ledger);

    @Update
    void update(Ledger ledger);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id,ledger_accountName,SUM(ledger_amount) as ledger_amount ,ledger_date,ledger_mode,ledger_description,ledger_type,ledger_account_id,created_at FROM Ledger GROUP BY ledger_accountName ORDER BY id DESC")
    List<Ledger> getLedgerHistory();

    @Query("SELECT * FROM Ledger WHERE  ledger_accountName = :accountName")
    List<Ledger> findByAccountName(String accountName);

    @Query("SELECT SUM(ledger_amount) AS Amount FROM ledger WHERE ledger_mode = :mode AND  ledger_accountName=:accountName")
    Integer getSumByModeAndAccountName(String mode,String accountName);

    @Query("SELECT distinct ledger_mode FROM ledger WHERE ledger_accountName=:accountName")
    List<String> findModeListByAccountName(String accountName);

    @Query("DELETE FROM  ledger WHERE  ledger_accountName=:accountName")
    int deleteByAccountName(String accountName);

    @Query("DELETE FROM ledger WHERE  id = :id")
    int deleteById(int id);

}
