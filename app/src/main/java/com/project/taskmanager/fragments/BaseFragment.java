package com.project.taskmanager.fragments;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.project.taskmanager.activities.HomeActivity;
import com.project.taskmanager.interfaces.HomeInteractiveListener;


public class BaseFragment extends Fragment {

    protected HomeInteractiveListener homeInteractiveListener;
    public Activity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = null;

        if (context instanceof HomeActivity) {
            activity = (HomeActivity) context;
        }

        homeInteractiveListener = (HomeInteractiveListener) activity;

    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.toggleTabVisiblity(View.GONE);
    }



}
