package com.project.taskmanager.fragments;

import static com.project.taskmanager.interfaces.HomeInteractiveListener.HOME_TAB;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.activities.ExpenseHistoryActivity;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.AddIncome;
import com.project.taskmanager.db.entities.TotalBalance;
import com.project.taskmanager.interfaces.Tags;
import com.project.taskmanager.models.AddCategoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 04/03/2019.
 */

public class HomeFragment extends BaseFragment {

    RecyclerView recyclerView;

    @BindView(R.id.income_ll)
    LinearLayout mIncome;


    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    @BindView(R.id.totalBalance_tv)
    TextView mTotalBalanceTv;

    @BindView(R.id.bankBalance_tv)
    TextView mBankBalanceTv;

    @BindView(R.id.cashBalance_tv)
    TextView mCashBalanceTv;

    @BindView(R.id.expenseBalance_tv)
    TextView mExpenseBalanceTv;

    @BindView(R.id.expense_img)
    LinearLayout mExpenseImg;

    @BindView(R.id.noData_ll)
    LinearLayout mNoDataLl;

    Unbinder unbinder;

    private static HomeFragment mInstance;

    @BindView(R.id.noData_img)
    ImageView mNoDataImg;

    @BindView(R.id.addCategory_ll)
    LinearLayout mAddCategoryLl;

    DbHelper dbHelper;

    public static HomeFragment getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new HomeFragment();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new HomeFragment();
        }

        return mInstance;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper=new DbHelper(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeInteractiveListener.toAddIncome(AddIncomeFrg.getInstance());
            }
        });


        mAddCategoryLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeInteractiveListener.toAddCategory(AddCategoryFrg.getInstance());
            }
        });


        getRecentTxn();

        getTotalCategories();

        getTotalExpenseBalance();


        mExpenseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExpenseHistoryActivity.class);
                startActivity(intent);
                //homeInteractiveListener.toExpenseHistory(ExpenseHistoryFrg.getInstance());
            }
        });


    }

    private void getTotalExpenseBalance() {

        class GetTotalExpenseBalance extends AsyncTask<Void, Void, Void> {
            AddIncome addIncome;
            int totalExpensebalance;


            @Override
            protected Void doInBackground(Void... voids) {
                totalExpensebalance = DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .addIncomeDao()
                        .getTotalExpenseBalance(Tags.EXPENSE);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mExpenseBalanceTv.setText(SharedPreference.getCurrency() + " " + Utils.getAmountInDecimal(totalExpensebalance + ""));

            }
        }

        GetTotalExpenseBalance gb = new GetTotalExpenseBalance();
        gb.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.selectTab(HOME_TAB);
        homeInteractiveListener.setToolBarTitle(getString(R.string.app_heading));
        homeInteractiveListener.toggleBackArrowVisiblity(View.GONE);
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.VISIBLE);
    }

    public static String getTagName() {
        return HomeFragment.class.getSimpleName();
    }

    private void getBankAndCashBalance() {

        class GetBankAndCashBalance extends AsyncTask<Void, Void, List<TotalBalance>> {

            @Override
            protected List<TotalBalance> doInBackground(Void... voids) {
                List<TotalBalance> totalBalances = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .totalBalanceDao()
                        .getAll();
                return totalBalances;
            }

            @Override
            protected void onPostExecute(List<TotalBalance> totalBalances) {
                super.onPostExecute(totalBalances);

                String cashAmount = Utils.getAmountInDecimal(totalBalances.get(0).getAmount() + "");
                String bankAmount = Utils.getAmountInDecimal(totalBalances.get(1).getAmount() + "");


                mCashBalanceTv.setText(SharedPreference.getCurrency() + " " + cashAmount);
                mBankBalanceTv.setText(SharedPreference.getCurrency() + " " + bankAmount);
            }
        }

        GetBankAndCashBalance gb = new GetBankAndCashBalance();
        gb.execute();
    }

    private void getTotalCategories() {
        List<AddCategoryModel> allCategories = dbHelper.getAllCategories();
        mBankBalanceTv.setText(allCategories.size()+"");

    }

    private void getRecentTxn() {

        class GetRecentTxn extends AsyncTask<Void, Void, List<AddIncome>> {

            @Override
            protected List<AddIncome> doInBackground(Void... voids) {
                List<AddIncome> incomeList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .addIncomeDao()
                        .getRecentTxn();
                return incomeList;
            }

            @Override
            protected void onPostExecute(List<AddIncome> addIncomes) {
                super.onPostExecute(addIncomes);
                if (addIncomes.size() == 0) {
                    mNoDataLl.setVisibility(View.VISIBLE);
                    /* mNoDataFoundTv.setVisibility(View.VISIBLE);*/
                    recyclerView.setVisibility(View.GONE);
                } else {
                    mNoDataLl.setVisibility(View.GONE);
                    /*  mNoDataFoundTv.setVisibility(View.GONE);*/
                    recyclerView.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), " Got recent Txn successfully", Toast.LENGTH_LONG).show();
                    RecentTxnAdapter recentTxnAdapter = new RecentTxnAdapter(getActivity(), addIncomes);
                    recyclerView.setAdapter(recentTxnAdapter);
                }

            }
        }

        GetRecentTxn gt = new GetRecentTxn();
        gt.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //---------------------------RecentTxnAdapter------------------------------------//
    public class RecentTxnAdapter extends RecyclerView.Adapter<RecentTxnAdapter.MyViewHolder> {

        Context context;
        List<AddIncome> childFeedList;
        private int lastCheckedPosition = -1;


        public RecentTxnAdapter(Context context, List<AddIncome> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_txn_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final AddIncome addIncome = childFeedList.get(position);

            holder.mAccountNameTv.setText(addIncome.getFromm());
            holder.mDescriptionTv.setText(addIncome.getDescription());

            String amountToShow = Utils.getAmountInDecimal(addIncome.getAmount());
            //holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + Integer.parseInt(addIncome.getAmount()));

            if (addIncome.getType() == Tags.EXPENSE) {
                holder.mAmountTv.setTextColor(Color.RED);
                holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + Integer.parseInt(addIncome.getAmount()));
            } else if (addIncome.getType() == Tags.INCOME) {
                holder.mAmountTv.setTextColor(Color.parseColor("#689F38"));
                holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + Integer.parseInt(addIncome.getAmount()));
            }

            holder.mDateTv.setText(Utils.getDayInString(addIncome.getDate()));
            String month = Utils.getMonthInString(addIncome.getDate());
            holder.mMonthTv.setText(month.substring(0, 3));
            holder.mModeTv.setText(addIncome.getActualModeName());

        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mAccountNameTv;
            TextView mDescriptionTv;
            TextView mAmountTv;
            TextView mDateTv;
            TextView mMonthTv;
            TextView mModeTv;


            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mDescriptionTv = (TextView) itemView.findViewById(R.id.description_tv);
                mAmountTv = (TextView) itemView.findViewById(R.id.amount_tv);
                mDateTv = (TextView) itemView.findViewById(R.id.date_tv);
                mMonthTv = (TextView) itemView.findViewById(R.id.month_tv);
                mModeTv = (TextView) itemView.findViewById(R.id.mode_tv);

            }
        }
    }


}
