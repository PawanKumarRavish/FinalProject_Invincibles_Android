package com.project.taskmanager.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.taskmanager.R;
import com.project.taskmanager.Utils;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.AddAccount;
import com.project.taskmanager.models.AccountCategoryModel;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by admin on 05/03/2019.
 */

public class AddAccountFrg extends BaseFragment {
    String fragmentType;

    Dialog alertDialog;
    BottomSheetDialog dialog1;
    private int[] mMaterialColors;
    private static final Random RANDOM = new Random();

    ArrayList<AccountCategoryModel> accountCategoryList;
    AccountCategoryAdapter accountCategoryAdapter;

    @BindView(R.id.save_btn)
    Button mSaveBtn;

    Unbinder unbinder;

    @BindView(R.id.accountName_et)
    EditText mAccountNameEt;

    @BindView(R.id.accountType_tv)
    TextView mAccountCategoryTv;

    @BindView(R.id.dropdownImg)
    ImageView mDropdownImg;

    @BindView(R.id.categoryLl)
    RelativeLayout mCategoryLl;

    private static AddAccountFrg mInstance;
    public static AddAccountFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new AddAccountFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new AddAccountFrg();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_account_layout, null);
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

        accountCategoryList = new ArrayList<>();

        alertDialog = new Dialog(getActivity());

        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);

        prepareAccountCategoryData();

        setAccountCategoryAdapter();

        mAccountCategoryTv.setText(accountCategoryList.get(0).getAccountName());

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAccountNameEt.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please add account name", Toast.LENGTH_LONG).show();
                } else if (mAccountCategoryTv.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please select account category", Toast.LENGTH_LONG).show();
                } else {
                    Utils.hideKeyboard(getActivity());
                    saveData();
                }
            }
        });

        mDropdownImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog1.show();
                //setAccountCategoryAdapter();
            }
        });

        mCategoryLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog1.show();
            }
        });

    }

    private void saveData() {

        class SaveAccount extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {


                /*java.util.Date currentDateTime=new java.util.Date();
                System.out.println(currentDateTime);*/


                AddAccount addAccount = new AddAccount(mAccountNameEt.getText().toString().trim(), mAccountCategoryTv.getText().toString().trim(), Utils.getCurrentTimeStamp());

                //adding to database
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .addAccountDao()
                        .insert(addAccount);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity(), "Your account has been saved successfully", Toast.LENGTH_LONG).show();
                mAccountNameEt.setText("");

                if(fragmentType.equalsIgnoreCase("Income")){
                    homeInteractiveListener.toAddIncome(AddTaskFrg.getInstance());
                }
                else{
                    homeInteractiveListener.toAddCategory(AddCategoryFrg.getInstance());
                }
            }

        }

        SaveAccount saveAccount = new SaveAccount();
        saveAccount.execute();

    }

    private void setAccountCategoryAdapter() {
        View view = getLayoutInflater().inflate(R.layout.demo3_layout, null);

        RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.recyclerVieww);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView mNoDataFound = (TextView) view.findViewById(R.id.noDataFound_tvv);

        accountCategoryAdapter = new AccountCategoryAdapter(getActivity(), accountCategoryList);
        recyclerVieww.setAdapter(accountCategoryAdapter);

        dialog1 = new BottomSheetDialog(getActivity());
        dialog1.setContentView(view);

    }

    private void prepareAccountCategoryData() {
        AccountCategoryModel accountCategoryModel = new AccountCategoryModel("Income");
        accountCategoryList.add(accountCategoryModel);

        accountCategoryModel = new AccountCategoryModel("Expense");
        accountCategoryList.add(accountCategoryModel);

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
        homeInteractiveListener.setToolBarTitle("Add Account");
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
    }

    //--------------------------Account Category Adapter--------------------------------------------//
    public class AccountCategoryAdapter extends RecyclerView.Adapter<AccountCategoryAdapter.ViewHolder> {

        int selectedItemposition;
        Context activity;
        private ArrayList<AccountCategoryModel> moviesList;
        private int lastCheckedPosition = -1;

        public AccountCategoryAdapter(Context activity, ArrayList<AccountCategoryModel> moviesList) {
            this.moviesList = moviesList;
            this.activity = activity;


        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_design, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final AccountCategoryModel accountCategoryModel = moviesList.get(position);

            holder.spinerItem.setText(accountCategoryModel.getAccountName());

            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(accountCategoryModel.getAccountName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

            if(lastCheckedPosition==position){
                holder.mCardView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                holder.spinerItem.setTextColor(Color.parseColor("#010101"));

            }
            else
            {
                holder.mCardView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.spinerItem.setTextColor(Color.parseColor("#010101"));
            }

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition=position;
                    mAccountCategoryTv.setText(accountCategoryModel.getAccountName());
                    notifyDataSetChanged();
                    dialog1.dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView spinerItem;
            MaterialLetterIcon mIcon;
            CardView mCardView;

            public ViewHolder(View convertView) {
                super(convertView);
                spinerItem = (TextView) convertView.findViewById(R.id.accountName_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mCardView=(CardView)itemView.findViewById(R.id.cardView);
            }
        }
    }
}
