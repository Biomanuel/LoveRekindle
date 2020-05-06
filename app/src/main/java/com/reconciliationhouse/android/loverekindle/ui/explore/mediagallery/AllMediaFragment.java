package com.reconciliationhouse.android.loverekindle.ui.explore.mediagallery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAllMediaBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.ui.explore.ExploreFragment;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AllMediaFragment extends Fragment implements Listeners.MediaItemClickListener {

    private MediaGalleryViewModel mMediaGalleryViewModel;
    private MediaAdapter mAdapter;
    private FragmentAllMediaBinding mBinding;

    public AllMediaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = FragmentAllMediaBinding.inflate(inflater, container, false);
        mAdapter = new MediaAdapter(this);

        mBinding.rvAllMedia.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mBinding.rvAllMedia.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMediaGalleryViewModel = new ViewModelProvider(getParentFragment()).get(MediaGalleryViewModel.class);
        mMediaGalleryViewModel.getAllMedia().observe(getParentFragment().getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                mAdapter.setMediaItems(mediaItems);
                mBinding.setListNotEmpty(mAdapter.getItemCount() > 0);
            }
        });
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
        if (getParentFragment() != null)
            NavHostFragment.findNavController(getParentFragment())
                    .navigate(MediaGalleryFragmentDirections.actionNavigationMediaGalleryToNavigationMediaPreview(mediaId));
    }
}
