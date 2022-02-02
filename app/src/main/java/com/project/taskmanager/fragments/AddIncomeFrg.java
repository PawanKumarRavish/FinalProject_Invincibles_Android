package com.project.taskmanager.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.AddAccount;
import com.project.taskmanager.db.entities.AddIncome;
import com.project.taskmanager.db.entities.Ledger;
import com.project.taskmanager.db.entities.TotalBalance;
import com.project.taskmanager.interfaces.Tags;
import com.project.taskmanager.models.ModeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 05/03/2019.
 */

public class AddIncomeFrg extends BaseFragment {

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog dialog;
    BottomSheetDialog dialog1;
    private int[] mMaterialColors;
    private static final Random RANDOM = new Random();

    int accountId, amountToSend, amountToSendInNegative;

    @BindView(R.id.dateLayout)
    RelativeLayout mDateLayout;

    @BindView(R.id.fromLayout)
    RelativeLayout mFromLayout;

    @BindView(R.id.modeLayout)
    RelativeLayout mModeLayout;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheet;

    private Calendar selectedCal;

    ArrayList<ModeModel> modeList;
    Dialog alertDialog;
    Dialog alertDialog2;
    ModeAdapter modeAdapter;

    @BindView(R.id.fromImage)
    ImageView mFromImage;

    @BindView(R.id.modeImage)
    ImageView mModeImage;

    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    private int mYear, mMonth, mDay;

    BottomSheetBehavior behavior;

    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.saveBtn)
    Button mSaveBtn;

    @BindView(R.id.addAcountBtn)
    Button mAddAcountBtn;

    Unbinder unbinder;

    @BindView(R.id.amount_et)
    EditText mAmountEt;

    @BindView(R.id.date_tv)
    TextView mDateTv;

    @BindView(R.id.from_tv)
    TextView mFromTv;

    @BindView(R.id.mode_tv)
    TextView mModeTv;

    @BindView(R.id.description_et)
    EditText mDescriptionEt;

    private static AddIncomeFrg mInstance;

    public static AddIncomeFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new AddIncomeFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new AddIncomeFrg();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_income_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        modeList = new ArrayList<>();

        alertDialog = new Dialog(getActivity());
        alertDialog2 = new Dialog(getActivity());

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);

        getAllAccountsList();

        modeList = SharedPreference.getBankAndModeList();
        setModeAdapter();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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


        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAmountEt.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Amount should not be empty", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(mAmountEt.getText().toString())==0){
                    Toast.makeText(getActivity(), "Amount should not be zero", Toast.LENGTH_LONG).show();
                }
                else if (mDateTv.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Date should  be assigned", Toast.LENGTH_LONG).show();
                } else if (mFromTv.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "From should not be empty", Toast.LENGTH_LONG).show();
                } else if (mModeTv.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Mode should not be empty", Toast.LENGTH_LONG).show();
                } else if (mDescriptionEt.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Description should not be empty", Toast.LENGTH_LONG).show();
                } else {
                    Utils.hideKeyboard(getActivity());
                    saveToLedger();
                    saveToTotalBalance();
                    saveIncome();

                }

            }
        });


        mAddAcountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                homeInteractiveListener.toAddAccount(AddAccountFrg.getInstance());

            }
        });


        mFromImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog.show();
               /* behavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
            }
        });

        mFromLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog.show();


            }
        });

        mModeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog1.show();
                //setModeAdapter();
            }
        });

        mModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog1.show();
                //setModeAdapter();
            }
        });
    }

    private void saveToTotalBalance() {
        final int amountToSend = Integer.parseInt(mAmountEt.getText().toString().trim());
        final int amountToSendInNegative = -amountToSend;
        class SaveToTotalBalance extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                TotalBalance totalBalance = DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .totalBalanceDao()
                        .findByMode(mModeTv.getText().toString().trim().equalsIgnoreCase(Tags.CASH) ? Tags.CASH : Tags.BANK);

                Log.e("Previous Balance", totalBalance.getAmount() + "");

                // totalBalance.setMode(mModeTv.getText().toString().trim());
                totalBalance.setAmount(totalBalance.getAmount() + amountToSend);
                //adding to database
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .totalBalanceDao()
                        .update(totalBalance);
                Log.e("After Balnce", totalBalance.getAmount() + "");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("Total Balance<>", "Total Balance Saved Successfully");
            }
        }

        SaveToTotalBalance saveToTotalBalance = new SaveToTotalBalance();
        saveToTotalBalance.execute();
    }

    private void saveToLedger() {
        amountToSend = Integer.parseInt(mAmountEt.getText().toString().trim());
        amountToSendInNegative = -amountToSend;
        class SaveToLedger extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Ledger ledger = new Ledger(amountToSendInNegative, Utils.toDbTimeStamp(selectedCal.getTime()), mFromTv.getText().toString(), Utils.fetchMode(mModeTv.getText().toString()), mDescriptionEt.getText().toString(), Tags.INCOME, accountId, Utils.getCurrentTimeStamp(), mModeTv.getText().toString().trim());

                //adding to database
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .ledgerDao()
                        .insert(ledger);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("Ledger<>", "Ledger Saved Successfully");
            }

        }

        SaveToLedger saveToLedger= new SaveToLedger();
        saveToLedger.execute();
    }

    private void saveIncome() {

        class SaveIncome extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AddIncome addIncome = new AddIncome(mAmountEt.getText().toString(), Utils.toDbTimeStamp(selectedCal.getTime()), mFromTv.getText().toString(), Utils.fetchMode(mModeTv.getText().toString()), mDescriptionEt.getText().toString(), Tags.INCOME, accountId, Utils.getCurrentTimeStamp(), mModeTv.getText().toString().trim());

                //adding to database
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .addIncomeDao()
                        .insert(addIncome);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity(), "Your income has been saved successfully", Toast.LENGTH_LONG).show();
                homeInteractiveListener.toHome(HomeFragment.getInstance());
            }
        }

        SaveIncome saveIncome = new SaveIncome();
        saveIncome.execute();

    }

    private void setModeAdapter() {

        View view = getLayoutInflater().inflate(R.layout.demo2_layout, null);

        RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.recyclerVieww);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView mNoDataFound=(TextView)view.findViewById(R.id.noDataFound_tvv);

        Button mAddBankBtn=(Button)view.findViewById(R.id.addBankBtn);
        mAddBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("Fragment","INCOME");
                AddBankFrg addBankFrg=AddBankFrg.getInstance();
                addBankFrg.setArguments(bundle);
                dialog1.dismiss();
                homeInteractiveListener.toAddBankFrg(addBankFrg);
            }
        });



        if(modeList.size() == 0){
            mNoDataFound.setVisibility(View.VISIBLE);
            recyclerVieww.setVisibility(View.GONE);
        }
        else{
            mNoDataFound.setVisibility(View.GONE);
            recyclerVieww.setVisibility(View.VISIBLE);
            modeAdapter = new ModeAdapter(getActivity(), modeList);
            recyclerVieww.setAdapter(modeAdapter);
        }

        dialog1 = new BottomSheetDialog(getActivity());
        dialog1.setContentView(view);


    }

    private void prepareModeData() {
        ModeModel modeModel = new ModeModel("Cash");
        modeList.add(modeModel);

        modeModel = new ModeModel("Bank (new)");
        modeList.add(modeModel);
    }

    private void getAllAccountsList() {
        class GetAllAccountsList extends AsyncTask<Void, Void, List<AddAccount>> {

            @Override
            protected List<AddAccount> doInBackground(Void... voids) {
                List<AddAccount> accountList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .addAccountDao()
                        .getAll();
                return accountList;
            }

            @Override
            protected void onPostExecute(List<AddAccount> addAccounts) {
                super.onPostExecute(addAccounts);


                View view = getLayoutInflater().inflate(R.layout.demo1_layout, null);

                RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.recyclerVieww);
                recyclerVieww.setHasFixedSize(true);
                recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

                TextView mNoDataFound=(TextView)view.findViewById(R.id.noDataFound_tvv);
                ImageView mNoDataImg=(ImageView)view.findViewById(R.id.noData_img);
                LinearLayout mNoDataLl=(LinearLayout) view.findViewById(R.id.noData_ll);

                Button mAddAccountBtn=(Button)view.findViewById(R.id.addAcountBtn);
                mAddAccountBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle=new Bundle();
                        bundle.putString("Fragment","INCOME");
                        AddAccountFrg addAccountFrg=AddAccountFrg.getInstance();
                        addAccountFrg.setArguments(bundle);
                        dialog.dismiss();
                        homeInteractiveListener.toAddAccount(addAccountFrg);
                    }
                });



                if(addAccounts.size() == 0){
                    mNoDataLl.setVisibility(View.VISIBLE);
                   /* mNoDataFound.setVisibility(View.VISIBLE);*/
                    recyclerVieww.setVisibility(View.GONE);
                }
                else{
                    mNoDataLl.setVisibility(View.GONE);
                    /*mNoDataFound.setVisibility(View.GONE);*/
                    recyclerVieww.setVisibility(View.VISIBLE);
                    AccountListAdapter adapter = new AccountListAdapter(getActivity(), addAccounts);
                    recyclerVieww.setAdapter(adapter);
                }

                dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(view);


            }
        }

        GetAllAccountsList gt=new GetAllAccountsList();
        gt.execute();
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
        homeInteractiveListener.setToolBarTitle("Add Income");
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.GONE);
    }


    //---------------------------AccountListAdapter------------------------------------//
    public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyViewHolder> {

        Context context;
        List<AddAccount> childFeedList;
        private int lastCheckedPosition = -1;

        private SparseBooleanArray selectedItems = new SparseBooleanArray();


        public AccountListAdapter(Context context, List<AddAccount> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final AddAccount addAccount = childFeedList.get(position);
            holder.mAccountNameTv.setText(addAccount.getAccountName());
            holder.mRadioButton.setChecked(position == lastCheckedPosition);


            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(addAccount.getAccountName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

            if(lastCheckedPosition==position){
                holder.mCardView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                holder.mAccountNameTv.setTextColor(Color.parseColor("#010101"));

               // holder.mRadioButton.setHighlightColor(Color.parseColor("#010101"));
            }
            else
            {
                holder.mCardView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.mAccountNameTv.setTextColor(Color.parseColor("#010101"));

               // holder.mRadioButton.setHighlightColor(Color.parseColor("#010101"));
            }


            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition=position;
                    accountId = addAccount.getId();
                    mFromTv.setText(addAccount.getAccountName());
                    notifyDataSetChanged();
                    dialog.dismiss();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });

            holder.mRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = position;
                    mFromTv.setText(addAccount.getAccountName());
                    notifyDataSetChanged();
                    dialog.dismiss();
                    //  sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                }
            });


        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mAccountNameTv;
            RadioButton mRadioButton;
            MaterialLetterIcon mIcon;
            CardView mCardView;

            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mRadioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mCardView=(CardView)itemView.findViewById(R.id.cardView);


            }
        }
    }


    //---------------------------Mode Adapter---------------------------------------//
    public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.ViewHolder> {

        int selectedItemposition;
        Context activity;
        private ArrayList<ModeModel> moviesList;
        private int lastCheckedPosition = -1;

        public ModeAdapter(Context activity, ArrayList<ModeModel> moviesList) {
            this.moviesList = moviesList;
            this.activity = activity;


        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mode_design_layout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final ModeModel modeModel = moviesList.get(position);
            holder.mBankNameTv.setText(modeModel.getModeName());

            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(modeModel.getModeName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

            if(lastCheckedPosition==position){
                holder.mCardView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                holder.mBankNameTv.setTextColor(Color.parseColor("#010101"));
                //holder.mRadioButton.setHighlightColor(Color.parseColor("#010101"));
            }
            else
            {
                holder.mCardView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.mBankNameTv.setTextColor(Color.parseColor("#010101"));
                //holder.mRadioButton.setHighlightColor(Color.parseColor("#010101"));
            }

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = position;
                    mModeTv.setText(modeModel.getModeName());
                    notifyDataSetChanged();
                    dialog1.dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {
            return moviesList == null ? 0 : moviesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            MaterialLetterIcon mIcon;
            CardView mCardView;
            TextView mBankNameTv;

            public ViewHolder(View convertView) {
                super(convertView);

                mBankNameTv = (TextView) itemView.findViewById(R.id.bankName_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mCardView=(CardView)itemView.findViewById(R.id.cardView);
            }
        }
    }
}
