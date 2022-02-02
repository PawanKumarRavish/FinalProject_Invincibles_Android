package com.project.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.taskmanager.interfaces.Tags;
import com.project.taskmanager.models.ModeModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 05/03/2019.
 */

public class SharedPreference {

    public static String My_Prefrences = "Expense Prefrences";
    static SharedPreferences mPref;
    SharedPreferences.Editor editor;

    public SharedPreference(Context mContext) {
        mPref = mContext.getSharedPreferences(My_Prefrences, Context.MODE_PRIVATE);
        editor = mPref.edit();
    }




    public static String getUserPin() {
        return mPref.getString(Tags.USER_PIN, "");
    }

    public static void setUserPin(String pin) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(Tags.USER_PIN, pin);
        editor.commit();
    }

    public static String getCurrency() {
        return mPref.getString(Tags.CURRENCY, "$");
    }

    public static void setCurrency(String currency) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(Tags.CURRENCY, currency);
        editor.commit();
    }

    public static String getName() {
        return mPref.getString(Tags.NAME, "");
    }

    public static void setName(String name) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(Tags.NAME, name);
        editor.commit();
    }


    public static String getFileName() {
        return mPref.getString(Tags.FILE_NAME, "");
    }

    public static void setFileName(String fileName) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(Tags.FILE_NAME, fileName);
        editor.commit();
    }

    public static void setLogin() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(Tags.IS_LOGIN, true);
        editor.commit();
    }
    public static void resetLogin() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(Tags.IS_LOGIN, false);
        editor.commit();
    }
    public static boolean isLogin() {
        return mPref.getBoolean(Tags.IS_LOGIN,false);
    }



    public static boolean isFirstTime() {
        return mPref.getBoolean(Tags.IS_FIRST_TIME,false);
    }

    public static void setFirstTime() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(Tags.IS_FIRST_TIME, true);
        editor.commit();
    }


    public static boolean isCount() {
        return mPref.getBoolean(Tags.COUNT,false);
    }

    public static void setCount() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(Tags.COUNT, true);
        editor.commit();
    }



    public static void setQuestionsAvailable() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(Tags.IS_QUESTIONS_AVAILABLE, true);
        editor.commit();
    }

    public static boolean isQuestionsAvailable() {
        return mPref.getBoolean(Tags.IS_QUESTIONS_AVAILABLE,false);
    }


    public static void setBankAndModeList(ArrayList<ModeModel> bankAndModeList) {
        SharedPreferences.Editor editor = mPref.edit();
        Gson gson=new Gson();
        Type type = new TypeToken<ArrayList<ModeModel>>() {}.getType();
        String json=gson.toJson(bankAndModeList,type);
        editor.putString(Tags.BANK_AND_MODE_LIST, json);
        editor.commit();
    }

    public static ArrayList<ModeModel> getBankAndModeList() {
        String json= mPref.getString(Tags.BANK_AND_MODE_LIST, "");
        Gson gson=new Gson();
        Type type = new TypeToken<ArrayList<ModeModel>>() {}.getType();
        ArrayList<ModeModel> list = (ArrayList<ModeModel>) gson.fromJson(json, type);
        if(list==null){
            list= new ArrayList<>();
            list.add(new ModeModel(Tags.CASH));
        }
        return list;
    }

}
