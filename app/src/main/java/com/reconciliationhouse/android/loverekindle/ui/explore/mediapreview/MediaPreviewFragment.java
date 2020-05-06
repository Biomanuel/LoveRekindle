package com.reconciliationhouse.android.loverekindle.ui.explore.mediapreview;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentMediaPreviewBinding;

import org.jetbrains.annotations.NotNull;

public class MediaPreviewFragment extends Fragment {

    private MediaPreviewViewModel mViewModel;
    private FragmentMediaPreviewBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_media_preview, container, false);

        if (getArguments() != null) {
            MediaPreviewViewModel.Factory viewModelFactory = new MediaPreviewViewModel.Factory(MediaPreviewFragmentArgs.fromBundle(getArguments()).getMediaId());
            mViewModel = new ViewModelProvider(this, viewModelFactory).get(MediaPreviewViewModel.class);
            mBinding.setViewModel(mViewModel);
            mBinding.setLifecycleOwner(this);
        }

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
}
