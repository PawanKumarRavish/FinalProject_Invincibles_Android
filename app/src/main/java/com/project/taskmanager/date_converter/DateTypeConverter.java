package com.project.taskmanager.date_converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by admin on 08/03/2019.
 */

public class DateTypeConverter {

    @TypeConverter
    public long convertDateToLong(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(long time) {
        return new Date(time);
    }
}
