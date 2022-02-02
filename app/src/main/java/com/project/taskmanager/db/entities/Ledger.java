package com.project.taskmanager.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by admin on 11/03/2019.
 */

@Entity(tableName = "Ledger")
public class Ledger implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "ledger_amount")
    private int amount;

    @ColumnInfo(name = "ledger_date")
    private String date;

    @ColumnInfo(name = "ledger_accountName")
    private String from;

    @ColumnInfo(name = "ledger_mode")
    private String mode;

    @ColumnInfo(name = "ledger_description")
    private String description;

    @ColumnInfo(name = "ledger_type")
    private int type;

    @ColumnInfo(name = "ledger_account_id")
    private int accountId;

    @ColumnInfo(name = "created_at")
    private String createdDateTime;

    @ColumnInfo(name = "actual_mode")
    private String actualModeName;


    public Ledger(int amount, String date, String from, String mode, String description, int type, int accountId, String createdDateTime,String actualModeName) {
        this.amount = amount;
        this.date = date;
        this.from = from;
        this.mode = mode;
        this.description = description;
        this.type = type;
        this.accountId = accountId;
        this.createdDateTime = createdDateTime;
        this.actualModeName= actualModeName;
    }

    public Ledger() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getActualModeName() {
        return actualModeName;
    }

    public void setActualModeName(String actualModeName) {
        this.actualModeName = actualModeName;
    }
}
