package com.project.taskmanager.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 18/03/2019.
 */

public class ExpenseHistoryFrg extends BaseFragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mChart;
    private List<AddIncome> expenseList;
    private HashMap<String,AddIncome> dataMap=new HashMap<>();


    RecyclerView recyclerView;

    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    Unbinder unbinder;

    private static ExpenseHistoryFrg mInstance;
    public static ExpenseHistoryFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new ExpenseHistoryFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new ExpenseHistoryFrg();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expense_history_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart = (LineChart) view.findViewById(R.id.chart);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getExpenseHistory();
    }

    private void showGraph() {
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        float maxAmount=0;
        for (AddIncome expIncome:expenseList){
            dataMap.put(Utils.getDayInString(expIncome.getDate()),expIncome);
            if(Float.parseFloat(expIncome.getAmount())>maxAmount){
                maxAmount=Float.parseFloat(expIncome.getAmount());
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

    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=1;i<=31;i++){
            xVals.add(i+"");
        }
        return xVals;
    }

    private ArrayList<Entry> setYAxisValues(){


        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for(int i=0;i<=30;i++){
            String keyName=i<10?"0"+(i+1):""+(i+1);
            if(dataMap.get(keyName)!=null) {
                yVals.add(new Entry(Float.parseFloat(dataMap.get(keyName).getAmount()), i));
            }else{
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
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .addIncomeDao()
                        .getLatestTxns(Tags.EXPENSE);
                return expenseList;
            }

            @Override
            protected void onPostExecute(List<AddIncome> expenseList) {
                super.onPostExecute(expenseList);
                ExpenseHistoryFrg.this.expenseList=expenseList;
                if (expenseList.size() == 0) {
                    mNoDataFoundTv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    mNoDataFoundTv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    ExpenseHisAdapter expenseHisAdapter = new ExpenseHisAdapter(getActivity(), expenseList);
                    recyclerView.setAdapter(expenseHisAdapter);
                    showGraph();
                }
            }
        }

        GetExpenseHistory gh = new GetExpenseHistory();
        gh.execute();
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
        homeInteractiveListener.setToolBarTitle("Expense History");
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
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


    //---------------------------RecentTxnAdapter------------------------------------//
    public class ExpenseHisAdapter extends RecyclerView.Adapter<ExpenseHisAdapter.MyViewHolder> {

        Context context;
        List<AddIncome> childFeedList;


        public ExpenseHisAdapter(Context context, List<AddIncome> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public ExpenseHisAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_his_design, parent, false);
            return new ExpenseHisAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ExpenseHisAdapter.MyViewHolder holder, int position) {
            final AddIncome addIncome = childFeedList.get(position);
            holder.mAccountNameTv.setText(addIncome.getFromm());
            holder.mDescriptionTv.setText(addIncome.getDescription());
            holder.mAmountTv.setText(SharedPreference.getCurrency() + " " + addIncome.getAmount());
            holder.mDateTv.setText(Utils.getDayInString(addIncome.getDate()));
            String month = Utils.getMonthInString(addIncome.getDate());
            holder.mMonthTv.setText(month.substring(0, 3));
            holder.mModeTv.setText("Mode: "+addIncome.getActualModeName());

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
