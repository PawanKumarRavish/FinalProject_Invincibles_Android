package com.project.taskmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddCategoryModel implements Parcelable {

    private int id;
    private String categoryName;
    private String  categoryDescription;
    private String timestamp;

    public static final String TABLE_NAME = "table_categories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_DESCRIPTION = "category_description";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CATEGORY_NAME + " TEXT,"
                    + COLUMN_CATEGORY_DESCRIPTION + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public AddCategoryModel(int id, String categoryName, String categoryDescription, String timestamp) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.timestamp = timestamp;
    }

    public AddCategoryModel() {
    }

    protected AddCategoryModel(Parcel in) {
        id = in.readInt();
        categoryName = in.readString();
        categoryDescription = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<AddCategoryModel> CREATOR = new Creator<AddCategoryModel>() {
        @Override
        public AddCategoryModel createFromParcel(Parcel in) {
            return new AddCategoryModel(in);
        }

        @Override
        public AddCategoryModel[] newArray(int size) {
            return new AddCategoryModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(categoryName);
        dest.writeString(categoryDescription);
        dest.writeString(timestamp);
    }
}
