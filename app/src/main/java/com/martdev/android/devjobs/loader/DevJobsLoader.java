package com.martdev.android.devjobs.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.martdev.android.devjobs.DevJob;
import com.martdev.android.devjobs.utils.QueryUtils;

import java.util.List;

public class DevJobsLoader extends AsyncTaskLoader<List<DevJob>> {

    private String mURL;

    public DevJobsLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<DevJob> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        return QueryUtils.getJobsData(mURL);
    }
}
