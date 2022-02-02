package com.project.taskmanager.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.taskmanager.R;
import com.project.taskmanager.fragments.HomeFragment;
import com.project.taskmanager.fragments.LedgerFrg;
import com.project.taskmanager.fragments.Profile;
import com.project.taskmanager.fragments.Reports;
import com.project.taskmanager.models.ModeModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends WalletActivity implements TabLayout.OnTabSelectedListener,OnItemSelectedListener, View.OnClickListener {

    ArrayList<ModeModel> modeList;

    TabLayout tabLayout;
    TextView mToolbarTitle;
    ImageView mBackArrow, mToolBarlogo;
    Spinner mCalenderSpinner;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        modeList=new ArrayList<>();

        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        mBackArrow = findViewById(R.id.mBackArrow);
        mToolBarlogo = findViewById(R.id.mToolBarlogo);
        mCalenderSpinner = findViewById(R.id.calenderSpinner);
        mBackArrow.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.mTablayout);
        tabLayout.addOnTabSelectedListener(this);

        //loading the default fragment
        toHome(HomeFragment.getInstance());
        //Setting up tab layout
        setupTabLayout();



        final ImageView optionsmenu = (ImageView) findViewById(R.id.optionsMenu);
        optionsmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi")
                Context wrapper = new ContextThemeWrapper(HomeActivity.this, R.style.MyPopupMenuStyle);
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
                                startActivity(new Intent(HomeActivity.this,PrivacyPolicyActivity.class));
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


    private void setupTabLayout() {
        tabLayout.getTabAt(0).setCustomView(R.layout.custom_tab);
        tabLayout.getTabAt(1).setCustomView(R.layout.custom_tab);
        tabLayout.getTabAt(2).setCustomView(R.layout.custom_tab);
        tabLayout.getTabAt(3).setCustomView(R.layout.custom_tab);


        TextView tabHome = (TextView) tabLayout.getTabAt(0).getCustomView();
        TextView tabReports = (TextView) tabLayout.getTabAt(1).getCustomView();
        TextView tabLedger = (TextView) tabLayout.getTabAt(2).getCustomView();
        TextView tabProfile = (TextView) tabLayout.getTabAt(3).getCustomView();


        tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_coloured, 0, 0);
        tabHome.setText(getString(R.string.home));

        tabReports.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_reports_grey, 0, 0);
        tabReports.setText(getString(R.string.reports));

        tabLedger.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_ledger_grey, 0, 0);
        tabLedger.setText(getString(R.string.ledger));

        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_grey, 0, 0);
        tabProfile.setText(getString(R.string.profile));

    }

    @Override
    public void onTabSelected(TabLayout.Tab tabs) {

        TextView tab = (TextView) tabs.getCustomView();
        Log.e("onTabSelected", tab.getText().toString());

        if (tab.getText().equals(getString(R.string.reports))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_reports_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toReports(Reports.getInstance());
        } else if (tab.getText().equals(getString(R.string.ledger))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_ledger_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toLedgerFrg(LedgerFrg.getInstance());
        }
        else if (tab.getText().equals(getString(R.string.profile))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toProfile(Profile.getInstance());
        }

        else {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toHome(HomeFragment.getInstance());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tabs) {

        TextView tab = (TextView) tabs.getCustomView();
        Log.e("onTabUnselected", tab.getText().toString());

        if (tab.getText().equals(getString(R.string.reports))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_reports_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));

        } else if (tab.getText().equals(getString(R.string.ledger))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_ledger_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));

        } else if (tab.getText().equals(getString(R.string.profile))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));

        } else {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_grey, 0, 0);
            tab.setTextColor(Color.parseColor("#707070"));
        }



    }

    @Override
    public void onTabReselected(TabLayout.Tab tabs) {

        TextView tab = (TextView) tabs.getCustomView();

        if (tab.getText().equals(getString(R.string.reports))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_reports_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toReports(Reports.getInstance());
        } else if (tab.getText().equals(getString(R.string.ledger))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_ledger_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toLedgerFrg(LedgerFrg.getInstance());
        } else if (tab.getText().equals(getString(R.string.profile))) {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toProfile(Profile.getInstance());
        } else {
            tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_coloured, 0, 0);
            tab.setTextColor(Color.parseColor("#0786e5"));
            toHome(HomeFragment.getInstance());
        }

    }

    @Override
    public void onClick(View v) {
        super.onBackPressed();

    }


    @Override
    public void setToolBarTitle(String titleName) {
        mToolbarTitle.setText(titleName);
    }

    @Override
    public void toggleBackArrowVisiblity(int visiblity) {
        mBackArrow.setVisibility(visiblity);
    }

    @Override
    public void toggleTabVisiblity(int visiblity) {
        tabLayout.setVisibility(visiblity);
    }

    @Override
    public void toggleCalenderVisiblity(int visiblity) {
        mCalenderSpinner.setVisibility(visiblity);

    }

    @Override
    public void selectTab(int position) {
        tabLayout.getTabAt(position).select();
    }

    @Override
    public ImageView getToolBarImage() {
        return mToolBarlogo;
    }

    @Override
    public TextView getToolBarText() {
        return mToolbarTitle;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),
                mCalenderSpinner.getItemAtPosition(position).toString(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onBackPressed() {
        String currentFragment = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        if (currentFragment.equals(HomeFragment.getTagName())
                || currentFragment.equals(Reports.getTagName())
                || currentFragment.equals(LedgerFrg.getTagName())
                || currentFragment.equals(Profile.getTagName())
                || currentFragment.equals(Profile.getTagName())) {
        } else {

        }
        super.onBackPressed();

    }
}
