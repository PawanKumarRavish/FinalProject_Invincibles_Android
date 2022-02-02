package com.project.taskmanager.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.db.AppDatabase;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.Ledger;
import com.project.taskmanager.db.entities.TotalBalance;
import com.project.taskmanager.interfaces.iTransactionStatus;

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

public class  LedgerFrg extends BaseFragment {

    List<Ledger> ledgers;
    ArrayList<Ledger> newList;
    LedgerAdapter ledgerAdapter;

   /* Ledger ledger,ledger1;*/  // uncomment it for cash ,bank list

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

    private static final Random RANDOM = new Random();

    private static LedgerFrg mInstance;

    public static LedgerFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new LedgerFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new LedgerFrg();
        }

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

        /*ledger=new Ledger(); // uncomment it for cash ,bank list
        ledger1=new Ledger();*/ // uncomment it for cash ,bank list

        mMaterialColors = getActivity().getResources().getIntArray(R.array.colors);
        mSearchEt.setText("");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*getTotalBalanceList();*/ // uncomment it for cash ,bank list

        getLedgerHistory();

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
                            ledgerAdapter.filterList(ledgers);
                            //recyclerView.setAdapter(ledgerAdapter);
                        } else {
                            //filter(s.toString());
                            ((LedgerAdapter)recyclerView.getAdapter()).filter(s.toString());

                        }

                    }
                });
            }
        }, 100);







    }

    /*private void filter(String text) {
        List<Ledger> filteredList = new ArrayList<>();
        for (Ledger item : ledgers) {
            Log.e("Item", item.getFrom());
            if (item.getFrom().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        ledgerAdapter.filterList(filteredList);
    }*/


    private void getTotalBalanceList() {
        class GetTotalBalanceList extends AsyncTask<Void, Void, List<TotalBalance>> {

            @Override
            protected List<TotalBalance> doInBackground(Void... voids) {
                List<TotalBalance> totalBalanceList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .totalBalanceDao()
                        .getAll();
                return totalBalanceList;
            }

            @Override
            protected void onPostExecute(List<TotalBalance> totalBalances) {
                super.onPostExecute(totalBalances);

               /* ledger.setFrom(totalBalances.get(0).getMode());   // uncomment it for cash ,bank list
                ledger.setAmount(totalBalances.get(0).getAmount());


                ledger1.setFrom(totalBalances.get(1).getMode());
                ledger1.setAmount(totalBalances.get(1).getAmount());*/


            }
        }

        GetTotalBalanceList gt = new GetTotalBalanceList();
        gt.execute();


    }

    private void getLedgerHistory() {
        class GetLedgerHistory extends AsyncTask<Void, Void, List<Ledger>> {

            @Override
            protected List<Ledger> doInBackground(Void... voids) {
                ledgers = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .ledgerDao()
                        .getLedgerHistory();
                return ledgers;
            }

            @Override
            protected void onPostExecute(List<Ledger> ledgers) {
                super.onPostExecute(ledgers);

                newList=new ArrayList<>(ledgers.size());

                if (ledgers.size() == 0) {
                    mNoDataLl.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    /*mNoDataFoundTv.setVisibility(View.VISIBLE);*/


                } else {
                    /*ledgers.add(0,ledger); // uncomment it for cash ,bank list
                    ledgers.add(1,ledger1);*/ // uncomment it for cash ,bank list

                    recyclerView.setVisibility(View.VISIBLE);
                    mNoDataLl.setVisibility(View.GONE);
                    /*mNoDataFoundTv.setVisibility(View.GONE);*/
                    //Toast.makeText(getActivity(), "Got Ledger List successfully", Toast.LENGTH_SHORT).show();

                    ledgerAdapter = new LedgerAdapter(getActivity(), ledgers);
                    Log.e("List",ledgers+"");
                    recyclerView.setAdapter(ledgerAdapter);
                }


            }
        }
        GetLedgerHistory getLedgerHistory = new GetLedgerHistory();
        getLedgerHistory.execute();
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
        homeInteractiveListener.setToolBarTitle("Ledger");
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.VISIBLE);
    }

    public static String getTagName() {
        return Ledger.class.getSimpleName();
    }


    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.MyViewHolder> {

        Context context;
        List<Ledger> childFeedList;


        public LedgerAdapter(Context context, List<Ledger> childFeedList) {
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
            final Ledger ledger = childFeedList.get(position);
            holder.mAccountNameTv.setText(ledger.getFrom());

            if (ledger.getAmount() < 0) {  // Negative
                //holder.mAmountTv.setTextColor(Color.RED);
                holder.mAmountTv.setTextColor(Color.parseColor("#689F38"));
                holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + Math.abs(ledger.getAmount()));
            } else if (ledger.getAmount() == 0) { // zero
                holder.mAmountTv.setTextColor(Color.BLACK);
                holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + ledger.getAmount());
            } else {  // positive
                /*holder.mAmountTv.setTextColor(Color.parseColor("#689F38"));*/
                holder.mAmountTv.setTextColor(Color.RED);
                holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + ledger.getAmount());
            }


            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(ledger.getFrom().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);


            holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteSelectedRow(ledger.getFrom(), new iTransactionStatus() {
                        @Override
                        public void onSuccess() {
                            childFeedList.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                    //Toast.makeText(getActivity(), "Delete Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            holder.mEditTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), "Edit Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            holder.mFrontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("ACCOUNT_NAME", ledger.getFrom());
                    LedgerDetailsFrg ledgerDetailsFrg = LedgerDetailsFrg.getInstance();
                    ledgerDetailsFrg.setArguments(bundle);
                    homeInteractiveListener.toLedgerDetails(ledgerDetailsFrg);

                }
            });

        }


        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public void filterList(List<Ledger> filteredList) {
            this.childFeedList = filteredList;
            notifyDataSetChanged();
        }

        public void filter(String text) {
            List<Ledger> filteredList = new ArrayList<>();
            for (Ledger item : childFeedList) {
                Log.e("Item", item.getFrom());
                if (item.getFrom().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            this.childFeedList=filteredList;
            notifyDataSetChanged();
            //ledgerAdapter.filterList(filteredList);
        }



        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mAccountNameTv;
            TextView mAmountTv;
            TextView mDeleteTv;
            TextView mEditTv;
            MaterialLetterIcon mIcon;
            View mFrontLayout;


            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mAmountTv = (TextView) itemView.findViewById(R.id.amount_tv);
                mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
                mEditTv = (TextView) itemView.findViewById(R.id.edit_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mFrontLayout = (FrameLayout) itemView.findViewById(R.id.frontLayout);


            }
        }
    }

    private void deleteSelectedRow(final String from, final iTransactionStatus callback) {
        class DeleteSelectedRow extends AsyncTask<Void, Void, Void> {

            boolean isSuccess = false;

            @Override
            protected Void doInBackground(Void... voids) {

                final AppDatabase appDatabase = DatabaseClient.getInstance(getActivity()).getAppDatabase();
                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        List<String> modeList = appDatabase.ledgerDao().findModeListByAccountName(from);
                        for (String mode : modeList) {
                            Integer balance = appDatabase.ledgerDao().getSumByModeAndAccountName(mode, from);
                            Log.e("Balance", balance + "");
                            int balanceUpdatedRow = appDatabase.totalBalanceDao().updateDeletedBalance(balance, mode);
                            Log.e("Affetced Rows", balanceUpdatedRow + "");
                        }
                        int deletedRow = appDatabase.ledgerDao().deleteByAccountName(from);

                        Log.e("Deleted Row", deletedRow + "");
                        isSuccess = true;

                    }
                });


                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isSuccess) {
                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }
            }
        }

        DeleteSelectedRow deleteSelectedRow = new DeleteSelectedRow();
        deleteSelectedRow.execute();
    }


}
