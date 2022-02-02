package com.project.taskmanager.models;

/**
 * Created by admin on 11/03/2019.
 */

public class LedgerModel {

    String accountName;
    int accountId;
    int amount;

    public LedgerModel(String accountName, int accountId, int amount) {
        this.accountName = accountName;
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
