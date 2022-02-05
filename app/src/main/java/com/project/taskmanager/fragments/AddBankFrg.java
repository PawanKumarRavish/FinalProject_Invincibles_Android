package com.project.taskmanager.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.models.ModeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Gaganjot Singh on 25/04/2019.
 */

public class AddBankFrg extends BaseFragment {
    String fragmentType;

    ArrayList<ModeModel> modeList;

    @BindView(R.id.bankName_et)
    EditText mBankNameEt;

    @BindView(R.id.saveBtn)
    Button mSaveBtn;

    Unbinder unbinder;

    private static AddBankFrg mInstance;
    public static AddBankFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new AddBankFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new AddBankFrg();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_bank_layoutt, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle=getArguments();
        if(bundle!=null){
            fragmentType=bundle.getString("Fragment");
        }

        modeList=new ArrayList<>();

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBankNameEt.getText().toString().length()==0){
                    Toast.makeText(getActivity(), "Enter bank name", Toast.LENGTH_SHORT).show();
                }
                else{
                    modeList = SharedPreference.getBankAndModeList();
                    ModeModel modeModel = new ModeModel("Bank" + " " + mBankNameEt.getText().toString().trim());
                    modeList.add(modeModel);
                    SharedPreference.setBankAndModeList(modeList);
                    Toast.makeText(getActivity(), "Bank Saved Successfully", Toast.LENGTH_SHORT).show();

                    if(fragmentType.equalsIgnoreCase("Income")){
                        homeInteractiveListener.toAddIncome(AddTaskFrg.getInstance());
                    }
                    else{
                        homeInteractiveListener.toAddCategory(AddCategoryFrg.getInstance());
                    }
                    //homeInteractiveListener.toHome(HomeFragment.getInstance());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.setToolBarTitle("Add Bank");
    }
}
