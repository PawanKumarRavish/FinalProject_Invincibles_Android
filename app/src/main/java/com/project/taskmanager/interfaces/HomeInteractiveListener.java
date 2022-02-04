package com.project.taskmanager.interfaces;

import androidx.fragment.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 04/03/2019.
 */

public interface HomeInteractiveListener {
    int HOME_TAB=0;
    int REPORTS_TAB=1;
    int LEDGER_TAB=2;
    int PROFILE_TAB=3;



    void setToolBarTitle(String titleName);
    void toggleBackArrowVisiblity(int visiblity);
    void toggleTabVisiblity(int visiblity);
    void toggleCalenderVisiblity(int visiblity);
    void selectTab(int position);
    ImageView getToolBarImage();
    TextView getToolBarText();


    void toHome(Fragment frg);
    void toAddIncome(Fragment frg);
    void toAddAccount(Fragment frg);
    void toAddCategory(Fragment frg);
    void toLedgerFrg(Fragment frg);
    void toLedgerDetails(Fragment frg);
    void toDemo(Fragment frg);
    void toExpenseHistory(Fragment frg);
    void toProfile(Fragment frg);
    void toReports(Fragment frg);
    void toAddBankFrg(Fragment frg);


}
