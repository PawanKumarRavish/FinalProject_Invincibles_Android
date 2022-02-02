package com.project.taskmanager.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by admin on 14/03/2019.
 */
@Entity(tableName = "TotalBalance",indices = {@Index(value = {"mode", "amount"},
        unique = true)})
public class TotalBalance {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "mode")
    private String mode;

    @ColumnInfo(name = "amount")
    private int amount;


    public TotalBalance(String mode, int amount) {
        this.mode = mode;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
