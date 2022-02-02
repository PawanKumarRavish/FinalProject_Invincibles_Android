package com.project.taskmanager.models;

/**
 * Created by admin on 05/03/2019.
 */

public class AccountCategoryModel {

    String accountName;

    public AccountCategoryModel(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
