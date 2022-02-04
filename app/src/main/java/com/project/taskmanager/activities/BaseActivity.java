package com.project.taskmanager.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.taskmanager.R;
import com.project.taskmanager.interfaces.HomeInteractiveListener;

public class BaseActivity extends AppCompatActivity implements HomeInteractiveListener {

    protected void doFragmentTransition(int container, Fragment fragment, FragmentManager manager, boolean addtostack) {

        String fragTag = fragment.getClass().getSimpleName();

        Fragment frag = manager.findFragmentByTag(fragTag);

        if (frag == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(container, fragment, fragTag);

            if (addtostack) {
                transaction.addToBackStack(fragTag);
            }

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commitAllowingStateLoss();

        } else {

            // manager.popBackStackImmediate(fragTag, 0);
            try {
                manager.popBackStackImmediate(fragTag, 0);
            } catch (IllegalStateException ignored) {
                // Toast.makeText(this, "" + ignored.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onBackPressed() {
        int stackcount = getSupportFragmentManager().getBackStackEntryCount();

        if (stackcount == 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void setToolBarTitle(String titleName) {

    }

    @Override
    public void toggleBackArrowVisiblity(int visiblity) {

    }

    @Override
    public void toggleTabVisiblity(int visiblity) {

    }

    @Override
    public void toggleCalenderVisiblity(int visiblity) {

    }

    @Override
    public void selectTab(int position) {

    }

    @Override
    public ImageView getToolBarImage() {
        return null;
    }

    @Override
    public TextView getToolBarText() {
        return null;
    }





    @Override
    public void toHome(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toAddIncome(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toAddAccount(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toAddCategory(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toLedgerFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toLedgerDetails(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toDemo(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toExpenseHistory(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toProfile(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toReports(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }

    @Override
    public void toAddBankFrg(Fragment frg) {
        doFragmentTransition(R.id.frame_container, frg, getSupportFragmentManager(), true);
    }
}
