package com.project.taskmanager.activities;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.project.taskmanager.R;
import com.project.taskmanager.fragments.AddCategoryFrg;
import com.project.taskmanager.fragments.AllCategoriesFrg;
import com.project.taskmanager.fragments.AllTaskFrg;
import com.project.taskmanager.fragments.HomeFragment;
import com.project.taskmanager.fragments.Profile;
import com.project.taskmanager.models.ModeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends WalletActivity implements View.OnClickListener {

    ArrayList<ModeModel> modeList;

    TabLayout tabLayout;
    TextView mToolbarTitle;
    ImageView mBackArrow, mToolBarlogo;
    Spinner mCalenderSpinner;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.mBottomNavView)
    BottomNavigationView mBottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mBottomNavView.getMenu().clear(); //clear old inflated items.
        mBottomNavView.inflateMenu(R.menu.bottom_nav);

        modeList = new ArrayList<>();

        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        mBackArrow = findViewById(R.id.mBackArrow);
        mToolBarlogo = findViewById(R.id.mToolBarlogo);
        mCalenderSpinner = findViewById(R.id.calenderSpinner);
        mBackArrow.setOnClickListener(this);


        mBottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottom_home) {
                    HomeFragment homeFragment=new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
                } else if (item.getItemId() == R.id.bottom_categories) {

                    AllCategoriesFrg ledgerFrg =new AllCategoriesFrg();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, ledgerFrg).commit();

                } else if (item.getItemId() == R.id.bottom_alltasks) {
                    AllTaskFrg allTaskFrg =new AllTaskFrg();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, allTaskFrg).commit();
                } else if (item.getItemId() == R.id.bottom_profile) {
                    Profile allCategoriesFrg=new Profile();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, allCategoriesFrg).commit();
                }
                return true;
            }
        });

        //loading the default fragment
        toHome(HomeFragment.getInstance());


        //Setting up tab layout
        //setupTabLayout();



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
        //tabLayout.setVisibility(visiblity);
    }

    @Override
    public void toggleCalenderVisiblity(int visiblity) {
        mCalenderSpinner.setVisibility(visiblity);

    }

    @Override
    public void selectTab(int position) {
      //  tabLayout.getTabAt(position).select();
    }

    @Override
    public ImageView getToolBarImage() {
        return mToolBarlogo;
    }

    @Override
    public TextView getToolBarText() {
        return mToolbarTitle;
    }



}
