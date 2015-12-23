package com.shenkar.orgtasksystem.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.shenkar.orgtasksystem.R;

public class ReportTaskStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner acceptSpinner = (Spinner) findViewById(R.id.accept_status_spinner);
        ArrayAdapter<CharSequence> acceptAdapter = ArrayAdapter.createFromResource(this,R.array.accept_status_array, android.R.layout.simple_spinner_item);
        acceptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acceptSpinner.setAdapter(acceptAdapter);

        Spinner statusSpinner = (Spinner) findViewById(R.id.task_status_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,R.array.task_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
