package com.shenkar.orgtasksystem.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.controller.MVCController;
import com.shenkar.orgtasksystem.model.Task;

public class ReportTaskStatusActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private MVCController controller;
    public Task currentTask;
    public Intent intent;
    private TextView taskLocation, taskCategory, taskPriority, taskTime, taskDate;
    private Spinner acceptSpinner, statusSpinner;
    private String acceptStatusLabel, statusLabel;
    private Button photoButton;
    private ImageView camPic;
    private Bitmap bmp;
    private LinearLayout acceptStatusLay;

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
        this.acceptStatusLay = (LinearLayout) findViewById(R.id.accept_status_lay);
        this.acceptStatusLay.setVisibility(View.INVISIBLE);
        this.acceptSpinner = (Spinner) findViewById(R.id.accept_status_spinner);
        this.statusSpinner = (Spinner) findViewById(R.id.task_status_spinner);
        this.photoButton = (Button) findViewById(R.id.photoButton);
        this.photoButton.setVisibility(View.INVISIBLE);
        this.camPic = (ImageView) findViewById(R.id.camPic);

        ArrayAdapter<CharSequence> acceptAdapter = ArrayAdapter.createFromResource(this,R.array.accept_status_array, android.R.layout.simple_spinner_item);
        acceptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acceptSpinner.setAdapter(acceptAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,R.array.task_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        intent = getIntent();
        if (intent != null) {
            this.currentTask = new Task();
            this.currentTask = (Task) intent.getSerializableExtra("CurrentTask");
            //The task is no longer new
            currentTask.isNew = "0";
            try {
                controller.updateTaskStatusByID(currentTask);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            taskLocation.setText(currentTask.location);
            taskCategory.setText(currentTask.category);
            taskPriority.setText(currentTask.priority);
            taskDate.setText(currentTask.dueDate);
            taskTime.setText(currentTask.dueTime);

            if (currentTask.acceptStatus.equals("ACCEPT")){
                acceptSpinner.setSelection(acceptAdapter.getPosition("ACCEPT"));
                acceptStatusLay.setVisibility(View.VISIBLE);
            } else if (currentTask.acceptStatus.equals("REJECT")){
                acceptSpinner.setSelection(acceptAdapter.getPosition("REJECT"));
                acceptStatusLay.setVisibility(View.INVISIBLE);
            }

            if (currentTask.status.equals("DONE")) {
                statusSpinner.setSelection(statusAdapter.getPosition("DONE"));
                try {
                    bmp = this.controller.loadImageById(currentTask.parseID);
                    camPic.setImageBitmap(bmp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (currentTask.status.equals("PENDING")) {
                statusSpinner.setSelection(statusAdapter.getPosition("PENDING"));
            }
        }

        acceptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                acceptStatusLabel = parent.getItemAtPosition(position).toString();
                currentTask.acceptStatus = acceptStatusLabel;
                if (acceptStatusLabel.equals("ACCEPT")) {
                    acceptStatusLay.setVisibility(View.VISIBLE);
                } else {
                    acceptStatusLay.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusLabel = parent.getItemAtPosition(position).toString();
                currentTask.status = statusLabel;
                if (statusLabel.equals("DONE")) {
                    photoButton.setVisibility(View.VISIBLE);
                    camPic.setVisibility(View.VISIBLE);

                } else {
                    photoButton.setVisibility(View.INVISIBLE);
//                    camPic.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            camPic.setImageBitmap(imageBitmap);
            Toast.makeText(ReportTaskStatusActivity.this, "Image Uploaded",Toast.LENGTH_SHORT).show();
            try {
                this.controller.uploadImage(currentTask.parseID, imageBitmap);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveStatus(View view){
        try {
            currentTask.isNew = "0";
            this.controller.updateTaskStatusByID(currentTask);
            if (currentTask.acceptStatus.equals("REJECT")){
                Toast.makeText(this, "Task Rejected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportTaskStatusActivity.this,MainActivity.class);
                startActivity(intent);
            } else if (currentTask.acceptStatus.equals("ACCEPT")){
                Toast.makeText(this,"Task accepted and task status is "+currentTask.status, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportTaskStatusActivity.this,MainActivity.class);
                startActivity(intent);
            } else{
                Intent intent = new Intent(ReportTaskStatusActivity.this,MainActivity.class);
                startActivity(intent);
            }

        } catch (ParseException e) {
            Toast.makeText(this,"Error Saving Task Status: Please try again"+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
