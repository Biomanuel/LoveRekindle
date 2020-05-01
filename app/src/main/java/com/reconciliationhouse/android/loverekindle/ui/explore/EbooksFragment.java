package com.reconciliationhouse.android.loverekindle.ui.explore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentEbooksBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EbooksFragment extends Fragment implements Listeners.MediaItemClickListener {

    private ExploreViewModel mExploreViewModel;
    private MediaAdapter mAdapter;

    public EbooksFragment() {
        // Required empty public constructor
    }

    public static EbooksFragment newInstance() {
        return new EbooksFragment();
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mExploreViewModel = new ViewModelProvider(getParentFragment()).get(ExploreViewModel.class);

        // Inflate the layout for this fragment
        final FragmentEbooksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ebooks, container, false);
        mAdapter = new MediaAdapter(this);

        mExploreViewModel.getEbooks().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                mAdapter.setMediaItems(mediaItems);
                binding.setListNotEmpty(mAdapter.getItemCount() > 0);
            }
        });
        binding.rvEbooks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvEbooks.setAdapter(mAdapter);


        return binding.getRoot();
    }

    private Boolean isNetworkAvailable() {

        if (getActivity() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    public void onMediaItemClick(String mediaId, MediaItem.MediaType mediaType) {

    }
}
