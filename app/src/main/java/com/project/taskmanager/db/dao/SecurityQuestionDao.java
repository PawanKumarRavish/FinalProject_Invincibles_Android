package com.project.taskmanager.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.project.taskmanager.db.entities.SecurityQuestions;

import java.util.List;

/**
 * Created by admin on 07/03/2019.
 */
@Dao
public interface SecurityQuestionDao {

    @Query("SELECT * FROM SecurityQuestions")
    List<SecurityQuestions> getAll();

    @Insert
    void insert(SecurityQuestions securityQuestions);

    @Delete
    void delete(SecurityQuestions securityQuestions);

    @Update
    void update(SecurityQuestions securityQuestions);


}
