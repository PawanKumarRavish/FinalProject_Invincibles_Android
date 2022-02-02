package com.project.taskmanager.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by admin on 07/03/2019.
 */


@Entity(tableName = "AddIncome")
public class AddIncome implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "fromm")
    private String fromm;

    @ColumnInfo(name = "mode")
    private String mode;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "account_id")
    private int accountId;

    @ColumnInfo(name = "created_at")
    private String createdDateTime;

    @ColumnInfo(name = "actual_mode")
    private String actualModeName;

    public AddIncome(String amount, String date, String fromm, String mode, String description,int type,int accountId,String createdDateTime,String actualModeName) {
        this.amount = amount;
        this.date = date;
        this.fromm = fromm;
        this.mode = mode;
        this.description = description;
        this.type = type;
        this.accountId = accountId;
        this.createdDateTime = createdDateTime;
        this.actualModeName = actualModeName;
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

    public String getFromm() {
        return fromm;
    }

    public void setFromm(String fromm) {
        this.fromm = fromm;
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
