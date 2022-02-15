package com.project.taskmanager.fragments;

import static com.project.taskmanager.interfaces.HomeInteractiveListener.LEDGER_TAB;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.project.taskmanager.R;
import com.project.taskmanager.Utils;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.interfaces.Tags;
import com.project.taskmanager.models.AddCategoryModel;
import com.project.taskmanager.models.AddTaskModel;

import java.util.ArrayList;
import java.util.Calendar;
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
    BottomSheetDialog editDialog;
    private int mYear, mMonth, mDay;
    private Calendar selectedCal;

    List<AddCategoryModel> allCategories;
    BottomSheetDialog dialog1;
    CategoriesAdapter categoriesAdapter;
    TextView mcategoryTv;
    int selectedCategoryId=0;

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

        getAllTasks();

        allCategories = dbHelper.getAllCategories();
        setCategoriesAdapter(allCategories);


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

    private void editTask(AddTaskModel addCategoryModel) {

        View view = getLayoutInflater().inflate(R.layout.edit_task_layout, null);

        EditText mTaskNameEt = (EditText) view.findViewById(R.id.mTaskNameTv);
        EditText mDescriptionEt = (EditText) view.findViewById(R.id.description_et);
        TextView mDateTv = (TextView) view.findViewById(R.id.date_tv);
         mcategoryTv = (TextView) view.findViewById(R.id.mCategoryTv);
        RelativeLayout mCategoryRl = (RelativeLayout) view.findViewById(R.id.mCategoryLayout);
        RelativeLayout mDateLayout = (RelativeLayout) view.findViewById(R.id.dateLayout);

        mCategoryRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();
            }
        });

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

        mTaskNameEt.setText(addCategoryModel.getTaskName());
        mDescriptionEt.setText(addCategoryModel.getTaskDescription());
        mDateTv.setText(addCategoryModel.getTaskDueDate());
        mcategoryTv.setText(addCategoryModel.getCategoryName());
        selectedCategoryId=addCategoryModel.getCategoryId();


        Button mSaveBtn=(Button)view.findViewById(R.id.saveBtn);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTaskNameEt.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Task Name should not be empty", Toast.LENGTH_LONG).show();
                } else if (mDescriptionEt.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Description should not be empty", Toast.LENGTH_LONG).show();
                }else{
                    dbHelper.updateTask(addCategoryModel ,mTaskNameEt.getText().toString().trim(),mDescriptionEt.getText().toString().trim(),
                            mDateTv.getText().toString(),mcategoryTv.getText().toString(),selectedCategoryId);
                    editDialog.dismiss();
                    getAllTasks();
                }
            }
        });


        editDialog = new BottomSheetDialog(getActivity());
        editDialog.setContentView(view);
        editDialog.show();


    }
    private void getAllTasks() {
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


            holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.deleteTask(ledger);
                    getAllTasks();
                }
            });

            holder.mEditTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   editTask(ledger);
                }
            });

            holder.mAddSubTaskTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putInt(Tags.TASK_ID,ledger.getId());
                    AddSubTaskFrg addSubTaskFrg =new AddSubTaskFrg();
                    addSubTaskFrg.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, addSubTaskFrg).addToBackStack(null).commit();

                }
            });
            holder.mShowSubTaskTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putInt(Tags.TASK_ID,ledger.getId());
                    AllSubtasksFrg allSubtasksFrg =new AllSubtasksFrg();
                    allSubtasksFrg.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, allSubtasksFrg).addToBackStack(null).commit();


                }
            });

            holder.mCompleteTaskTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


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

            TextView mAccountNameTv,mDescTv,mShowSubTaskTv,mAddSubTaskTv,mCompleteTaskTv;
            TextView mDeleteTv;
            TextView mEditTv;
            MaterialLetterIcon mIcon;
            View mFrontLayout;


            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mDescTv = (TextView) itemView.findViewById(R.id.mDescTv);
                mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
                mAddSubTaskTv =  itemView.findViewById(R.id.mAddSubTaskTv);
                mCompleteTaskTv =  itemView.findViewById(R.id.mCompleteTask);
                mShowSubTaskTv =  itemView.findViewById(R.id.mShowSubTaskTv);
                mEditTv = (TextView) itemView.findViewById(R.id.edit_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mFrontLayout = (FrameLayout) itemView.findViewById(R.id.frontLayout);


            }
        }
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
        public void onBindViewHolder(CategoriesAdapter.MyViewHolder holder, final int position) {
            AddCategoryModel ledger = childFeedList.get(position);
            holder.mCategoryNameTv.setText(ledger.getCategoryName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcategoryTv.setText(ledger.getCategoryName());
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
