package com.reconciliationhouse.android.loverekindle.ui.explore.mediagallery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.CatergoryRowLayoutBinding;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAllMediaBinding;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAudiosBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AudiosFragment extends Fragment implements Listeners.MediaItemClickListener {

    private MediaGalleryViewModel mMediaGalleryViewModel;
    private MediaAdapter mAdapter;
    private FragmentAudiosBinding mBinding;
    private HashMap<String, List<MediaItem>> categoryMap;

    private HashMap<String, MediaAdapter> categoryAdapters;
    private boolean sorted = false; // sorted is Flag to know if the category views have been inflated b4.

    public AudiosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = FragmentAudiosBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mAdapter = new MediaAdapter(this);

        mBinding.rvAll.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mBinding.rvAll.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMediaGalleryViewModel = new ViewModelProvider(requireActivity()).get(MediaGalleryViewModel.class);
        mMediaGalleryViewModel.getAudios().observe(getParentFragment().getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                mAdapter.setMediaItems(mediaItems);
                mBinding.setListNotEmpty(mAdapter.getItemCount() > 0);
                categoryMap = mMediaGalleryViewModel.categorise(mediaItems);
                updateCategoryUI();
            }
        });

        mBinding.setIsFiltered(mMediaGalleryViewModel.getIsFiltered());
    }

    private void updateCategoryUI() {
        if (!sorted) {
            categoryAdapters = new HashMap<>();
            LinearLayout categoryList = mBinding.categoriesFilterLayout;
            LayoutInflater inflater = LayoutInflater.from(this.requireActivity());

            for (String category : categoryMap.keySet()) {
                CatergoryRowLayoutBinding rowBinding = CatergoryRowLayoutBinding.inflate(inflater, categoryList, false);
                MediaAdapter rowRvAdapter = new MediaAdapter(this);

                rowBinding.setTitle(category);
                rowBinding.setThereIsMore(categoryMap.get(category).size() > 3);

                rowBinding.mediaListRv.setLayoutManager(new LinearLayoutManager(this.requireContext(), RecyclerView.HORIZONTAL, false));
                rowBinding.mediaListRv.setAdapter(rowRvAdapter);

                categoryAdapters.put(category, rowRvAdapter);
                categoryList.addView(rowBinding.getRoot());
            }
        }
        for (String category : categoryAdapters.keySet())
            categoryAdapters.get(category).setMediaItems(categoryMap.get(category));

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
    public void onMediaItemClick(String mediaId, String category) {
        if (getParentFragment() != null)
            NavHostFragment.findNavController(getParentFragment())
                    .navigate(MediaGalleryFragmentDirections.actionNavigationMediaGalleryToNavigationMediaPreview(mediaId, category));
    }
}
