package com.project.taskmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.SecurityQuestions;
import com.project.taskmanager.models.SecurityModel;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyActivity extends WalletActivity {

    BottomSheetDialog dialog1;
    @BindView(R.id.answer_et)
    EditText mAnswerEt;
    private int[] mMaterialColors;
    private static final Random RANDOM = new Random();
    SecurityAdapter securityAdapter;

    @BindView(R.id.currency_tv)
    TextView mCurrencyTv;

    @BindView(R.id.dropdownImg)
    ImageView mDropdownImg;

    @BindView(R.id.save_btn)
    Button mSaveBtn;

    @BindView(R.id.name_et)
    EditText mNameEt;

    @BindView(R.id.mBackArrow)
    ImageView mBackArrow;

    @BindView(R.id.currency_ll)
    RelativeLayout mCurrencyLl;

    @BindView(R.id.securityQues_tv)
    TextView mSecurityQuesTv;

    @BindView(R.id.dropdownImg1)
    ImageView mDropdownImg1;

    @BindView(R.id.securityLl)
    RelativeLayout mSecurityLl;

    ArrayList<SecurityModel> securityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);

        securityList = new ArrayList<>();

        mMaterialColors = this.getResources().getIntArray(R.array.colors);

        prepareSecurityQuestions();

        setSecurityAdapter();

        mSecurityQuesTv.setText(securityList.get(0).getSecurityQuestions());


        mDropdownImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyDialog();
            }
        });

        mCurrencyLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyDialog();
            }
        });

        mDropdownImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(CurrencyActivity.this);
                dialog1.show();
                //setAccountCategoryAdapter();
            }
        });

        mSecurityLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(CurrencyActivity.this);
                dialog1.show();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNameEt.getText().toString().length() == 0) {
                    Toast.makeText(CurrencyActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (mCurrencyTv.getText().toString().length() == 0) {
                    Toast.makeText(CurrencyActivity.this, "Please enter currency", Toast.LENGTH_SHORT).show();
                }
                else if (mAnswerEt.getText().toString().isEmpty()) {
                    Toast.makeText(CurrencyActivity.this, "Please submit answer", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveSecurityQuestion();
                    SharedPreference.setQuestionsAvailable();
                    SharedPreference.setLogin();
                    SharedPreference.setFirstTime();
                    SharedPreference.setName(mNameEt.getText().toString().trim());

                    Intent intent = new Intent(CurrencyActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void saveSecurityQuestion() {

        class SaveSecurityQuestion extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                SecurityQuestions securityQuestions = new SecurityQuestions(mSecurityQuesTv.getText().toString(),mAnswerEt.getText().toString().trim());

                //adding to database
                DatabaseClient.getInstance(CurrencyActivity.this).getAppDatabase()
                        .securityQuestionDao()
                        .insert(securityQuestions);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(CurrencyActivity.this, "Your data has been saved successfully", Toast.LENGTH_LONG).show();
            }
        }

        SaveSecurityQuestion saveSecurityQuestion = new SaveSecurityQuestion();
        saveSecurityQuestion.execute();

    }

    private void prepareSecurityQuestions() {
        SecurityModel securityModel = new SecurityModel("What is your mother's name?");
        securityList.add(securityModel);

        securityModel = new SecurityModel("What is the name of your first pet?");
        securityList.add(securityModel);

        securityModel = new SecurityModel("What was your first car?");
        securityList.add(securityModel);

        securityModel = new SecurityModel("What is the name of your first school?");
        securityList.add(securityModel);

        securityModel = new SecurityModel("What is the name of the town where you were born?");
        securityList.add(securityModel);

        Log.e("List", securityList + "");
    }


    private void setSecurityAdapter() {
        View view = getLayoutInflater().inflate(R.layout.demo3_layout, null);

        RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.recyclerVieww);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(CurrencyActivity.this));

        TextView mNoDataFound = (TextView) view.findViewById(R.id.noDataFound_tvv);

        securityAdapter = new SecurityAdapter(CurrencyActivity.this, securityList);
        recyclerVieww.setAdapter(securityAdapter);

        dialog1 = new BottomSheetDialog(CurrencyActivity.this);
        dialog1.setContentView(view);


    }

    private void showCurrencyDialog() {
        CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int i) {

                if (name.equalsIgnoreCase("India Rupee")) {
                    mCurrencyTv.setText(name + " " + "(" + "₹" + ")");
                    SharedPreference.setCurrency("₹");
                    picker.dismiss();
                } else {
                    mCurrencyTv.setText(name + " " + "(" + symbol + ")");
                    SharedPreference.setCurrency(symbol);
                    picker.dismiss();
                }

            }
        });
        picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    //--------------------------Account Category Adapter--------------------------------------------//
    public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.ViewHolder> {

        int selectedItemposition;
        Context activity;
        private ArrayList<SecurityModel> moviesList;
        private int lastCheckedPosition = -1;

        public SecurityAdapter(Context activity, ArrayList<SecurityModel> moviesList) {
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

            SecurityModel securityModel = moviesList.get(position);

            holder.spinerItem.setText(securityModel.getSecurityQuestions());

            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(securityModel.getSecurityQuestions().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

            if (lastCheckedPosition == position) {
                holder.mCardView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                holder.spinerItem.setTextColor(Color.parseColor("#010101"));

            } else {
                holder.mCardView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.spinerItem.setTextColor(Color.parseColor("#010101"));
            }

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = position;
                    mSecurityQuesTv.setText(securityModel.getSecurityQuestions());
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
                mCardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }
    }
}
