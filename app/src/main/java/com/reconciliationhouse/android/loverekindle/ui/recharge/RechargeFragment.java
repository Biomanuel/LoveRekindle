package com.reconciliationhouse.android.loverekindle.ui.recharge;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reconciliationhouse.android.loverekindle.R;

public class RechargeFragment extends Fragment {

    private RechargeViewModel mViewModel;

    public static RechargeFragment newInstance() {
        return new RechargeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recharge_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RechargeViewModel.class);
        // TODO: Use the ViewModel
    }

}
