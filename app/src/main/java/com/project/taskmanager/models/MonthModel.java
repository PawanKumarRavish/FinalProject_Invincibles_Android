package com.project.taskmanager.models;

/**
 * Created by Gaganjot Singh on 19/03/2019.
 */

public class MonthModel {

    int monthId;
    String monthName;

    public MonthModel(int monthId, String monthName) {
        this.monthId = monthId;
        this.monthName = monthName;
    }

    public int getMonthId() {
        return monthId;
    }

    public void setMonthId(int monthId) {
        this.monthId = monthId;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }
}
