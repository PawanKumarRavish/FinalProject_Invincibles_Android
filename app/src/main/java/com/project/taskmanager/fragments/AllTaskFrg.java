package com.project.taskmanager.fragments;

import static com.project.taskmanager.interfaces.HomeInteractiveListener.LEDGER_TAB;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.textfield.TextInputEditText;
import com.project.taskmanager.R;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.models.AddCategoryModel;
import com.project.taskmanager.models.AddTaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllTaskFrg extends BaseFragment {

    Unbinder unbinder;
    DbHelper dbHelper;
    List<AddTaskModel> allTasks;

    @BindView(R.id.searchEt)
    TextInputEditText mSearchEt;

    @BindView(R.id.noData_ll)
    LinearLayout mNoDataLl;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private static final Random RANDOM = new Random();
    private int[] mMaterialColors;

    TasksAdapter tasksAdapter;

    private static AllTaskFrg mInstance;
    public static AllTaskFrg getInstance() {
        mInstance = null;
        mInstance = new AllTaskFrg();
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_tasks_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DbHelper(getActivity());
        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);
        mSearchEt.setText("");

        allTasks = dbHelper.getAllTasks();
        if (allTasks.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataLl.setVisibility(View.GONE);
            tasksAdapter=new TasksAdapter(getActivity(),allTasks);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(tasksAdapter);
            tasksAdapter.notifyDataSetChanged();

        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataLl.setVisibility(View.VISIBLE);

        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            tasksAdapter.filterList(allTasks);
                        } else {
                            ((TasksAdapter)mRecyclerView.getAdapter()).filter(s.toString());

                        }

                    }
                });
            }
        }, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSearchEt.setText("");
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setToolBarTitle(getString(R.string.all_tasks));
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.VISIBLE);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list_design, parent, false);
            return new TasksAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TasksAdapter.MyViewHolder holder, final int position) {
            AddTaskModel ledger = childFeedList.get(position);
            holder.mAccountNameTv.setText(ledger.getTaskName());
            holder.mDescTv.setText(ledger.getTaskDescription());


            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(ledger.getCategoryName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);


            /*holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.deleteCategory(ledger);
                    getAllCategories();
                }
            });

            holder.mEditTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCategory(ledger);
                }
            });*/


        }


        public void filter(String text) {
            List<AddTaskModel> filteredList = new ArrayList<>();
            for (AddTaskModel item : childFeedList) {
                Log.e("Item", item.getTaskName());
                if (item.getCategoryName().toLowerCase().contains(text.toLowerCase())) {
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

        public void filterList(List<AddTaskModel> filteredList) {
            this.childFeedList = filteredList;
            notifyDataSetChanged();
        }




        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mAccountNameTv,mDescTv;
            TextView mDeleteTv;
            TextView mEditTv;
            MaterialLetterIcon mIcon;
            View mFrontLayout;


            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mDescTv = (TextView) itemView.findViewById(R.id.mDescTv);
                mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
                mEditTv = (TextView) itemView.findViewById(R.id.edit_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mFrontLayout = (FrameLayout) itemView.findViewById(R.id.frontLayout);


            }
        }
    }
}
