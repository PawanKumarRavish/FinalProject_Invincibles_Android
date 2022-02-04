package com.project.taskmanager.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.taskmanager.date_converter.DateTypeConverter;
import com.project.taskmanager.db.dao.AddAccountDao;
import com.project.taskmanager.db.dao.AddExpenseDao;
import com.project.taskmanager.db.dao.AddIncomeDao;
import com.project.taskmanager.db.dao.LedgerDao;
import com.project.taskmanager.db.dao.SecurityQuestionDao;
import com.project.taskmanager.db.dao.TotalBalanceDao;
import com.project.taskmanager.db.entities.AddAccount;
import com.project.taskmanager.db.entities.AddExpense;
import com.project.taskmanager.db.entities.AddIncome;
import com.project.taskmanager.db.entities.Ledger;
import com.project.taskmanager.db.entities.SecurityQuestions;
import com.project.taskmanager.db.entities.TotalBalance;

/**
 * Created by admin on 05/03/2019.
 */

@Database(entities = {AddAccount.class, AddIncome.class, AddExpense.class, Ledger.class, TotalBalance.class, SecurityQuestions.class}, version = 1,exportSchema = false)
@TypeConverters({DateTypeConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    public abstract AddAccountDao addAccountDao();
    public abstract AddIncomeDao addIncomeDao();
    public abstract LedgerDao ledgerDao();
    public abstract TotalBalanceDao totalBalanceDao();
    public abstract SecurityQuestionDao securityQuestionDao();
    public abstract AddExpenseDao addExpenseDao();
}
