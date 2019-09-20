package com.martdev.android.devjobs;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.martdev.android.devjobs.adapter.DevJobAdapter;
import com.martdev.android.devjobs.loader.DevJobsLoader;

import java.util.List;

public class DevJobResultActivity extends AppCompatActivity
        implements LoaderCallbacks<List<DevJob>>, DevJobAdapter.OnItemClickHandler {
    private static final String URL = "https://jobs.github.com/position?";
    private static final String KEYWORD = "com.martdev.android.devjobs.keyword";

    private static final int DEVJOB_LOADER_ID = 1;

    public static Intent newIntent(Context packageContext, String keyword) {
        Intent intent = new Intent(packageContext, DevJobResultActivity.class);
        intent.putExtra(KEYWORD, keyword);
        return intent;
    }

    private DevJobAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mNoDataMessage;

    private String mGitHubJobUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devjobs_result);

        String keyword = getIntent().getStringExtra(KEYWORD);

        mRecyclerView = findViewById(R.id.devjob_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRefreshLayout = findViewById(R.id.swipe);

        mNoDataMessage = findViewById(R.id.no_data_message);

        updateView();
        checkNetwork(keyword);

    }

    private void updateView() {
        mAdapter = new DevJobAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void checkNetwork(String keyword) {
        mGitHubJobUrl = getUrl(keyword);

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getSupportLoaderManager();
            if (loaderManager.getLoader(DEVJOB_LOADER_ID) == null) {
                loaderManager.initLoader(DEVJOB_LOADER_ID, null, DevJobResultActivity.this);
            } else {
                loaderManager.restartLoader(DEVJOB_LOADER_ID, null, DevJobResultActivity.this);
            }
        } else {
            mRefreshLayout.setRefreshing(false);

            mNoDataMessage.setText(R.string.no_internet_message);
        }
    }

    private String getUrl(String keyword) {
        return "https://jobs.github.com/positions.json?description=" + keyword;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.keyword_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        query(searchView);

        return super.onCreateOptionsMenu(menu);
    }

    private void query(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!(query == null)){
                    checkNetwork(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<DevJob>> onCreateLoader(int id, @Nullable Bundle args) {
        return new DevJobsLoader(this, mGitHubJobUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<DevJob>> loader, List<DevJob> data) {
        mRefreshLayout.setRefreshing(false);

        if (data != null && !data.isEmpty()) {
            mAdapter.setDevJobList(data);
        } else {
            mNoDataMessage.setText(R.string.no_job_available_msg);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<DevJob>> loader) {
    }

    @Override
    public void clickItem(DevJob devJob) {
        Uri uri = Uri.parse(devJob.getUrl());
        Intent intent = DevJobWebPage.newIntent(DevJobResultActivity.this, uri);
        startActivity(intent);
    }
}
