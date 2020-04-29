package com.reconciliationhouse.android.loverekindle.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.ExplorePagerAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mExploreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mExploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        FragmentExploreBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        ViewPager2 viewPager = binding.exploreViewpager;
        TabLayout tabLayout = binding.exploreTabs;

        viewPager.setAdapter(new ExplorePagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.OnConfigureTabCallback() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(ExplorePagerAdapter.getPageTitle(position));
            }
        }).attach();

        return binding.getRoot();
    }
}
