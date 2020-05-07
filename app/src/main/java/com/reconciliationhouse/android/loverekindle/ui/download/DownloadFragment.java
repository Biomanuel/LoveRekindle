package com.reconciliationhouse.android.loverekindle.ui.download;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.LibraryMediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.DownloadFragmentBinding;
import com.reconciliationhouse.android.loverekindle.databinding.PurchaseFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.ui.purchase.PurchaseViewModel;
import com.reconciliationhouse.android.loverekindle.ui.purchase.Purchased_Fragment;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import java.util.List;

public class DownloadFragment extends Fragment implements Listeners.MediaItemClickListener{

    private DownloadViewModel mViewModel;

    private DownloadFragmentBinding binding;
    PurchaseViewModel purchaseViewModel;

   public  DownloadFragment(){

   }
    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        purchaseViewModel = new ViewModelProvider(getParentFragment()).get(PurchaseViewModel.class);

        // Inflate the layout for this fragment
        binding = DownloadFragmentBinding.inflate(inflater, container, false);
        final LibraryMediaAdapter mAdapter = new LibraryMediaAdapter(DownloadFragment.this);
        purchaseViewModel.getAllMedia().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {

                mAdapter.setMediaItems(mediaItems);
            }
        });
        binding.downloadRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.downloadRecyclerview.setAdapter(mAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DownloadViewModel.class);

    }

    @Override
    public void onMediaItemClick(String mediaId, MediaItem.MediaType mediaType) {

    }
}
