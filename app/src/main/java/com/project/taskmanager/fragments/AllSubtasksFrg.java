package com.project.taskmanager.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.taskmanager.R;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.interfaces.Tags;
import com.project.taskmanager.models.AddSubTaskModel;
import com.project.taskmanager.models.AddTaskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllSubtasksFrg extends BaseFragment {

    DbHelper dbHelper;
    Unbinder unbinder;
    int taskId;

    @BindView(R.id.noData_ll)
    LinearLayout mNoDataLl;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;
    SubTasksAdapter subTasksAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_sub_tasks_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);

        dbHelper = new DbHelper(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskId = bundle.getInt(Tags.TASK_ID);

        }

        getAllSubtasks();


    }

    private void getAllSubtasks() {
        List<AddSubTaskModel> subTasksOfTasks = dbHelper.getSubTasksOfTasks(taskId);
        Log.e("subTaskList", subTasksOfTasks.size() + "");
        if (subTasksOfTasks.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataLl.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataLl.setVisibility(View.GONE);
            subTasksAdapter=new SubTasksAdapter(getActivity(),subTasksOfTasks);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(subTasksAdapter);
            subTasksAdapter.notifyDataSetChanged();
        }


    }



    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class SubTasksAdapter extends RecyclerView.Adapter<SubTasksAdapter.MyViewHolder> {

        Context context;
        List<AddSubTaskModel> childFeedList;


        public SubTasksAdapter(Context context, List<AddSubTaskModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public SubTasksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_task_list_design, parent, false);
            return new SubTasksAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SubTasksAdapter.MyViewHolder holder, final int position) {
            AddSubTaskModel ledger = childFeedList.get(position);
            holder.mAccountNameTv.setText(ledger.getSubTaskName());
            holder.mDescTv.setText(ledger.getDescription());
            if(ledger.getIsSubTaskCompleted().equalsIgnoreCase("false")){
                holder.mCompleteTaskTv.setText("Mark As Completed");
                holder.mCompleteTaskTv.setTextColor(Color.parseColor("#DB4437"));
            }else{
                holder.mCompleteTaskTv.setText("Completed");
                holder.mCompleteTaskTv.setTextColor(Color.parseColor("#689F38"));
            }


            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(ledger.getSubTaskName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);


            holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.deleteSubTask(ledger);
                    getAllSubtasks();
                }
            });

            /*holder.mEditTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTask(ledger);
                }
            });*/


            holder.mCompleteTaskTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(ledger.getIsSubTaskCompleted().equalsIgnoreCase("true")){
                       Toast.makeText(getActivity(), "Sub task already completed", Toast.LENGTH_SHORT).show();
                   }else{
                       dbHelper.updateSubTask(ledger,"true");
                       getAllSubtasks();
                   }

                }
            });


        }


        public void filter(String text) {
            List<AddSubTaskModel> filteredList = new ArrayList<>();
            for (AddSubTaskModel item : childFeedList) {
                Log.e("Item", item.getSubTaskName());
                if (item.getSubTaskName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            this.childFeedList=filteredList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public void filterList(List<AddSubTaskModel> filteredList) {
            this.childFeedList = filteredList;
            notifyDataSetChanged();
        }




        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mAccountNameTv,mDescTv,mCompleteTaskTv;
            TextView mDeleteTv;
            TextView mEditTv;
            MaterialLetterIcon mIcon;
            View mFrontLayout;


            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mDescTv = (TextView) itemView.findViewById(R.id.mDescTv);
                mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
                mCompleteTaskTv =  itemView.findViewById(R.id.mCompleteTask);
                mEditTv = (TextView) itemView.findViewById(R.id.edit_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mFrontLayout = (FrameLayout) itemView.findViewById(R.id.frontLayout);


            }
        }
    }
}
