package com.project.taskmanager.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by admin on 05/03/2019.
 */

@Entity(tableName = "AddAccount")
public class AddAccount implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "accountName")
    private String accountName;

    @ColumnInfo(name = "accountCategory")
    private String accountCategory;


    @ColumnInfo(name = "created_at")
    private String createdDateTime;

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public AddAccount(String accountName, String accountCategory,String createdDateTime) {
        this.accountName = accountName;
        this.accountCategory = accountCategory;
        this.createdDateTime = createdDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }
}
