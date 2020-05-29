package com.reconciliationhouse.android.loverekindle.ui.explore.mediapreview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.recycleradapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.adapters.recycleradapters.MediaReviewAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.DialogMediaPreviewLayoutBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;
import com.reconciliationhouse.android.loverekindle.ui.explore.mediapreview.MediaPreviewViewModel;

import java.util.List;

public class DialogMediaPreview extends DialogFragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 0;
    static int RESULT_CODE_SHARE = 12;

    CallbackResult callbackResult;
    private MediaPreviewViewModel mViewModel;
    private DialogMediaPreviewLayoutBinding mBinding;
    private Assignment purpose;

    public DialogMediaPreview() {
    }

    /**
     * This dialog fragment is to function only for MediaPreviewFragment for display more details of
     * any mediaItem parameter.
     */
    DialogMediaPreview(final CallbackResult callbackResult, Assignment assignment) {
        this.callbackResult = callbackResult;
        purpose = assignment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert getParentFragment() != null;
        mViewModel = new ViewModelProvider(getParentFragment()).get(MediaPreviewViewModel.class);
        if (purpose == null && savedInstanceState != null)
            purpose = (Assignment) savedInstanceState.getSerializable("Purpose");
        mBinding = DialogMediaPreviewLayoutBinding.inflate(inflater, container, false);
        mBinding.setAppbarClickListener(this);
        mBinding.setViewModel(mViewModel);
        mBinding.setPurpose(purpose);

        switch (purpose) {
            case MORE_REVIEWS:
                initForMoreReviews();
                break;
            case RELATED_MEDIA:
                initForMoreRelatedMedia();
                break;
            case MORE_ABOUT:
                initForReadMoreAboutMedia();
                break;
            case EDIT_REVIEW:
                break;
        }

        return mBinding.getRoot();
    }

    private void initForReadMoreAboutMedia() {

    }

    private void initForMoreReviews() {
        final MediaReviewAdapter mAdapter = new MediaReviewAdapter();
        mBinding.listItemRv.setAdapter(mAdapter);
        mBinding.listItemRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mViewModel.getReviews().observe(getViewLifecycleOwner(), new Observer<List<MediaReview>>() {
            @Override
            public void onChanged(@NonNull List<MediaReview> mediaReviews) {
                mAdapter.setReviews(mediaReviews);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Purpose", purpose);
    }

    private void initForMoreRelatedMedia() {
        final MediaAdapter mAdapter = new MediaAdapter((MediaPreviewFragment) this.getParentFragment());
        mBinding.listItemRv.setAdapter(mAdapter);
        mBinding.listItemRv.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mViewModel.getRelatedMedia().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(@NonNull List<MediaItem> mediaItemList) {
                mAdapter.setMediaItems(mediaItemList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_share:
                if (callbackResult != null)
                    callbackResult.onDialogResults(REQUEST_CODE, RESULT_CODE_SHARE);
                dismiss();
                break;
            case R.id.bt_close:
                dismiss();
                break;
        }
    }

    public enum Assignment {
        MORE_REVIEWS, RELATED_MEDIA, MORE_ABOUT, EDIT_REVIEW
    }

    public interface CallbackResult {
        void onDialogResults(int requestCode, int resultCode);
    }

}