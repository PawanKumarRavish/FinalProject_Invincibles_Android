package com.project.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.project.taskmanager.interfaces.Tags.BANK;
import static com.project.taskmanager.interfaces.Tags.CASH;

/**
 * Created by admin on 04/03/2019.
 */

public class Utils {
    public static boolean isSingelton=true;

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static String fetchMode(String input){
        return input.equalsIgnoreCase(CASH)?CASH:BANK;
    }

    public static  void getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String toDbTimeStamp(Date date){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        String strDate = sdfDate.format(date);
        return strDate;
    }




    public static String getDayInString(String date){
        String retVal="";
        SimpleDateFormat SDF_FULL = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat SDF_DAY = new SimpleDateFormat("dd");
        try {
            retVal= SDF_DAY.format(SDF_FULL.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static String getMonthInString(String date){
        String retVal="";
        SimpleDateFormat SDF_FULL = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat SDF_DAY = new SimpleDateFormat("MMM");
        try {
            retVal= SDF_DAY.format(SDF_FULL.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static String  getYearInString(String date){
        String retVal="";
        SimpleDateFormat SDF_FULL = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy");
        try {
            retVal= SDF_DAY.format(SDF_FULL.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static String getFormattedAmount(String amount){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String output = formatter.format(Double.parseDouble(amount));
        return output;
    }

    public static String getAmountInDecimal(String amount) {
        BigDecimal xmlvalue = new BigDecimal(amount);
        xmlvalue = xmlvalue.setScale(2, RoundingMode.HALF_DOWN);
        String str1 = xmlvalue.toString();
        return str1;
    }

    public  static void showWarningAlert(Context context,String title){
        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.setCustomImage(R.drawable.ic_warning);
        sweetAlertDialog.show();

        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#0786e5"));
    }

}
