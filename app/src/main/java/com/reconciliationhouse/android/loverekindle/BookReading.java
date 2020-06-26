package com.reconciliationhouse.android.loverekindle;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
//import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;

import java.io.File;
import java.util.Objects;


public class BookReading extends AppCompatActivity {

    PDFView pdfView;
    Toolbar toolbar;
    TextView title;
    MediaItem mMediaItem;
    String uid;
    int currentpage;
//    LibraryMaterialDatabase libraryMaterialDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reading);

        initView();
        displayEbook();

    }

    private void initView() {
//        libraryMaterialDatabase = Room.databaseBuilder(this, LibraryMaterialDatabase.class, "LibraryMaterialDatabase").allowMainThreadQueries().build();
        uid = getIntent().getStringExtra("id");
        String bookname = getIntent().getStringExtra("bookname");
        currentpage = getIntent().getIntExtra("currentpage", 0);

        title = findViewById(R.id.reading_page_title);
        toolbar = findViewById(R.id.reading_toolbar);

        title.setText(bookname);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void displayEbook() {
        String ebookuri = getIntent().getStringExtra("uri");
        Log.e("uri ", ebookuri);
        pdfView = findViewById(R.id.pdfview);
        File file = new File(ebookuri);


        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(currentpage)
                .spacing(10)
                //.pageSnap(true)
                //.pageFling(true)
                .scrollHandle(null)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
//                        libraryMaterialDatabase.LibraryMaterialEntryDaoAccess().updateBook(pageCount,page,uid);
                    }
                })

                //.pageFitPolicy(FitPolicy.BOTH)
                .scrollHandle(null)
                .load();
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid", android.os.Process.myPid());

    }
}
