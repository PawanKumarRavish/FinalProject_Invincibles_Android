package com.project.taskmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.project.taskmanager.db.entities.AddAccount;

import java.util.List;

/**
 * Created by admin on 05/03/2019.
 */

@Dao
public interface AddAccountDao {

    @Query("SELECT * FROM AddAccount")
    List<AddAccount> getAll();

    @Insert
    void insert(AddAccount addAccount);

    @Delete
    void delete(AddAccount addAccount);

    @Update
    void update(AddAccount addAccount);

}
