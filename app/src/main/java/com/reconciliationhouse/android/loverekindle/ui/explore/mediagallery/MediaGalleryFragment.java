package com.reconciliationhouse.android.loverekindle.ui.explore.mediagallery;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.ExplorePagerAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentMediaGalleryBinding;

import org.jetbrains.annotations.NotNull;

import static com.foresightridge.childbirth.utility.ViewUtilsKt.reduceDragSensitivity;

public class MediaGalleryFragment extends Fragment {

    private MediaGalleryViewModel mMediaGalleryViewModel;
    private FragmentMediaGalleryBinding mBinding;
    private SavedStateViewModelFactory mFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mMediaGalleryViewModel = new ViewModelProvider(this, mFactory).get(MediaGalleryViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_media_gallery, container, false);
        ViewPager2 viewPager = mBinding.exploreViewpager;
        TabLayout tabLayout = mBinding.exploreTabs;

        reduceDragSensitivity(viewPager);

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
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setSupportActionBar(mBinding.exploreToolbar);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_explore_fragment, menu);

        mMediaGalleryViewModel.getIsFiltered().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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
                mMediaGalleryViewModel.filter();
                return true;
            case R.id.action_unfilter:
                mMediaGalleryViewModel.unfilter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFactory = new SavedStateViewModelFactory(requireActivity().getApplication(), requireActivity());
    }
}
