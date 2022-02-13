package com.project.taskmanager.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.project.taskmanager.R;
import com.project.taskmanager.Utils;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.models.AddCategoryModel;
import com.project.taskmanager.models.ModeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 05/03/2019.
 */

public class AddTaskFrg extends BaseFragment {

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog dialog1;

    @BindView(R.id.mTaskNameTv)
    EditText mTaskNameTv;

    @BindView(R.id.mCategoryTv)
    TextView mCategoryTv;

    @BindView(R.id.mCategoryLayout)
    RelativeLayout mCategoryLayout;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheet;

    private Calendar selectedCal;

    ArrayList<ModeModel> modeList;
    Dialog alertDialog;
    Dialog alertDialog2;
    CategoriesAdapter categoriesAdapter;


    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    private int mYear, mMonth, mDay;
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.saveBtn)
    Button mSaveBtn;


    Unbinder unbinder;

    @BindView(R.id.date_tv)
    TextView mDueDateTv;

    @BindView(R.id.dateLayout)
    RelativeLayout mDueDateRl;


    @BindView(R.id.description_et)
    EditText mDescriptionEt;

    int selectedCategoryId=0;

    DbHelper dbHelper;
    List<AddCategoryModel> allCategories;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_task_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DbHelper(getActivity());

        allCategories = dbHelper.getAllCategories();

        modeList = new ArrayList<>();

        alertDialog = new Dialog(getActivity());
        alertDialog2 = new Dialog(getActivity());

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        setCategoriesAdapter(allCategories);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);


        mDueDateRl.setOnClickListener(new View.OnClickListener() {
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
                                mDueDateTv.setText(fDate + "-" + paddedMonth + "-" + year);

                            } else {

                                fmonth = "0" + monthOfYear;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                mDueDateTv.setText(dayOfMonth + "-" + paddedMonth + "-" + year);
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
                if(mTaskNameTv.getText().toString().trim().isEmpty()
                ||mDescriptionEt.getText().toString().trim().isEmpty()
                || mDueDateTv.getText().toString().trim().isEmpty()
                ||mCategoryTv.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please add all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    long primaryKey = dbHelper.insertTask(mTaskNameTv.getText().toString().trim(), mDescriptionEt.getText().toString().trim(),
                            mDueDateTv.getText().toString().trim(),mCategoryTv.getText().toString(),selectedCategoryId);
                    Log.e("Key",primaryKey+"");
                    if(primaryKey == -1) {
                        Toast.makeText(getActivity(), "Data is not inserted in database", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                        HomeFragment homeFragment=new HomeFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
                    }
                }




            }
        });

        mCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();
            }
        });


    }


    private void setCategoriesAdapter(List<AddCategoryModel> allCategories) {

        View view = getLayoutInflater().inflate(R.layout.dialog_catgories, null);

        RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView mNoDataFound = (TextView) view.findViewById(R.id.noDataFound_tvv);

        if (allCategories.size() == 0) {
            mNoDataFound.setVisibility(View.VISIBLE);
            recyclerVieww.setVisibility(View.GONE);
        } else {
            mNoDataFound.setVisibility(View.GONE);
            recyclerVieww.setVisibility(View.VISIBLE);
            categoriesAdapter = new CategoriesAdapter(getActivity(), allCategories);
            recyclerVieww.setAdapter(categoriesAdapter);
        }

        dialog1 = new BottomSheetDialog(getActivity());
        dialog1.setContentView(view);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setToolBarTitle(getString(R.string.add_task));
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.GONE);
    }



    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

        Context context;
        List<AddCategoryModel> childFeedList;


        public CategoriesAdapter(Context context, List<AddCategoryModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public CategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories_design, parent, false);
            return new CategoriesAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CategoriesAdapter.MyViewHolder holder, final int position) {
            AddCategoryModel ledger = childFeedList.get(position);
            holder.mCategoryNameTv.setText(ledger.getCategoryName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCategoryTv.setText(ledger.getCategoryName());
                    selectedCategoryId=ledger.getId();
                    dialog1.dismiss();
                }
            });


        }


        @Override
        public int getItemCount() {
            return childFeedList.size();
        }




        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mCategoryNameTv;
            public MyViewHolder(View itemView) {
                super(itemView);

                mCategoryNameTv = (TextView) itemView.findViewById(R.id.mCategoryNameTv);

            }
        }
    }


}
