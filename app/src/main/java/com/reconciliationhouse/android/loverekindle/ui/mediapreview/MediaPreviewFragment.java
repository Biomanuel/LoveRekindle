package com.reconciliationhouse.android.loverekindle.ui.mediapreview;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.reconciliationhouse.android.loverekindle.BuildConfig;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.recycleradapters.MediaAdapter;
import com.reconciliationhouse.android.loverekindle.adapters.recycleradapters.TagsAdapter;
import com.reconciliationhouse.android.loverekindle.data.LocalMedia;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentMediaPreviewBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;
import com.reconciliationhouse.android.loverekindle.utils.PermissionCheck;
import com.reconciliationhouse.android.loverekindle.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.content.FileProvider.getUriForFile;

public class MediaPreviewFragment extends Fragment implements Listeners.MediaItemClickListener,
        Listeners.MediaTagClickListener, DialogMediaPreview.CallbackResult, View.OnClickListener {

    public static final String APP_NAME = "Love Rekindle";
    private static final String TAG = MediaPreviewFragment.class.getSimpleName();
    private MediaPreviewViewModel mViewModel;
    private FragmentMediaPreviewBinding mBinding;
    private MediaAdapter mAdapter;
    private TagsAdapter mTagsAdapter;
    long downloadId;
    //TODO: Eliminate mMedia and replace with completely with mLocalMedia,
    // since all the attr you need in mMedia is also in mLocalMedia
    private MediaItem mMedia;
    private DownloadManager manager;
    private Uri Download_Uri;
    private Handler mHandler = new Handler();
    private boolean downloadable = true;
    private String mFilename;
    private Uri mFileUri;
    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


            Log.e("IN", "" + id);

            if (downloadId == id) {

                mBinding.setDownloading(false);
                mViewModel.updateLocalMediaUri(mFileUri.toString());
                Log.e("INSIDE", "" + id);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(requireContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(mMedia.getTitle())
                                .setContentText("Download completed");


                NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
            }

        }
    };
    private LocalMedia mLocalMedia;
    private boolean mStartMediaPlayer;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        requireActivity().registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MediaPreviewActivity) requireActivity()).setSupportActionBar(mBinding.previewMediaToolbar);
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
            try {
                mStartMediaPlayer = MediaPreviewFragmentArgs.fromBundle(getArguments()).getStartMediaPlayer();
                String mediaId = MediaPreviewFragmentArgs.fromBundle(getArguments()).getMediaId();
                String category = MediaPreviewFragmentArgs.fromBundle(getArguments()).getCategory();
                MediaPreviewViewModel.Factory viewModelFactory = new MediaPreviewViewModel.Factory(getActivity().getApplication(), mediaId, category);
                mViewModel = new ViewModelProvider(this, viewModelFactory).get(MediaPreviewViewModel.class);
                mViewModel.startMediaPlayerImmediately(mStartMediaPlayer);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "onActivityCreated: getNavigationArgs", e);
                mViewModel = new ViewModelProvider(requireActivity()).get(MediaPreviewViewModel.class);
            }
            mBinding.setViewModel(mViewModel);
            mBinding.setLifecycleOwner(this);
            mBinding.setGeneralClickListener(this);

        }

        mViewModel.getMediaItem().observe(getViewLifecycleOwner(), new Observer<MediaItem>() {
            boolean isInserted = false;

            @Override
            public void onChanged(MediaItem mediaItem) {
                mMedia = mediaItem;

                if (mediaItem != null && !isInserted) {
                    mViewModel.insertLocalMedia();
                    isInserted = true;

                    if (mViewModel.shouldStartMediaPlayer()) openMediaAction();
                }

                try {
                    mFilename = mViewModel.getMediaFileName();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    downloadable = false;
                    mFilename = "";
                }
                Toast.makeText(requireContext(), "FileName is: " + mFilename, Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getRelatedMedia().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItemList) {
                mAdapter.setMediaItems(mediaItemList);
                mBinding.moreRelatedMediaFlag.setOnClickListener(MediaPreviewFragment.this);
            }
        });

        mBinding.moreReviewFlag.setOnClickListener(this);

        mTagsAdapter = new TagsAdapter(MediaPreviewFragment.this);
        mBinding.tagsRv.setAdapter(mTagsAdapter);
        mBinding.tagsRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        mViewModel.getMediaItem().observe(getViewLifecycleOwner(), new Observer<MediaItem>() {
            @Override
            public void onChanged(MediaItem mediaItem) {
                if (mediaItem.getTags() != null && mediaItem.getTags().size() > 0)
                    mTagsAdapter.setTags(mediaItem.getTags());
            }
        });

        mViewModel.getLocalMedia().observe(getViewLifecycleOwner(), new Observer<LocalMedia>() {
            @Override
            public void onChanged(LocalMedia media) {
                mLocalMedia = media;
                if (media != null) mBinding.setDownloaded(media.isDownloaded());
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
                shareAction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMediaItemClick(String mediaId, String category) {
        NavHostFragment.findNavController(this)
                .navigate(MediaPreviewFragmentDirections.actionNavigationMediaPreviewSelf(mediaId, category));
    }

    private void showDialogFullscreen(DialogMediaPreview.Assignment purpose) {
        DialogMediaPreview newFragment = new DialogMediaPreview(this, purpose);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setCustomAnimations(R.anim.slide_in_right, android.R.anim.slide_out_right);
        transaction.add(mBinding.mediaPreviewRoot.getId(), newFragment).addToBackStack(null).commit();
    }

    private void shareAction() {
        // TODO: Write procedures to share the current mediaItem
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.setSystemBarColor(requireActivity());
    }

    @Override
    public void onTagClick(String tag) {

    }

    @Override
    public void onDialogResults(int requestCode, int resultCode) {
        if (resultCode == DialogMediaPreview.RESULT_CODE_SHARE) {
            shareAction();
        }
    }

    private void openMediaAction() {
        String media_url = mMedia == null ? " " : mMedia.getMedia_url();

        if (mLocalMedia == null) {
            mViewModel.insertLocalMedia();
        }

        //if (mLocalMedia.isDownloaded()) media_url = mViewModel.getLocalMedia().getValue().getMedia_uri();
        if (mMedia.getType() == MediaItem.MediaType.EBOOK) {
            NavHostFragment.findNavController(this)
                    .navigate(MediaPreviewFragmentDirections.actionNavigationMediaPreviewToEbookReaderFragment(media_url));
            //startActivity(new Intent(requireActivity(), PDFViewActivity_.class));
        } else NavHostFragment.findNavController(this)
                .navigate(MediaPreviewFragmentDirections.actionNavigationMediaPreviewToAudioPlayer(media_url));
    }

    private void downloadMedia() {
        DownloadManager downloadmanager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(mMedia.getMedia_url());

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(mMedia.getTitle());
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);
//        request.setDestinationUri(Uri.parse("file://" + mMedia.getType().name() + "/" + "myfile.mp3"));

        if (mMedia.getType() == MediaItem.MediaType.EBOOK)
            request.setDestinationUri(getCacheMediaPath(mMedia.getTitle() + ".pdf"));
        else {
            InputStream inputStream = null;
            try {
                inputStream = getContext().getContentResolver().openInputStream(getCacheMediaPath(mMedia.getTitle() + ".mp3"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            request.setDestinationUri(inputStream);
        }

        downloadmanager.enqueue(request);
    }

    private Uri getCacheMediaPath(String fileName) {
        File path = new File(requireActivity().getExternalCacheDir(), "media");
        if (!path.exists()) path.mkdirs();
        File mediaName = new File(path, fileName);
        return getUriForFile(requireActivity(), requireActivity().getPackageName() + ".provider", mediaName);
    }

    public void startDownload() {
        //<SET UP DOWNLOAD MANAGER HERE>

        if (!downloadable) return;
        Download_Uri = Uri.parse(mMedia.getMedia_url());

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Love Rekindle Downloading " + mMedia.getTitle());
        request.setDescription("Downloading " + mMedia.getTitle());
        request.setVisibleInDownloadsUi(false);
        //request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getPath(), "Love Rekindle/Media/"  + "/." + mMedia.getType().name()+ "/" + mFilename);
        String localPath = Environment.getExternalStoragePublicDirectory(APP_NAME) + "/Media" + "/." + mMedia.getType().name() + "/" + mFilename;
        final File mediaFile = new File(localPath);
        mFileUri = Uri.fromFile(mediaFile);
        if (mediaFile.exists()) {
            Toast.makeText(requireContext(), "File Already Exist!" + mLocalMedia.isDownloaded(), Toast.LENGTH_LONG).show();
            //This is temporary remove it before deployment inorder to avoid increasing the download count unnecessarily
            mViewModel.updateLocalMediaUri(mFileUri.toString());
            return;
        }
        request.setDestinationUri(mFileUri);

        if (!mBinding.getDownloading()) {
            downloadId = manager.enqueue(request);
            mBinding.setDownloading(true);
        }

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor cursor = manager.query(q);
                cursor.moveToFirst();
                int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                cursor.close();
                final float dl_progress = (bytesDownloaded * 1f / bytesTotal) * 100;
                Runnable updateProgressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mBinding.progressBarCircular.setIndeterminate(false);
                        mBinding.progressBarCircular.setProgress((int) dl_progress);
                    }
                };
                mHandler.postDelayed(updateProgressRunnable, 100);

            }
        }, 0, 10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_related_media_flag:
                showDialogFullscreen(DialogMediaPreview.Assignment.RELATED_MEDIA);
                break;
            case R.id.more_review_flag:
                showDialogFullscreen(DialogMediaPreview.Assignment.MORE_REVIEWS);
                break;
            case R.id.read_more_flag:
                showDialogFullscreen(DialogMediaPreview.Assignment.MORE_ABOUT);
                break;
            case R.id.write_your_review_txt:
                showDialogFullscreen(DialogMediaPreview.Assignment.EDIT_REVIEW);
                break;
            case R.id.btn_read_now:
                openMediaAction();
                break;
            case R.id.download:
                //downloadMedia();
                if (!PermissionCheck.readAndWriteExternalStorage(requireActivity())) {
                    Toast.makeText(requireContext(), "Permission not Granted", Toast.LENGTH_LONG).show();
                } else startDownload();
                break;
        }
    }

    @Override
    public void onDetach() {
        requireActivity().unregisterReceiver(onComplete);
        super.onDetach();
    }

}
