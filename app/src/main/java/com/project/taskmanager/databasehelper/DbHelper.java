package com.project.taskmanager.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.taskmanager.models.AddCategoryModel;
import com.project.taskmanager.models.AddTaskModel;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "task_db";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        // create categories table
        db.execSQL(AddCategoryModel.CREATE_TABLE);
        db.execSQL(AddTaskModel.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AddCategoryModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AddTaskModel.TABLE_NAME);

        // Create tables again
        onCreate(db);

    }



    public long insertCategory(String categoryName,String categoryDescription) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(AddCategoryModel.COLUMN_CATEGORY_NAME, categoryName);
        values.put(AddCategoryModel.COLUMN_CATEGORY_DESCRIPTION, categoryDescription);

        // insert row
        long id = db.insert(AddCategoryModel.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<AddCategoryModel> getAllCategories() {
        List<AddCategoryModel> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + AddCategoryModel.TABLE_NAME + " ORDER BY " +
                AddCategoryModel.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddCategoryModel note = new AddCategoryModel();
                note.setId(cursor.getInt(cursor.getColumnIndex(AddCategoryModel.COLUMN_ID)));
                note.setCategoryName(cursor.getString(cursor.getColumnIndex(AddCategoryModel.COLUMN_CATEGORY_NAME)));
                note.setCategoryDescription(cursor.getString(cursor.getColumnIndex(AddCategoryModel.COLUMN_CATEGORY_DESCRIPTION)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(AddCategoryModel.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public void deleteCategory(AddCategoryModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AddCategoryModel.TABLE_NAME, AddCategoryModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public int updateCategory(AddCategoryModel note,String categoryName,String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AddCategoryModel.COLUMN_CATEGORY_NAME, categoryName);
        values.put(AddCategoryModel.COLUMN_CATEGORY_DESCRIPTION, description);
        // updating row
        return db.update(AddCategoryModel.TABLE_NAME, values, AddCategoryModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }


    public long insertTask(String name,String des,String dueDate,String categoryName,int categoryId) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(AddTaskModel.COLUMN_TASK_NAME, name);
        values.put(AddTaskModel.COLUMN_TASK_DESCRIPTION, des);
        values.put(AddTaskModel.COLUMN_TASK_DUEDATE, dueDate);
        values.put(AddTaskModel.COLUMN_CATEGORY_NAME, categoryName);
        values.put(AddTaskModel.COLUMN_CATEGORY_ID, categoryId);

        // insert row
        long id = db.insert(AddTaskModel.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<AddTaskModel> getAllTasks() {
        List<AddTaskModel> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + AddTaskModel.TABLE_NAME + " ORDER BY " +
                AddTaskModel.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddTaskModel note = new AddTaskModel();
                note.setId(cursor.getInt(cursor.getColumnIndex(AddTaskModel.COLUMN_ID)));
                note.setTaskName(cursor.getString(cursor.getColumnIndex(AddTaskModel.COLUMN_TASK_NAME)));
                note.setTaskDescription(cursor.getString(cursor.getColumnIndex(AddTaskModel.COLUMN_TASK_DESCRIPTION)));
                note.setTaskDueDate(cursor.getString(cursor.getColumnIndex(AddTaskModel.COLUMN_TASK_DUEDATE)));
                note.setCategoryName(cursor.getString(cursor.getColumnIndex(AddTaskModel.COLUMN_CATEGORY_NAME)));
                note.setCategoryId(cursor.getInt(cursor.getColumnIndex(AddTaskModel.COLUMN_CATEGORY_ID)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(AddTaskModel.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public void deleteTask(AddTaskModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AddTaskModel.TABLE_NAME, AddTaskModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

}
