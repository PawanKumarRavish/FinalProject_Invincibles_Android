package com.project.taskmanager.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.AddIncome;
import com.project.taskmanager.interfaces.Tags;
import com.project.taskmanager.models.MonthModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseHistoryActivity extends BaseActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    Dialog alertDialog;

    ArrayList<MonthModel> monthList;
    MonthAdapter monthAdapter;

    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    @BindView(R.id.mBackArrow)
    ImageView mBackArrow;

    @BindView(R.id.calenderImg)
    ImageView mCalenderImg;

    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.noData_img)
    ImageView mNoDataImg;

    private LineChart mChart;

    private List<AddIncome> expenseList;

    private HashMap<String, AddIncome> dataMap = new HashMap<>();

    RecyclerView recyclerView;


    private static ExpenseHistoryActivity mInstance;

    public static ExpenseHistoryActivity getInstance() {

        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new ExpenseHistoryActivity();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new ExpenseHistoryActivity();
        }

        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);
        ButterKnife.bind(this);

        mChart = (LineChart) findViewById(R.id.chart);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseHistoryActivity.this));


        monthList = new ArrayList<>();

        getExpenseHistory();

        prepareMonthData();

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCalenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthAdapter();

            }
        });






        final ImageView optionsmenu = (ImageView) findViewById(R.id.optionsMenu);
        optionsmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi")
                Context wrapper = new ContextThemeWrapper(ExpenseHistoryActivity.this, R.style.MyPopupMenuStyle);
                PopupMenu popup = new PopupMenu(wrapper, optionsmenu);
                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popup);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                } catch (Exception e) {

                    Log.e(">>>>>", "error forcing menu icons to show", e);

                }
                popup.getMenuInflater().inflate(R.menu.optionsmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.privacyPolicy:
                                startActivity(new Intent(ExpenseHistoryActivity.this,PrivacyPolicyActivity.class));
                                //finish();
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                popup.show();
            }
        });
    }

    private void prepareMonthData() {
        MonthModel monthModel = new MonthModel(0, "January");
        monthList.add(monthModel);

        monthModel = new MonthModel(1, "February");
        monthList.add(monthModel);

        monthModel = new MonthModel(2, "March");
        monthList.add(monthModel);

        monthModel = new MonthModel(3, "April");
        monthList.add(monthModel);

        monthModel = new MonthModel(4, "May");
        monthList.add(monthModel);

        monthModel = new MonthModel(5, "June");
        monthList.add(monthModel);

        monthModel = new MonthModel(6, "July");
        monthList.add(monthModel);

        monthModel = new MonthModel(7, "August");
        monthList.add(monthModel);

        monthModel = new MonthModel(8, "September");
        monthList.add(monthModel);

        monthModel = new MonthModel(9, "October");
        monthList.add(monthModel);

        monthModel = new MonthModel(10, "November");
        monthList.add(monthModel);

        monthModel = new MonthModel(11, "December");
        monthList.add(monthModel);


    }

    private void setMonthAdapter() {
        alertDialog = new Dialog(ExpenseHistoryActivity.this);

        monthAdapter = new MonthAdapter(ExpenseHistoryActivity.this, monthList);

        // alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//this line MUST BE BEFORE setContentView
        alertDialog.setContentView(R.layout.listdialog);
        alertDialog.setCanceledOnTouchOutside(false);

        final RelativeLayout listHead = (RelativeLayout) alertDialog.findViewById(R.id.listHead);
        TextView confirmTitle = (TextView) alertDialog.findViewById(R.id.confirmTitle);
        final RecyclerView recycler_view = (RecyclerView) alertDialog.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ExpenseHistoryActivity.this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setAdapter(monthAdapter);

        ImageView cancel = (ImageView) alertDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void showGraph() {
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        float maxAmount = 0;
        for (AddIncome expIncome : expenseList) {
            dataMap.put(Utils.getDayInString(expIncome.getDate()), expIncome);
            if (Float.parseFloat(expIncome.getAmount()) > maxAmount) {
                maxAmount = Float.parseFloat(expIncome.getAmount());
            }
        }
        // add data
        setData();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
       /* mChart.setDescription("Demo Line Chart");*/

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaxValue(maxAmount);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);


        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        //  dont forget to refresh the drawing
        mChart.invalidate();

    }

    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            xVals.add(i + "");
        }
        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {


        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i <= 30; i++) {
            String keyName = i < 10 ? "0" + (i + 1) : "" + (i + 1);
            if (dataMap.get(keyName) != null) {
                yVals.add(new Entry(Float.parseFloat(dataMap.get(keyName).getAmount()), i));
            } else {
                yVals.add(new Entry(0f, i));
            }
        }


        return yVals;
    }


    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "Days");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }


    private void getExpenseHistory() {
        class GetExpenseHistory extends AsyncTask<Void, Void, List<AddIncome>> {


            @Override
            protected List<AddIncome> doInBackground(Void... voids) {
                List<AddIncome> expenseList = DatabaseClient
                        .getInstance(ExpenseHistoryActivity.this)
                        .getAppDatabase()
                        .addIncomeDao()
                        .getLatestTxns(Tags.EXPENSE);
                        /*.getDropDownMonthTxns(new SimpleSQLiteQuery("SELECT id,fromm,mode,description,type,account_id,created_at,date, SUM(amount) AS amount FROM AddIncome WHERE type=" +Tags.EXPENSE+ " AND date(date)>=date('now','start of year','"+2+" month') AND date(date)<=date('now','start of year','"+3+" month','-1 day') group by date order by date desc"));*/
                return expenseList;
            }

            @Override
            protected void onPostExecute(List<AddIncome> expenseList) {
                super.onPostExecute(expenseList);
                ExpenseHistoryActivity.this.expenseList = expenseList;
                if (expenseList.size() == 0) {
                    mNoDataImg.setVisibility(View.VISIBLE);
                   /* mNoDataFoundTv.setVisibility(View.VISIBLE);*/
                    recyclerView.setVisibility(View.GONE);
                } else {
                    mNoDataImg.setVisibility(View.GONE);
                  /*  mNoDataFoundTv.setVisibility(View.GONE);*/
                    recyclerView.setVisibility(View.VISIBLE);
                    ExpenseHisAdapter expenseHisAdapter = new ExpenseHisAdapter(ExpenseHistoryActivity.this, expenseList);
                    recyclerView.setAdapter(expenseHisAdapter);
                    showGraph();
                }
            }
        }

        GetExpenseHistory gh = new GetExpenseHistory();
        gh.execute();
    }


    private void getListByMonth(final int monthId) {

        class GetListByMonth extends AsyncTask<Void, Void, List<AddIncome>> {

            @Override
            protected List<AddIncome> doInBackground(Void... voids) {
                ExpenseHistoryActivity.this.expenseList.clear();
                List<AddIncome> expenseList = DatabaseClient
                        .getInstance(ExpenseHistoryActivity.this)
                        .getAppDatabase()
                        .addIncomeDao()
                        .getDropDownMonthTxns(new SimpleSQLiteQuery("SELECT id,fromm,mode,description,type,account_id,created_at,date, SUM(amount) AS amount FROM AddIncome WHERE type=" + Tags.EXPENSE + " AND date(date)>=date('now','start of year','" + monthId + " month') AND date(date)<=date('now','start of year','" + monthId + 1 + " month','-1 day') group by date order by date desc"));
                return expenseList;
            }

            @Override
            protected void onPostExecute(List<AddIncome> expenseList) {
                super.onPostExecute(expenseList);

                ExpenseHistoryActivity.this.expenseList = expenseList;
                if (expenseList.size() == 0) {
                    mNoDataFoundTv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    mNoDataFoundTv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    ExpenseHisAdapter expenseHisAdapter = new ExpenseHisAdapter(ExpenseHistoryActivity.this, expenseList);
                    recyclerView.setAdapter(expenseHisAdapter);
                    showGraph();
                }

            }
        }
        GetListByMonth gm = new GetListByMonth();
        gm.execute();
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    //--------------------------Month Adapter--------------------------------------------//
    public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder> {

        int selectedItemposition;
        Context activity;
        private ArrayList<MonthModel> moviesList;

        public MonthAdapter(Context activity, ArrayList<MonthModel> moviesList) {
            this.moviesList = moviesList;
            this.activity = activity;


        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinnerlayout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final MonthModel monthModel = moviesList.get(position);
            holder.spinerItem.setText(monthModel.getMonthName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mToolbarTitle.setText(monthModel.getMonthName());
                    getListByMonth(monthModel.getMonthId());
                    alertDialog.dismiss();

                }
            });

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView spinerItem;
            ImageView listCheck;

            public ViewHolder(View convertView) {
                super(convertView);
                spinerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
                listCheck = (ImageView) convertView.findViewById(R.id.spinnerCheck);
            }
        }
    }


    //---------------------------RecentTxnAdapter------------------------------------//
    public class ExpenseHisAdapter extends RecyclerView.Adapter<ExpenseHisAdapter.MyViewHolder> {

        Context context;
        List<AddIncome> childFeedList;


        public ExpenseHisAdapter(Context context, List<AddIncome> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_his_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final AddIncome addIncome = childFeedList.get(position);
            holder.mAccountNameTv.setText(addIncome.getFromm());
            holder.mDescriptionTv.setText(addIncome.getDescription());
            holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + addIncome.getAmount());
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
