package com.project.taskmanager.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
