package com.reconciliationhouse.android.loverekindle.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;

import com.reconciliationhouse.android.loverekindle.SharedViewModel;
import com.reconciliationhouse.android.loverekindle.adapters.LibraryPagerAdapter;



public class ProfileFragment extends Fragment{
    private SharedViewModel sharedViewModel;

    private ProfileViewModel mProfileViewModel;
    private Toolbar toolbar;
    private Menu mMenu;
    private CoordinatorLayout bottomSheetLayout1;
    BottomSheetBehavior bottomSheetBehavior1;

    //private FragmentProfileBinding mBinding;
    public ProfileFragment(){

    }
    public View onCreateView( LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      // mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        ViewGroup group=(ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        ViewPager2 viewPager = group.findViewById(R.id.library_pager);
        TabLayout tabLayout = group.findViewById(R.id.library_tabs);
        toolbar=group.findViewById(R.id.toolbar);



        viewPager.setAdapter(new LibraryPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.OnConfigureTabCallback() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(LibraryPagerAdapter.getPageTitle(position));
            }
        }).attach();

        return group;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        bottomSheetLayout1=view.findViewById(R.id.bottom_sheet_layout);


        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        setUpBottomSheet();
        sharedViewModel.getType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("purchase")){
                    bottomSheetLayout1.setVisibility(View.VISIBLE);


                    if (bottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);



                    } else {
                        bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    }

                }


            }
        });

    }
    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.profile_menu, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }
    private void setUpBottomSheet(){
        bottomSheetBehavior1=BottomSheetBehavior.from(bottomSheetLayout1);
        // set callback for changes
        bottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSheetLayout1.setVisibility(View.GONE);

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        bottomSheetLayout1.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:


                        break;
                }

            }
            @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }

        });

        //Log.d(TAG, "onStateChanged: " + newState);
    }

//    @Override
//    public void onButtonClick(String type) {
//        if (type.equals("purchase")){

   // }
}
