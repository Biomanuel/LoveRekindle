package com.reconciliationhouse.android.loverekindle.ui.chat;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.counsellor.CounsellorRequestAdapters;
import com.reconciliationhouse.android.loverekindle.databinding.ChatCategoriesFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.List;

public class ChatCategoriesFragment extends Fragment implements View.OnClickListener {


    private ChatCategoriesViewModel mViewModel;
    private ChatCategoriesFragmentBinding mBinding;
    private CoordinatorLayout bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<UserModel> parentingCounsellors,spiritualCounsellors,relationsHips,healthCounsellors;
    private TextView selectCounsellors;


   private CounsellorRequestAdapters adapters;

    public static ChatCategoriesFragment newInstance() {
        return new ChatCategoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(getParentFragment()).get(ChatCategoriesViewModel.class);
        mBinding = ChatCategoriesFragmentBinding.inflate(inflater, container, false);


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetLayout = view.findViewById(R.id.bottom_sheet_layout);
        recyclerView = view.findViewById(R.id.bottom_sheet_recyclerview);
        progressBar=view.findViewById(R.id.progress_bar);
        selectCounsellors=view.findViewById(R.id.select_a_counsellor);
        setUpBottomSheet();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatCategoriesViewModel.class);

        mBinding.spiritualCouncellor.setOnClickListener(this);
        mBinding.relationshipCouncellor.setOnClickListener(this);
        mBinding.parentingCouncellor.setOnClickListener(this);
        mBinding.healthCouncellor.setOnClickListener(this);






    }

    private void setUpBottomSheet() {


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSheetLayout.setVisibility(View.GONE);

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        bottomSheetLayout.setVisibility(View.GONE);
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

        //Log.d(TAG, "onStateChanged: " + newState);
    }

    @Override
    public void onClick(View v) {
        bottomSheetLayout.setVisibility(View.VISIBLE);


        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);


        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        }

        switch (v.getId()) {
            case R.id.spiritual_councellor:


                adapters = new CounsellorRequestAdapters(ChatCategoriesFragment.this);

                mViewModel.getAllSpiritualCounsellors(progressBar,selectCounsellors).observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
                    @Override
                    public void onChanged(List<UserModel> userModels) {
                        spiritualCounsellors=userModels;

                        adapters.setCounsellors(userModels);
                    }
                });


                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapters);

                break;
            case R.id.parenting_councellor:


                adapters = new CounsellorRequestAdapters(ChatCategoriesFragment.this);

                mViewModel.getAllParentingCounsellors(progressBar,selectCounsellors).observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
                    @Override
                    public void onChanged(List<UserModel> userModels) {
                        parentingCounsellors=userModels;

                        adapters.setCounsellors(userModels);
                    }
                });


                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapters);



                break;
            case R.id.relationship_councellor:


            adapters = new CounsellorRequestAdapters(ChatCategoriesFragment.this);

            mViewModel.getAllMarriageCounsellors(progressBar,selectCounsellors).observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
                @Override
                public void onChanged(List<UserModel> userModels) {
                    relationsHips=userModels;

                    adapters.setCounsellors(userModels);
                }
            });


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapters);

            break;
            case R.id.health_councellor:


            adapters = new CounsellorRequestAdapters(ChatCategoriesFragment.this);

            mViewModel.getAllHealthCounsellors(progressBar,selectCounsellors).observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
                @Override
                public void onChanged(List<UserModel> userModels) {
                    healthCounsellors=userModels;

                    adapters.setCounsellors(userModels);
                }
            });


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapters);

            break;


        }

    }
}
