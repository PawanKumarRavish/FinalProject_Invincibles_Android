package com.project.taskmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
