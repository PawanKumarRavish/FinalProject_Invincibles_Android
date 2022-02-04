package com.project.taskmanager.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.taskmanager.R;
import com.project.taskmanager.Utils;

/**
 * Created by admin on 15/03/2019.
 */

public class DemoFrg extends BottomSheetDialogFragment {


    private static DemoFrg mInstance;

    public DemoFrg() {
    }

    public static DemoFrg getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new DemoFrg();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new DemoFrg();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
