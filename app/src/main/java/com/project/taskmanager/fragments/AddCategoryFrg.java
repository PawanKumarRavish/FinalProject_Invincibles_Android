package com.project.taskmanager.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.taskmanager.R;
import com.project.taskmanager.Utils;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.models.ModeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 07/03/2019.
 */

public class AddCategoryFrg extends BaseFragment {

    @BindView(R.id.mCategoryNameEt)
    EditText mCategoryNameEt;


    ArrayList<ModeModel> modeList;

    @BindView(R.id.description_et)
    EditText mDescriptionEt;

    @BindView(R.id.saveBtn)
    Button msSaveBtn;


    Unbinder unbinder;

    DbHelper dbHelper;


    private static AddCategoryFrg mInstance;

    public static AddCategoryFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new AddCategoryFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new AddCategoryFrg();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_expense_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper=new DbHelper(getActivity());


        msSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());

                if(mCategoryNameEt.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Category Name should not be empty", Toast.LENGTH_LONG).show();
                } else if (mDescriptionEt.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Description should not be empty", Toast.LENGTH_LONG).show();
                }else{
                    saveCategory(mCategoryNameEt.getText().toString().trim(),mDescriptionEt.getText().toString().trim());
                }


            }
        });

    }

    private void saveCategory(String categoryName, String categoryDescripton) {
        long primaryKey = dbHelper.insertCategory(categoryName, categoryDescripton);
        Log.e("Key",primaryKey+"");
        if(primaryKey == -1) {
            Toast.makeText(getActivity(), "Data is not inserted in database", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Data Successfully inserted", Toast.LENGTH_SHORT).show();
            homeInteractiveListener.toHome(HomeFragment.getInstance());
        }

    }



    private void prepareModeData() {
        ModeModel modeModel = new ModeModel("Cash");
        modeList.add(modeModel);

        modeModel = new ModeModel("Bank");
        modeList.add(modeModel);
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
        homeInteractiveListener.setToolBarTitle(getString(R.string.add_category));
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.GONE);
    }


}
