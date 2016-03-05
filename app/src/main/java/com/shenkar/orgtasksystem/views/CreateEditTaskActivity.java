package com.shenkar.orgtasksystem.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.model.Task;
import com.shenkar.orgtasksystem.controller.MVCController;

import java.util.Calendar;
import java.util.List;

public class CreateEditTaskActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    public Intent intent;
    private MVCController controller;
    ArrayAdapter<String> membersAdapter;
    Task currentTask = new Task();
    TextView tvDate, tvTime;
    EditText taskDescription;
    RadioButton radioCategoryGeneral, radioCategoryCleaning, radioCategoryElectricity, radioCategoryComputers, radioPriorityNormal, radioPriorityUrgent, radioPriorityLow;
    Spinner memberSpinner , locationSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.controller = new MVCController(this);
        this.locationSpinner = (Spinner) findViewById(R.id.task_location_spinner);
        this.memberSpinner = (Spinner) findViewById(R.id.task_member_spinner);
        this.tvDate = (TextView) findViewById(R.id.tvDate);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.taskDescription = (EditText) findViewById(R.id.taskDescription);

        this.radioCategoryCleaning = (RadioButton) findViewById(R.id.radio_cleaning);
        this.radioCategoryElectricity = (RadioButton) findViewById(R.id.radio_electricity);
        this.radioCategoryComputers = (RadioButton) findViewById(R.id.radio_computers);
        this.radioCategoryGeneral = (RadioButton) findViewById(R.id.radio_general);
        this.radioCategoryGeneral.setChecked(true);
        this.currentTask.category = "general";

        this.radioPriorityUrgent = (RadioButton) findViewById(R.id.radio_urgent);
        this.radioPriorityLow = (RadioButton) findViewById(R.id.radio_low);
        this.radioPriorityNormal = (RadioButton) findViewById(R.id.radio_normal);
        this.radioPriorityNormal.setChecked(true);
        this.currentTask.priority = "normal";

        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,R.array.task_location_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);


        try {
            loadTaskMemberSpinnerData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        intent = getIntent();
        if (intent.hasExtra("CurrentTask")) {
            currentTask = new Task();
            currentTask = (Task) intent.getSerializableExtra("CurrentTask");
            //description
            taskDescription.setText(currentTask.description);
            tvTime.setText(currentTask.dueTime);
            tvDate.setText(currentTask.dueDate);
            //category
            if (currentTask.category.equals("cleaning")) {
                this.radioCategoryCleaning.setChecked(true);
                this.currentTask.category = "cleaning";
            } else if (currentTask.category.equals("electricity")) {
                this.radioCategoryElectricity.setChecked(true);
                this.currentTask.category = "electricity";
            } else if (currentTask.category.equals("computers")) {
                this.radioCategoryComputers.setChecked(true);
                this.currentTask.category = "computers";
            }
            //priority
            if (currentTask.priority.equals("urgent")) {
                this.radioPriorityUrgent.setChecked(true);
                this.currentTask.priority = "urgent";
            } else if (currentTask.priority.equals("low")){
                    this.radioPriorityLow.setChecked(true);
                    this.currentTask.priority = "low";
            }
            locationSpinner.setSelection(locationAdapter.getPosition(currentTask.location.toString()));
            memberSpinner.setSelection(membersAdapter.getPosition(currentTask.assignedTeamMember.toString()));
        }

    }

    private void loadTaskMemberSpinnerData() throws ParseException {
        List<String> labels = this.controller.getMembers();
         membersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);
        membersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        memberSpinner.setAdapter(membersAdapter);
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.task_member_spinner){
            String label = parent.getItemAtPosition(position).toString();
            this.currentTask.assignedTeamMember = label;
        }
        else if(spinner.getId() == R.id.task_location_spinner){
            String label = parent.getItemAtPosition(position).toString();
            this.currentTask.location = label;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void setForToday(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH)+1;
        tvDate.setText(day+"/"+month+"/"+year);
    }

    public void setForTomorrow(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH)+1;
        tvDate.setText(day+"/"+month+"/"+year);
    }

    public void save(View view) throws ParseException {
        this.controller.deleteTask(currentTask);
        this.currentTask.isNew = "1";
        this.currentTask.dueDate = tvDate.getText().toString();
        this.currentTask.dueTime = tvTime.getText().toString();
        this.currentTask.assignedTeamMember = memberSpinner.getSelectedItem().toString();
        this.currentTask.description = taskDescription.getText().toString();
        this.currentTask.location = locationSpinner.getSelectedItem().toString();
        this.currentTask.acceptStatus = "WAITING";
        this.currentTask.status = "WAITING";
        try {
            this.controller.addTask(this.currentTask);
            Toast.makeText(this,"New Task created and sent", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateEditTaskActivity.this,MainActivity.class);
            startActivity(intent);
        } catch (ParseException e) {
            Toast.makeText(this,"Error:"+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
