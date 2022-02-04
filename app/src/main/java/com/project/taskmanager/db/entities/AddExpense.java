package com.project.taskmanager.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by admin on 07/03/2019.
 */
@Entity(tableName = "AddExpense")
public class AddExpense implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "to")
    private String from;

    @ColumnInfo(name = "mode")
    private String mode;

    @ColumnInfo(name = "description")
    private String description;

    public AddExpense(String amount, String date, String from, String mode, String description) {
        this.amount = amount;
        this.date = date;
        this.from = from;
        this.mode = mode;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
