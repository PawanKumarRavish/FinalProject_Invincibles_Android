package com.project.taskmanager.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.taskmanager.R;
import com.project.taskmanager.Utils;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.interfaces.Tags;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddSubTaskFrg extends BaseFragment {

    DbHelper dbHelper;

    @BindView(R.id.mSubTaskNameTv)
    EditText mSubTaskNameTv;

    @BindView(R.id.description_et)
    EditText descriptionEt;

    @BindView(R.id.date_tv)
    TextView mDateTv;

    @BindView(R.id.dateLayout)
    RelativeLayout mDateLayout;

    @BindView(R.id.saveBtn)
    Button mSaveBtn;

    private int mYear, mMonth, mDay;
    private Calendar selectedCal;
    int taskId=0;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_sub_task_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DbHelper(getActivity());

        Bundle bundle=getArguments();
        if(bundle!=null){
            taskId=bundle.getInt(Tags.TASK_ID);
        }

        mDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    String fmonth, fDate;
                    int month;

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        try {
                            if (monthOfYear < 10 && dayOfMonth < 10) {

                                fmonth = "0" + monthOfYear;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                mDateTv.setText(fDate + "-" + paddedMonth + "-" + year);

                            } else {

                                fmonth = "0" + monthOfYear;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                mDateTv.setText(dayOfMonth + "-" + paddedMonth + "-" + year);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        // mDateTv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        selectedCal = Calendar.getInstance();
                        selectedCal.set(Calendar.YEAR, year);
                        selectedCal.set(Calendar.MONTH, monthOfYear);
                        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSubTaskNameTv.getText().toString().trim().isEmpty()
                        ||descriptionEt.getText().toString().trim().isEmpty()
                        || mDateTv.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please add all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    long primaryKey = dbHelper.insertSubTask(taskId,mSubTaskNameTv.getText().toString().trim(), descriptionEt.getText().toString().trim(),
                            mDateTv.getText().toString().trim(),"false");
                    Log.e("Key",primaryKey+"");
                    if(primaryKey == -1) {
                        Toast.makeText(getActivity(), "Data is not inserted in database", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                        AllTaskFrg homeFragment=new AllTaskFrg();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).addToBackStack(null).commit();
                    }
                }




            }
        });
    }

}
