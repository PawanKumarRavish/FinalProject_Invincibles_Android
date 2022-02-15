package com.project.taskmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddSubTaskModel implements Parcelable {

    private int id;
    private int taskId;
    private String subTaskName;
    private String  description;
    private String  subTaskDueDate;
    private String  isSubTaskCompleted;
    private String timestamp;

    public static final String TABLE_NAME = "table_subtask";
    public static final String COLUMN_ID = "id";
    public static final String TASK_ID = "task_id";
    public static final String COLUMN_SUB_TASK_NAME = "subtask_name";
    public static final String COLUMN_SUBTASK_DESCRIPTION = "subtask_description";
    public static final String COLUMN_SUBTASK_DUEDATE = "subtask_duedate";
    public static final String COLUMN_IS_SUBTASK_COMPLETED = "subtask_completed";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TASK_ID + " INTEGER,"
                    + COLUMN_SUB_TASK_NAME + " TEXT,"
                    + COLUMN_SUBTASK_DUEDATE + " TEXT,"
                    + COLUMN_SUBTASK_DESCRIPTION + " TEXT,"
                    + COLUMN_IS_SUBTASK_COMPLETED + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


    public AddSubTaskModel(int id, int taskId, String subTaskName, String description, String subTaskDueDate,String isSubTaskCompleted, String timestamp) {
        this.id = id;
        this.taskId = taskId;
        this.subTaskName = subTaskName;
        this.description = description;
        this.subTaskDueDate = subTaskDueDate;
        this.isSubTaskCompleted = isSubTaskCompleted;
        this.timestamp = timestamp;
    }

    public AddSubTaskModel(){

    }

    protected AddSubTaskModel(Parcel in) {
        id = in.readInt();
        taskId = in.readInt();
        subTaskName = in.readString();
        description = in.readString();
        subTaskDueDate = in.readString();
        isSubTaskCompleted = in.readString();
        timestamp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(taskId);
        dest.writeString(subTaskName);
        dest.writeString(description);
        dest.writeString(subTaskDueDate);
        dest.writeString(isSubTaskCompleted);
        dest.writeString(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddSubTaskModel> CREATOR = new Creator<AddSubTaskModel>() {
        @Override
        public AddSubTaskModel createFromParcel(Parcel in) {
            return new AddSubTaskModel(in);
        }

        @Override
        public AddSubTaskModel[] newArray(int size) {
            return new AddSubTaskModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubTaskDueDate() {
        return subTaskDueDate;
    }

    public void setSubTaskDueDate(String subTaskDueDate) {
        this.subTaskDueDate = subTaskDueDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsSubTaskCompleted() {
        return isSubTaskCompleted;
    }

    public void setIsSubTaskCompleted(String isSubTaskCompleted) {
        this.isSubTaskCompleted = isSubTaskCompleted;
    }
}
