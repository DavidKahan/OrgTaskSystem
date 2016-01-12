package com.shenkar.orgtasksystem.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.controller.MVCController;
import com.shenkar.orgtasksystem.model.Task;

public class ReportTaskStatusActivity extends AppCompatActivity {

    private MVCController controller;
    public Task currentTask;
    public Intent intent;
    TextView taskLocation, taskCategory, taskPriority, taskTime, taskDate;
    Spinner statusSpinner;
    String statusLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.controller = new MVCController(this);
        this.taskLocation = (TextView) findViewById(R.id.reportTaskLocation);
        this.taskCategory = (TextView) findViewById(R.id.taskCategory);
        this.taskPriority = (TextView) findViewById(R.id.taskPriority);
        this.taskTime = (TextView) findViewById(R.id.taskTime);
        this.taskDate = (TextView) findViewById(R.id.taskDate);
        this.statusSpinner = (Spinner) findViewById(R.id.task_status_spinner);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,R.array.task_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        intent = getIntent();
        if (intent != null) {
            this.currentTask = new Task();
            this.currentTask = (Task) intent.getSerializableExtra("CurrentTask");
            taskLocation.setText(currentTask.location);
            taskCategory.setText(currentTask.category);
            taskPriority.setText(currentTask.priority);
            taskDate.setText(currentTask.dueDate);
            taskTime.setText(currentTask.dueTime);
            if (currentTask.status.equals("DONE")) {
                statusSpinner.setSelection(statusAdapter.getPosition("DONE"));
            } else if (currentTask.status.equals("PENDING")) {
                statusSpinner.setSelection(statusAdapter.getPosition("PENDING"));
            }
        }



        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusLabel = parent.getItemAtPosition(position).toString();
                currentTask.status = statusLabel;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
                // Retrieve the object by id
                query.getInBackground(currentTask.id , new GetCallback<ParseObject>() {
                    String tmp = currentTask.id;
                    public void done(ParseObject task, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data.
                            task.put("status", statusLabel);
                            task.saveInBackground();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        Spinner acceptSpinner = (Spinner) findViewById(R.id.accept_status_spinner);
        ArrayAdapter<CharSequence> acceptAdapter = ArrayAdapter.createFromResource(this,R.array.accept_status_array, android.R.layout.simple_spinner_item);
        acceptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acceptSpinner.setAdapter(acceptAdapter);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void saveStatus(View view){
        //update task status in DB
        this.controller.updateTaskStatusByID(currentTask);
        //return to MainActivity
        Intent intent = new Intent(ReportTaskStatusActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
