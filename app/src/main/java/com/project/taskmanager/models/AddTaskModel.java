package com.project.taskmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddTaskModel implements Parcelable {
    private int id;
    private String taskName;
    private String  taskDescription;
    private String  taskDueDate;
    private String  categoryName;
    private int  categoryId;
    private String timestamp;

    public static final String TABLE_NAME = "table_task";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_TASK_DESCRIPTION = "task_description";
    public static final String COLUMN_TASK_DUEDATE = "task_duedate";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TASK_NAME + " TEXT,"
                    + COLUMN_TASK_DUEDATE + " TEXT,"
                    + COLUMN_TASK_DESCRIPTION + " TEXT,"
                    + COLUMN_CATEGORY_NAME + " TEXT,"
                    + COLUMN_CATEGORY_ID + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public AddTaskModel(int id, String taskName, String taskDescription, String taskDueDate, String categoryName,int categoryId,String timestamp) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDueDate = taskDueDate;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.timestamp = timestamp;
    }

    public AddTaskModel() {

    }


    protected AddTaskModel(Parcel in) {
        id = in.readInt();
        taskName = in.readString();
        taskDescription = in.readString();
        taskDueDate = in.readString();
        categoryName = in.readString();
        categoryId = in.readInt();
        timestamp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(taskName);
        dest.writeString(taskDescription);
        dest.writeString(taskDueDate);
        dest.writeString(categoryName);
        dest.writeInt(categoryId);
        dest.writeString(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddTaskModel> CREATOR = new Creator<AddTaskModel>() {
        @Override
        public AddTaskModel createFromParcel(Parcel in) {
            return new AddTaskModel(in);
        }

        @Override
        public AddTaskModel[] newArray(int size) {
            return new AddTaskModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(String taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
