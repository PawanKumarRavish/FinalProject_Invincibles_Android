package com.project.taskmanager.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import com.project.taskmanager.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends WalletActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    @BindView(R.id.pdfView)
    PDFView mPdfView;

    public static final String SAMPLE_FILE = "privacy_policy.pdf";
    String pdfFileName;

    Integer pageNumber = 0;

    @BindView(R.id.back_arrow)
    ImageView mBackArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

        mPdfView.setBackgroundColor(Color.LTGRAY);
        displayFromAsset(SAMPLE_FILE);

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        mPdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = mPdfView.getDocumentMeta();
        Log.e("THIS", "title = " + meta.getTitle());
        Log.e("THIS", "author = " + meta.getAuthor());
        Log.e("THIS", "subject = " + meta.getSubject());
        Log.e("THIS", "keywords = " + meta.getKeywords());
        Log.e("THIS", "creator = " + meta.getCreator());
        Log.e("THIS", "producer = " + meta.getProducer());
        Log.e("THIS", "creationDate = " + meta.getCreationDate());
        Log.e("THIS", "modDate = " + meta.getModDate());

        printBookmarksTree(mPdfView.getTableOfContents(), "-");


    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("This", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e("Error", "Cannot load page " + page);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
