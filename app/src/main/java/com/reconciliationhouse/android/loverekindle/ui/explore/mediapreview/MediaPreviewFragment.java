package com.reconciliationhouse.android.loverekindle.ui.explore.mediapreview;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentMediaPreviewBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.ui.explore.mediagallery.MediaGalleryFragmentDirections;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MediaPreviewFragment extends Fragment implements Listeners.MediaItemClickListener {

    private MediaPreviewViewModel mViewModel;
    private FragmentMediaPreviewBinding mBinding;
    private MediaAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMediaPreviewBinding.inflate(inflater, container, false);

        mAdapter = new MediaAdapter(this);
        mBinding.relatedMediaRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mBinding.relatedMediaRv.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setSupportActionBar(mBinding.previewMediaToolbar);
        mBinding.previewMediaToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MediaPreviewFragment.this).navigateUp();
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            String mediaId = MediaPreviewFragmentArgs.fromBundle(getArguments()).getMediaId();
            String category = MediaPreviewFragmentArgs.fromBundle(getArguments()).getCategory();
            MediaPreviewViewModel.Factory viewModelFactory = new MediaPreviewViewModel.Factory(mediaId, category);
            mViewModel = new ViewModelProvider(this, viewModelFactory).get(MediaPreviewViewModel.class);
            mBinding.setViewModel(mViewModel);
            mBinding.setLifecycleOwner(this);
        }

        mViewModel.getRelatedMedia().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItemList) {
                mAdapter.setMediaItems(mediaItemList);
                mBinding.setThereIsMoreRelatedMedia(mediaItemList.size() > 3);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_media_preview_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMediaItemClick(String mediaId, String category) {
        NavHostFragment.findNavController(this)
                .navigate(MediaPreviewFragmentDirections.actionNavigationMediaPreviewSelf(mediaId, category));
    }
}
