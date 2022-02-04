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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.AddIncome;
import com.project.taskmanager.interfaces.Tags;
import com.project.taskmanager.models.LegendModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.project.taskmanager.interfaces.HomeInteractiveListener.REPORTS_TAB;

/**
 * Created by Gaganjot Singh on 11/04/2019.
 */

public class Reports extends BaseFragment implements OnChartValueSelectedListener {

    RecyclerView recyclerView;
    RecyclerView recyclerVieww;
    LegendModel legendModel;
    ArrayList<LegendModel> parentFeedList;
    LegendAdapter legendAdapter;
    Legend legend;
    PieDataSet dataSet;

    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    Unbinder unbinder;

    @BindView(R.id.noData_img)
    ImageView mNoDataImg;

    @BindView(R.id.noData_ll)
    LinearLayout mNoDataLl;

    private PieChart pieChart;

    private static Reports mInstance;

    public static Reports getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new Reports();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new Reports();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentFeedList=new ArrayList<>();

        pieChart = (PieChart) view.findViewById(R.id.piechart);
        //pieChart.setUsePercentValues(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        try {
            List<AddIncome> expenseList = getExpenseHistory();
            ArrayList<Entry> yvalues = new ArrayList<Entry>();
            ArrayList<String> xVals = new ArrayList<String>();

            for (int i = 0; i < expenseList.size(); i++) {
                yvalues.add(new Entry(Float.valueOf(expenseList.get(i).getAmount()), i));
                xVals.add(expenseList.get(i).getFromm());

                legendModel=new LegendModel(expenseList.get(i).getFromm()); // new
                parentFeedList.add(legendModel);// new

                Log.e("Params",parentFeedList.toString());
            }


            recyclerVieww = (RecyclerView) view.findViewById(R.id.recyclerVieww);
            recyclerVieww.setHasFixedSize(true);
            recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

            legendAdapter= new LegendAdapter(getActivity(), parentFeedList);
            recyclerVieww.setAdapter(legendAdapter);



            dataSet = new PieDataSet(yvalues, "");
            dataSet.setSliceSpace(3); // adding slice between pie chart
            dataSet.setSelectionShift(5);

                /*// add many colors
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : ColorTemplate.VORDIPLOM_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.LIBERTY_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);*/

            PieData data = new PieData(xVals, dataSet);
               /* data.setValueFormatter(new PercentFormatter() {
                    @Override
                    public String getFormattedValue(float value, YAxis yAxis) {
                        return SharedPreference.getCurrency() + " " + mFormat.format(value);
                    }

                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return SharedPreference.getCurrency() + " " + mFormat.format(value);
                    }
                });*/
            pieChart.setData(data);
            pieChart.setDescription("");

            legend = pieChart.getLegend();
            int colourCodes[]=legend.getColors();
            legend.setForm(Legend.LegendForm.SQUARE);
            legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
            legend.setXEntrySpace(7);
            legend.setYEntrySpace(5);
            legend.setWordWrapEnabled(true);

            pieChart.setDrawHoleEnabled(true);
            pieChart.setTransparentCircleRadius(25f);
            pieChart.setHoleRadius(65f);

            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            data.setValueTextSize(10f);
            pieChart.animateXY(1400, 1400);
            data.setValueTextColor(Color.DKGRAY);

            data.setDrawValues(false);  // removing x values
            pieChart.setDrawSliceText(false); // removing y values
            pieChart.invalidate();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private List<AddIncome> getExpenseHistory() throws ExecutionException, InterruptedException {
        class GetExpenseHistory extends AsyncTask<Void, Void, List<AddIncome>> {


            @Override
            protected List<AddIncome> doInBackground(Void... voids) {
                List<AddIncome> expenseList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .addIncomeDao()
                        .getLastNDaysReport(Tags.EXPENSE, 10);
                return expenseList;
            }

            @Override
            protected void onPostExecute(List<AddIncome> expenseList) {
                super.onPostExecute(expenseList);
                //consumer.accept(expenseList);
                if (expenseList.size() == 0) {
                    mNoDataLl.setVisibility(View.VISIBLE);
                   /* mNoDataFoundTv.setVisibility(View.VISIBLE);*/
                    recyclerView.setVisibility(View.GONE);
                    recyclerVieww.setVisibility(View.GONE);
                } else {
                    mNoDataLl.setVisibility(View.GONE);
                   /* mNoDataFoundTv.setVisibility(View.GONE);*/
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerVieww.setVisibility(View.VISIBLE);

                    //Toast.makeText(getActivity(), " Got pie chart history", Toast.LENGTH_LONG).show();

                    HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), expenseList);
                    recyclerView.setAdapter(historyAdapter);
                }
            }
        }

        GetExpenseHistory gh = new GetExpenseHistory();
        return gh.execute().get();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private interface MyConsumer<T> {
        void accept(T o);
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }


    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.selectTab(REPORTS_TAB);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setToolBarTitle("Expense Reports");
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.VISIBLE);
    }

    public static String getTagName() {
        return Reports.class.getSimpleName();
    }

    //---------------------------RecentTxnAdapter------------------------------------//
    public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

        Context context;
        List<AddIncome> childFeedList;
        private int lastCheckedPosition = -1;


        public HistoryAdapter(Context context, List<AddIncome> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_design, parent, false);
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
            holder.mModeTv.setText( addIncome.getActualModeName());

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



    //---------------------------LegendAdapter------------------------------------//
    public class LegendAdapter extends RecyclerView.Adapter<LegendAdapter.MyViewHolder> {

        Context context;
        List<LegendModel> childFeedList;
        private int lastCheckedPosition = -1;


        public LegendAdapter(Context context, List<LegendModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.legend_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final LegendModel legendModel = childFeedList.get(position);
            holder.mLegendNameTv.setText(legendModel.getLegendName());

            int colorCodes[] = legend.getColors();
            for (int i = 0; i < legend.getColors().length - 1; i++){
                holder.mColourLl.setBackgroundColor(colorCodes[i]);
            }

            pieChart.getLegend().setWordWrapEnabled(true);
            pieChart.getLegend().setEnabled(false);

        }

        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mLegendNameTv;
            LinearLayout mColourLl;



            public MyViewHolder(View itemView) {
                super(itemView);

                mLegendNameTv = (TextView) itemView.findViewById(R.id.legendName_tv);
                mColourLl=(LinearLayout)itemView.findViewById(R.id.colour_ll);


            }
        }
    }
}
