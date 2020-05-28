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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;

import com.reconciliationhouse.android.loverekindle.SharedViewModel;
import com.reconciliationhouse.android.loverekindle.adapters.LibraryPagerAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAllMediaBinding;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    BottomSheetBehavior bottomSheetBehavior1;

    private ProfileViewModel mProfileViewModel;
    private SharedViewModel sharedViewModel;
    private Toolbar toolbar;
    private Menu mMenu;
    private CoordinatorLayout bottomSheetLayout1;
    private FragmentProfileBinding mBinding;
    private FirebaseAuth mAuth;

    public ProfileFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser.getEmail()==null) {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_navigation_profile_to_loginFragment);
        }
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mBinding = FragmentProfileBinding.inflate(inflater, container, false);
 mAuth=FirebaseAuth.getInstance();
        mBinding.libraryPager.setAdapter(new LibraryPagerAdapter(this));
        new TabLayoutMediator(mBinding.libraryTabs, mBinding.libraryPager, new TabLayoutMediator.OnConfigureTabCallback() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(LibraryPagerAdapter.getPageTitle(position));
            }
        }).attach();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setSupportActionBar(mBinding.toolbar);
        setHasOptionsMenu(true);

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);

        bottomSheetLayout1 = view.findViewById(R.id.bottom_sheet_layout);


        mBinding.recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_profile_to_rechargeFragment);
            }
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (Objects.requireNonNull(currentUser).getPhotoUrl()!=null) {
            mBinding.username.setText(currentUser.getDisplayName());
            mBinding.email.setText(currentUser.getEmail());
            Picasso.get().load(currentUser.getPhotoUrl()).into(mBinding.userProfileImage);

        }

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
            case R.id.settings:
                Toast.makeText(getContext(), "I was Clicked", Toast.LENGTH_SHORT).show();
                NavController navController = NavHostFragment.findNavController(ProfileFragment.this);
                navController.navigate(R.id.action_navigation_profile_to_settingsFragment);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpBottomSheet() {
        bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheetLayout1);
        // set callback for changes
        bottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
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

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }

        });


    }


}
