package com.shenkar.orgtasksystem.views;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.model.Task;
import com.shenkar.orgtasksystem.presenter.MVCController;

import java.io.Serializable;

public class CreateEditTaskActivity extends AppCompatActivity {

    private MVCController controller;
    Task currentTask = new Task();
    TextView tvDate, tvTime;
    EditText assignedTeamMemberEmail, taskDescription;
    RadioButton radioCategory, radioPriority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.tvDate = (TextView) findViewById(R.id.tvDate);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.assignedTeamMemberEmail = (EditText) findViewById(R.id.assignedTeamMemberEmail);
        this.taskDescription = (EditText) findViewById(R.id.taskDescription);
        this.radioCategory = (RadioButton) findViewById(R.id.radio_general);
        this.radioCategory.setChecked(true);
        this.currentTask.category = "general";

        this.radioPriority = (RadioButton) findViewById(R.id.radio_normal);
        this.radioPriority.setChecked(true);
        this.currentTask.priority = "normal";

        this.controller = new MVCController(this);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void onCategoryRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_cleaning:
                if (checked)
                    this.currentTask.category = "cleaning";
                break;
            case R.id.radio_computers:
                if (checked)
                    this.currentTask.category = "computers";
                break;
            case R.id.radio_electricity:
                if (checked)
                    this.currentTask.category = "electricity";
                break;
            case R.id.radio_general:
                if (checked)
                    this.currentTask.category = "general";
                break;
        }
    }

    public void onPriorityRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_low:
                if (checked)
                    this.currentTask.priority = "low";
                    break;
            case R.id.radio_normal:
                if (checked)
                    this.currentTask.priority = "normal";
                    break;
            case R.id.radio_urgent:
                if (checked)
                    this.currentTask.priority = "urgent";
                    break;
        }
    }

    public void save(View view) {
        //TODO: error handling
        this.currentTask.dueDate = tvDate.getText().toString();
        this.currentTask.dueTime = tvTime.getText().toString();
        this.currentTask.assignedTeamMember = assignedTeamMemberEmail.getText().toString();
        this.currentTask.description = taskDescription.getText().toString();

        this.currentTask.longitude = "234235";
        this.currentTask.latitude = "234235";
        this.currentTask.status = "WAITING";

        Intent intent = new Intent(CreateEditTaskActivity.this,MainActivity.class);
        intent.putExtra("CurrentTask", currentTask);
        startActivity(intent);
    }
}
