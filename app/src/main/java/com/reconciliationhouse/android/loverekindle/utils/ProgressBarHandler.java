package com.reconciliationhouse.android.loverekindle.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ProgressBarHandler {
    private ProgressBar mProgressBar;
    private Context mContext;
    private visibilityListener mVisibilityListener;

    public ProgressBarHandler(Context context) {
        mContext = context;

        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();

        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        mProgressBar.setIndeterminate(true);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(context);

        rl.setGravity(Gravity.CENTER);
        rl.addView(mProgressBar);

        layout.addView(rl, params);

        hide();
    }

    public ProgressBarHandler(){}

    public ProgressBarHandler initialize(Context context) {
        mContext = context;

        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();

        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        mProgressBar.setIndeterminate(true);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(context);

        rl.setGravity(Gravity.CENTER);
        rl.addView(mProgressBar);

        layout.addView(rl, params);

        hide();

        return this;
    }

    public void show() {
        if(mContext == null) {
            try {
                throw new Throwable(new NullPointerException());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Log.e("ProgressBarHandler.", "show(): Initialize ProgressBarHandler with context first");
            }
        }else {
            mProgressBar.setVisibility(View.VISIBLE);
            if (mVisibilityListener != null) mVisibilityListener.onShow();
        }
    }

    public void hide() {
        if(mContext == null) {
            try {
                throw new Throwable(new NullPointerException());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Log.e("ProgressBarHandler.", "show(): Initialize ProgressBarHandler with context first");
            }
        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (mVisibilityListener != null) mVisibilityListener.onHide();
        }
    }

    public void setVisibilityListener(visibilityListener visibilityListener){
        this.mVisibilityListener = visibilityListener;
    }

    public interface visibilityListener {
        void onHide();
        void onShow();
    }
}
