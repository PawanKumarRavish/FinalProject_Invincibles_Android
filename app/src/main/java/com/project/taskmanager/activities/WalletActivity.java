package com.project.taskmanager.activities;

import android.os.Bundle;

import com.project.taskmanager.SharedPreference;

public class WalletActivity extends BaseActivity {

    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference=new SharedPreference(this);
    }
}
