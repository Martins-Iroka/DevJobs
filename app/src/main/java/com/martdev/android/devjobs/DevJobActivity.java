package com.martdev.android.devjobs;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DevJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText search_job_view = findViewById(R.id.search_job_view);

        FloatingActionButton fab = findViewById(R.id.search_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_keyword = search_job_view.getText().toString();
                if (search_keyword.isEmpty()) {
                    Snackbar.make(v, "No keyword entered", Snackbar.LENGTH_LONG).show();
                } else {
                    Intent intent = DevJobResultActivity.newIntent(DevJobActivity.this, search_keyword);
                    startActivity(intent);
                }
            }
        });

    }
}
