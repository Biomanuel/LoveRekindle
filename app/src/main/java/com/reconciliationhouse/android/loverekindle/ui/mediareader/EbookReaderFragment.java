package com.reconciliationhouse.android.loverekindle.ui.mediareader;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
//import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.data.LocalMedia;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentEbookReaderBinding;
import com.reconciliationhouse.android.loverekindle.ui.mediapreview.MediaPreviewViewModel;
import com.reconciliationhouse.android.loverekindle.utils.NetworkCheck;
import com.reconciliationhouse.android.loverekindle.utils.Tools;
import com.reconciliationhouse.android.loverekindle.utils.ViewAnimation;
import com.shockwave.pdfium.PdfDocument;

import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EbookReaderFragment extends Fragment implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener, OnTapListener, View.OnClickListener {

    private static final String TAG = EbookReaderFragment.class.getSimpleName();
    private static final long LOADING_DURATION = 1000;
    Handler mHandler = new Handler();
    private FragmentEbookReaderBinding mBinding;
    Runnable removeProgressBar = new Runnable() {
        @Override
        public void run() {
            ViewAnimation.fadeOut(mBinding.lytProgress);
            mBinding.pdfView.setBackgroundColor(Color.LTGRAY);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.pdfView.setVisibility(View.VISIBLE);
                }
            }, 400);
        }
    };
    private InputStream mInputStream;
    private int startPageNumber = 0;
    private String pdfFileName;
    private String downloadUrl;
    private MediaPreviewViewModel mViewModel;
    private boolean initialStage = true;
    Runnable displayPdfRunnable = new Runnable() {
        @Override
        public void run() {
            displayFromInputStream();
        }
    };
    private Handler progressBarHandler = new Handler();
    private boolean pageControlsVisible = true;
    Runnable TogglePageControls = new Runnable() {
        @Override
        public void run() {
            if (pageControlsVisible) {
                ViewAnimation.fadeOut(mBinding.pageControls);
                pageControlsVisible = false;
            } else {
                ViewAnimation.fadeIn(mBinding.pageControls);
                pageControlsVisible = true;
            }
            if (mBinding.pdfView.getCurrentPage() == mBinding.pdfView.getPageCount())
                ViewAnimation.fadeOut(mBinding.nextPage);
            else if (mBinding.pdfView.getCurrentPage() == 0)
                ViewAnimation.fadeOut(mBinding.previousPage);
        }
    };

    public EbookReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentEbookReaderBinding.inflate(inflater, container, false);

        //make inputStream ready
        //setUpInputStream();
        mBinding.setLifecycleOwner(this);


        if (getArguments() != null)
            downloadUrl = EbookReaderFragmentArgs.fromBundle(getArguments()).getMediaUrl();

//        initToolbar();
        initComponent();
        Tools.setSystemBarColor(requireActivity(), R.color.grey_90);
        mBinding.setPageControlsVisible(pageControlsVisible);
        return mBinding.getRoot();
    }

    private void initComponent() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getParentFragment() != null;
        try {
            mViewModel = new ViewModelProvider(getParentFragment()).get(MediaPreviewViewModel.class);
        } catch (RuntimeException e) {
            mViewModel = new ViewModelProvider(requireActivity()).get(MediaPreviewViewModel.class);
            Log.e(TAG, "onActivityCreated: Unable to create viewmodel form ParentFragment", e);
        } catch (Exception e) {
            mViewModel = new ViewModelProvider(requireActivity()).get(MediaPreviewViewModel.class);
            Log.e(TAG, "onActivityCreated: Unable to create viewmodel form ParentFragment", e);
        }
        //mBinding.setViewModel(mViewModel);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (initialStage) {
            mViewModel.getLocalMedia().observe(getViewLifecycleOwner(), new Observer<LocalMedia>() {
                @Override
                public void onChanged(LocalMedia localMedia) {
                    if (localMedia != null && initialStage) {
                        Toast.makeText(requireContext(), "Ready!", Toast.LENGTH_LONG).show();
                        mBinding.lytProgress.setVisibility(View.VISIBLE);
                        mBinding.lytProgress.setAlpha(1.0f);
                        mBinding.pdfView.setVisibility(View.INVISIBLE);
                        startPageNumber = localMedia.getCurrent_position();
                        if (localMedia.isDownloaded()) {
                            displayFromUri(Uri.parse(localMedia.getMedia_uri()));
                        } else {
                            setUpInputStream();
                            mHandler.postDelayed(displayPdfRunnable, 8000);
                        }
                    }
                }
            });
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void setUpInputStream() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                if (!NetworkCheck.isConnected(requireActivity())) {
                    Tools.displayActionSnackBar(requireActivity(), mBinding.getRoot(),
                            "Network Error", "Please Check Your Internet Connection", "Retry",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO: Write Procedures to reload pdf from the internet
                                    mHandler.postDelayed(displayPdfRunnable, 1000);
                                }
                            });
                }

                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Open Url Connection
                    connection.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    connection.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage());

                    }

                    mInputStream = connection.getInputStream();//Get InputStream for connection

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    protected void displayFromInputStream() {
        pdfFileName = "InputStream PDF";// TODO: Update this to actually get the file name

        if (mInputStream == null) {
            Toast.makeText(requireActivity(), "InputStream is null", Toast.LENGTH_LONG).show();
            setUpInputStream();
        }

        mBinding.pdfView.fromStream(mInputStream)
                .defaultPage(startPageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .pageSnap(true)
                .swipeHorizontal(true)
                .scrollHandle(new DefaultScrollHandle(requireContext()))
                .spacing(10) // in dp
                .onPageError(this)
                .enableSwipe(true)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
        initialStage = false;
    }

    private void displayFromUri(Uri uri) {
        pdfFileName = "PDF from URi"; //TODO: Update this to actually set it to Book Title

        mBinding.pdfView.fromUri(uri)
                .defaultPage(startPageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .pageSnap(true)
                .swipeHorizontal(true)
                .scrollHandle(new DefaultScrollHandle(requireContext()))
                .spacing(10) // in dp
                .onPageError(this)
                .enableSwipe(true)
                .onTap(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {

        PdfDocument.Meta meta = mBinding.pdfView.getDocumentMeta();

        //printBookmarksTree(mBinding.pdfView.getTableOfContents(), "-");
        progressBarHandler.postDelayed(removeProgressBar, 1000);
        initialStage = false;
        ViewAnimation.fadeOut(mBinding.pageControls);
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.saveCurrentPosition(mBinding.pdfView.getCurrentPage());
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    @Override
    public boolean onTap(MotionEvent e) {
        mHandler.postDelayed(TogglePageControls, 500);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.next_page:
                mBinding.pdfView.jumpTo(mBinding.pdfView.getCurrentPage() + 1);
                Toast.makeText(requireContext(), "Next", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(TogglePageControls, 500);
                break;
            case R.id.previous_page:
                mBinding.pdfView.jumpTo(Math.max(mBinding.pdfView.getCurrentPage() - 1, 0));
                Toast.makeText(requireContext(), "Previous", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(TogglePageControls, 500);
                break;
            case android.R.id.home:
                NavHostFragment.findNavController(this).navigateUp();
                break;
        }
    }
}
