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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
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
import com.project.taskmanager.models.AddTaskModel;

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
                AddTaskFrg homeFragment=new AddTaskFrg();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
            }
        });


        mAddCategoryLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeInteractiveListener.toAddCategory(AddCategoryFrg.getInstance());
            }
        });


        getRecentsTasks();

        getTotalCategories();

        mExpenseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExpenseHistoryActivity.class);
                startActivity(intent);
                //homeInteractiveListener.toExpenseHistory(ExpenseHistoryFrg.getInstance());
            }
        });


    }

    private void getRecentsTasks() {
        List<AddTaskModel> allTasks = dbHelper.getAllTasks();
        mCashBalanceTv.setText(allTasks.size()+"");
        if(allTasks.size()>0){
            mNoDataLl.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            TasksAdapter tasksAdapter=new TasksAdapter(getActivity(),allTasks);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(tasksAdapter);


        }else{
            mNoDataLl.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
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

    private void getTotalCategories() {
        List<AddCategoryModel> allCategories = dbHelper.getAllCategories();
        mBankBalanceTv.setText(allCategories.size()+"");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }




    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {

        Context context;
        List<AddTaskModel> childFeedList;


        public TasksAdapter(Context context, List<AddTaskModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public TasksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_txn_design, parent, false);
            return new TasksAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TasksAdapter.MyViewHolder holder, final int position) {
            AddTaskModel childFeedModel = childFeedList.get(position);
            holder.mTaskNameTv.setText(childFeedModel.getTaskName());
            holder.mTaskDesTv.setText(childFeedModel.getTaskDescription());
            holder.mCategoryTv.setText(childFeedModel.getCategoryName());

            holder.mDateTv.setText(Utils.getDayInString(childFeedModel.getTaskDueDate()));
            String month = Utils.getMonthInString(childFeedModel.getTaskDueDate());
            holder.mMonthTv.setText(month.substring(0, 3));

        }


        @Override
        public int getItemCount() {
            return childFeedList.size();
        }




        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mTaskNameTv,mTaskDesTv,mCategoryTv,mDateTv,mMonthTv;


            public MyViewHolder(View itemView) {
                super(itemView);

                mTaskNameTv = itemView.findViewById(R.id.mTaskNameTv);
                mTaskDesTv = itemView.findViewById(R.id.mTaskDesTv);
                mCategoryTv = itemView.findViewById(R.id.mCategoryTv);
                mDateTv = itemView.findViewById(R.id.mDateTv);
                mMonthTv = itemView.findViewById(R.id.mMonthTv);


            }
        }
    }



}
