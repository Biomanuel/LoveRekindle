package com.reconciliationhouse.android.loverekindle.ui.explore;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentExploreBinding;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;


public class ExploreFragment extends Fragment {

    private FragmentExploreBinding mBinding;

    public ExploreFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        UserRepo.initializeWithUser((MainActivity) requireActivity());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);

        return mBinding.getRoot();
    }

//    @Nullable
//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if (getParentFragment() != null && getParentFragment().isDetached()) {
//            return AnimationUtils.loadAnimation(getContext(), R.anim.stay);
//        }
//        return super.onCreateAnimation(transit, enter, nextAnim);
//    }
}
