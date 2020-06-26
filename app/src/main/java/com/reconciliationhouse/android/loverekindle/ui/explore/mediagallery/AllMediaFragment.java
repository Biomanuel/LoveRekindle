package com.reconciliationhouse.android.loverekindle.ui.explore.mediagallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.recycleradapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAllMediaBinding;
import com.reconciliationhouse.android.loverekindle.databinding.MediaCatergoryRowLayoutBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.repository.LocalMediaRepository;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;
import com.reconciliationhouse.android.loverekindle.ui.mediapreview.MediaPreviewActivity;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.reconciliationhouse.android.loverekindle.ui.mediapreview.MediaPreviewActivity.MEDIA_CATEGORY_KEY;
import static com.reconciliationhouse.android.loverekindle.ui.mediapreview.MediaPreviewActivity.MEDIA_ID_KEY;

public class AllMediaFragment extends Fragment implements Listeners.MediaItemClickListener {

    public static final String MEDIA_TYPE_KEY = "MEDIA_TYPE";
    private MediaGalleryViewModel mMediaGalleryViewModel;
    private MediaAdapter mAdapter;
    private FragmentAllMediaBinding mBinding;
    private HashMap<String, List<MediaItem>> categoryMap;
    private MediaItem.MediaType mMediaType;

    private HashMap<String, MediaAdapter> categoryAdapters;
    private boolean sorted = false; // sorted is Flag to know if the category views have been inflated b4.

    public AllMediaFragment() {
        // Required empty public constructor
    }

    public static AllMediaFragment newTypedInstance(MediaItem.MediaType type) {
        AllMediaFragment fragment = new AllMediaFragment();
        Bundle args = new Bundle();
        args.putSerializable(MEDIA_TYPE_KEY, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMediaType = (MediaItem.MediaType) getArguments().getSerializable(MEDIA_TYPE_KEY);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = FragmentAllMediaBinding.inflate(inflater, container, false);
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
        LiveData<List<MediaItem>> media;
        if (mMediaType == MediaItem.MediaType.EBOOK) media = mMediaGalleryViewModel.getEbooks();
        else if (mMediaType == MediaItem.MediaType.AUDIO)
            media = mMediaGalleryViewModel.getAudios();
        else {
            media = mMediaGalleryViewModel.getAllMedia();
            Toast.makeText(requireContext(), "All: " + mMediaType, Toast.LENGTH_LONG).show();
        }

        media.observe(getParentFragment().getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
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
                MediaCatergoryRowLayoutBinding rowBinding = MediaCatergoryRowLayoutBinding.inflate(inflater, categoryList, false);
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
//        if (getParentFragment() != null)
//            NavHostFragment.findNavController(getParentFragment())
//                    .navigate(MediaGalleryFragmentDirections.actionNavigationMediaGalleryToMediaPreviewActivity(mediaId, category));
        Intent toMediaPreview = new Intent(requireActivity(), MediaPreviewActivity.class);
        toMediaPreview.putExtra(MEDIA_ID_KEY, mediaId);
        toMediaPreview.putExtra(MEDIA_CATEGORY_KEY, category);
        startActivity(toMediaPreview);
    }

}
