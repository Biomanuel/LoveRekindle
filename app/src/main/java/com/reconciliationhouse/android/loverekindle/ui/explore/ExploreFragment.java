package com.reconciliationhouse.android.loverekindle.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.ExplorePagerAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mExploreViewModel;
    private Menu mMenu;
    private FragmentExploreBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mExploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        ViewPager2 viewPager = mBinding.exploreViewpager;
        TabLayout tabLayout = mBinding.exploreTabs;

        viewPager.setAdapter(new ExplorePagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.OnConfigureTabCallback() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(ExplorePagerAdapter.getPageTitle(position));
            }
        }).attach();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setSupportActionBar(mBinding.exploreToolbar);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.menu_explore_fragment, menu);

        mExploreViewModel.getIsFiltered().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFiltered) {
                if (isFiltered) {
                    menu.findItem(R.id.action_filter_by_category).setVisible(false);
                    menu.findItem(R.id.action_unfilter).setVisible(true);
                } else {
                    menu.findItem(R.id.action_filter_by_category).setVisible(true);
                    menu.findItem(R.id.action_unfilter).setVisible(false);
                }
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_by_category:
                mExploreViewModel.filter();
                return true;
            case R.id.action_unfilter:
                mExploreViewModel.unfilter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
