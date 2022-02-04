package com.project.taskmanager.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.db.AppDatabase;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.Ledger;
import com.project.taskmanager.interfaces.iTransactionStatus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hp I3 on 11-03-2019.
 */

public class LedgerDetailsFrg extends BaseFragment {

    RecyclerView recyclerView;
    String accountName;

    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;
    Unbinder unbinder;

    private static LedgerDetailsFrg mInstance;
    public static LedgerDetailsFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new LedgerDetailsFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new LedgerDetailsFrg();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ledger_details_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            accountName = bundle.getString("ACCOUNT_NAME");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getLedgerListByAccountName();
    }

    private void getLedgerListByAccountName() {
        class GetLedgerListByAccountName extends AsyncTask<Void, Void, List<Ledger>> {

            @Override
            protected List<Ledger> doInBackground(Void... voids) {
                List<Ledger> ledgers = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .ledgerDao()
                        .findByAccountName(accountName);
                return ledgers;
            }

            @Override
            protected void onPostExecute(List<Ledger> ledgers) {
                super.onPostExecute(ledgers);

                if (ledgers.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    mNoDataFoundTv.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mNoDataFoundTv.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), "Got List by Name", Toast.LENGTH_SHORT).show();

                    LedgerByAccountNameAdapter ledgerByAccountNameAdapter=new LedgerByAccountNameAdapter(getActivity(),ledgers);
                    recyclerView.setAdapter(ledgerByAccountNameAdapter);

                }
            }
        }

        GetLedgerListByAccountName getLedgerListByAccountName = new GetLedgerListByAccountName();
        getLedgerListByAccountName.execute();
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
        homeInteractiveListener.setToolBarTitle(accountName);
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.VISIBLE);
    }





    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class LedgerByAccountNameAdapter extends RecyclerView.Adapter<LedgerByAccountNameAdapter.MyViewHolder> {

        Context context;
        List<Ledger> childFeedList;


        public LedgerByAccountNameAdapter(Context context, List<Ledger> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public LedgerByAccountNameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_by_account_design, parent, false);
            return new LedgerByAccountNameAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final LedgerByAccountNameAdapter.MyViewHolder holder, final int position) {
            final Ledger ledger = childFeedList.get(position);
            holder.mAccountNameTv.setText(ledger.getFrom());
            holder.mDescriptionTv.setText(ledger.getDescription());
            holder.mDateTv.setText(Utils.getDayInString(ledger.getDate()));
            String month=Utils.getMonthInString(ledger.getDate());
            holder.mMonthTv.setText(month.substring(0,3));

            holder.mModeTv.setText(ledger.getActualModeName());


            if(ledger.getAmount()<0){  // Negative
               /* holder.mAmountTv.setTextColor(Color.RED);*/
                holder.mAmountTv.setTextColor(Color.parseColor("#689F38"));
                holder.mAmountTv.setText(SharedPreference.getCurrency()+" "+Math.abs(ledger.getAmount()));
            }
            else if(ledger.getAmount()==0){ // zero
                holder.mAmountTv.setTextColor(Color.BLACK);
                holder.mAmountTv.setText(SharedPreference.getCurrency()+" "+ledger.getAmount());
            }
            else{  // positive
                holder.mAmountTv.setTextColor(Color.RED);
                /*holder.mAmountTv.setTextColor(Color.parseColor("#689F38"));*/
                holder.mAmountTv.setText(SharedPreference.getCurrency()+" "+ledger.getAmount());
            }

            /*holder.mEditTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Edit Clicked", Toast.LENGTH_SHORT).show();
                }
            });*/

            /*holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteSelectedLedger(ledger,new iTransactionStatus(){
                        @Override
                        public void onSuccess() {
                            childFeedList.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                }
            });*/


          /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteSelectedLedger(ledger, new iTransactionStatus() {
                        @Override
                        public void onSuccess() {
                            childFeedList.remove(position);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            });*/

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
            TextView mDeleteTv;
            TextView mEditTv;
            TextView mModeTv;


            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mDescriptionTv = (TextView) itemView.findViewById(R.id.description_tv);
                mAmountTv = (TextView) itemView.findViewById(R.id.amount_tv);
                mDateTv = (TextView) itemView.findViewById(R.id.date_tv);
                mMonthTv = (TextView) itemView.findViewById(R.id.month_tv);
                mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
                mEditTv = (TextView) itemView.findViewById(R.id.edit_tv);
                mModeTv = (TextView) itemView.findViewById(R.id.mode_tv);

            }
        }
    }

    private void deleteSelectedLedger(final Ledger ledger, final iTransactionStatus callback) {
        class DeleteSelectedRow extends AsyncTask<Void,Void,Void>{

            boolean isSuccess=false;

            @Override
            protected Void doInBackground(Void... voids) {

                final AppDatabase appDatabase=DatabaseClient.getInstance(getActivity()).getAppDatabase();
                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        int affectedRow = appDatabase.ledgerDao().deleteById(ledger.getId());
                         Log.e("Deleted Rows",affectedRow+"");

                        int balance = appDatabase.totalBalanceDao().updateDeletedBalance(ledger.getAmount(), ledger.getMode());

                        Log.e("Baalnce",balance+"");

                    }
                });


                return  null;
            }



            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(isSuccess) {
                    callback.onSuccess();
                }else {
                    callback.onFailure();
                }
            }
        }

        DeleteSelectedRow dr=new DeleteSelectedRow();
        dr.execute();


    }
}
