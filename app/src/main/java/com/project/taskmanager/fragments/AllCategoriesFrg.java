package com.project.taskmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.taskmanager.R;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.db.entities.Ledger;
import com.project.taskmanager.models.AddCategoryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.project.taskmanager.interfaces.HomeInteractiveListener.LEDGER_TAB;

/**
 * Created by admin on 11/03/2019.
 */

public class AllCategoriesFrg extends BaseFragment {

    List<AddCategoryModel> allCategoriesList;
    LedgerAdapter ledgerAdapter;

    RecyclerView recyclerView;

    @BindView(R.id.searchEt)
    TextInputEditText mSearchEt;

    @BindView(R.id.noData_img)
    ImageView mNoDataImg;

    private int[] mMaterialColors;

    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    @BindView(R.id.noData_ll)
    LinearLayout mNoDataLl;

    Unbinder unbinder;

    DbHelper dbHelper;

    BottomSheetDialog editDialog;

    private static final Random RANDOM = new Random();

    private static AllCategoriesFrg mInstance;

    public static AllCategoriesFrg getInstance() {
        mInstance = null;
        mInstance = new AllCategoriesFrg();
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ledger_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper=new DbHelper(getActivity());


        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);
        mSearchEt.setText("");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        getAllCategories();

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
                            ledgerAdapter.filterList(allCategoriesList);
                        } else {
                            ((LedgerAdapter)recyclerView.getAdapter()).filter(s.toString());

                        }

                    }
                });
            }
        }, 100);



    }

    private void getAllCategories() {
        allCategoriesList = dbHelper.getAllCategories();
        if(allCategoriesList.size()==0){
            recyclerView.setVisibility(View.GONE);
            mNoDataLl.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            mNoDataLl.setVisibility(View.GONE);
            ledgerAdapter=new LedgerAdapter(getActivity(),allCategoriesList);
            recyclerView.setAdapter(ledgerAdapter);
            ledgerAdapter.notifyDataSetChanged();
        }


    }


    private void editCategory(AddCategoryModel addCategoryModel) {

        View view = getLayoutInflater().inflate(R.layout.edit_category_layout, null);

        EditText mCategoryNameEt = (EditText) view.findViewById(R.id.mCategoryNameEt);
        EditText mDescriptionEt = (EditText) view.findViewById(R.id.description_et);

        mCategoryNameEt.setText(addCategoryModel.getCategoryName());
        mDescriptionEt.setText(addCategoryModel.getCategoryDescription());


        Button mSaveBtn=(Button)view.findViewById(R.id.saveBtn);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCategoryNameEt.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Category Name should not be empty", Toast.LENGTH_LONG).show();
                } else if (mDescriptionEt.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Description should not be empty", Toast.LENGTH_LONG).show();
                }else{
                    dbHelper.updateCategory(addCategoryModel,mCategoryNameEt.getText().toString().trim(),mDescriptionEt.getText().toString().trim());
                    editDialog.dismiss();
                    getAllCategories();
                }
            }
        });


        editDialog = new BottomSheetDialog(getActivity());
        editDialog.setContentView(view);
        editDialog.show();


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
        homeInteractiveListener.selectTab(LEDGER_TAB);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setToolBarTitle(getString(R.string.all_categories));
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.VISIBLE);
    }

    public static String getTagName() {
        return Ledger.class.getSimpleName();
    }


    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.MyViewHolder> {

        Context context;
        List<AddCategoryModel> childFeedList;


        public LedgerAdapter(Context context, List<AddCategoryModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
             AddCategoryModel ledger = childFeedList.get(position);
            holder.mAccountNameTv.setText(ledger.getCategoryName());
            holder.mDescTv.setText(ledger.getCategoryDescription());


            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(ledger.getCategoryName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);


            holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
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
            });


        }


        public void filter(String text) {
            List<AddCategoryModel> filteredList = new ArrayList<>();
            for (AddCategoryModel item : childFeedList) {
                Log.e("Item", item.getCategoryName());
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

        public void filterList(List<AddCategoryModel> filteredList) {
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
