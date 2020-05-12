package com.reconciliationhouse.android.loverekindle.ui.purchase;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.SharedViewModel;
import com.reconciliationhouse.android.loverekindle.adapters.LibraryMediaAdapter;
import com.reconciliationhouse.android.loverekindle.adapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAllMediaBinding;
import com.reconciliationhouse.android.loverekindle.databinding.PurchaseFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;


import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import java.util.List;
import java.util.logging.Logger;

public class Purchased_Fragment extends Fragment implements Listeners.MediaItemClickListener {
    SharedViewModel viewModel;
    PurchaseViewModel purchaseViewModel;
    BottomSheetBehavior bottomSheetBehavior;
    private PurchaseViewModel mViewModel;
    private PurchaseFragmentBinding binding;
    private CoordinatorLayout bottomSheetLayout;


    public Purchased_Fragment() {

    }

    public static Purchased_Fragment newInstance() {
        return new Purchased_Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        purchaseViewModel = new ViewModelProvider(getParentFragment()).get(PurchaseViewModel.class);

        // Inflate the layout for this fragment
        binding = PurchaseFragmentBinding.inflate(inflater, container, false);
        final LibraryMediaAdapter mAdapter = new LibraryMediaAdapter(Purchased_Fragment.this);
        purchaseViewModel.getAllMedia().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {

                mAdapter.setMediaItems(mediaItems);
            }
        });
        binding.purchasedRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.purchasedRecyclerview.setAdapter(mAdapter);


        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        RecyclerView recyclerView=view.findViewById(R.id.bottom_sheet_recyclerview);
//
//        final MediaAdapter adapter = new MediaAdapter(Purchased_Fragment.this);
//        purchaseViewModel.getAllMedia().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
//            @Override
//            public void onChanged(List<MediaItem> mediaItems) {
//
//                adapter.setMediaItems(mediaItems);
//            }
//        });
//
//
//        recyclerView.setLayoutManager(new GridLayoutManager( getContext(),2));
//        recyclerView.setAdapter(adapter);

        //setUpBottomSheet();b n

        binding.loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PurchaseViewModel.class);
        viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onMediaItemClick(String mediaId, String category) {

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
        binding.loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Iiiiiiiiiiiiiiiiiiiiiii", "Clicked");

                bottomSheetLayout.setVisibility(View.VISIBLE);


                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            }
        });
        //Log.d(TAG, "onStateChanged: " + newState);
    }
}
