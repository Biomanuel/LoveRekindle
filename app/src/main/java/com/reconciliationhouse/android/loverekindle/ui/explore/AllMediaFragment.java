package com.reconciliationhouse.android.loverekindle.ui.explore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAllMediaBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AllMediaFragment extends Fragment implements Listeners.MediaItemClickListener {

    private ExploreViewModel mExploreViewModel;
    private MediaAdapter mAdapter;

    public AllMediaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mExploreViewModel = new ViewModelProvider(getParentFragment()).get(ExploreViewModel.class);

        // Inflate the layout for this fragment
        final FragmentAllMediaBinding binding = FragmentAllMediaBinding.inflate(inflater, container, false);
        mAdapter = new MediaAdapter(this);

        mExploreViewModel.getAllMedia().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                mAdapter.setMediaItems(mediaItems);
                binding.setListNotEmpty(mAdapter.getItemCount() > 0);
            }
        });
        binding.rvAllMedia.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvAllMedia.setAdapter(mAdapter);

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
