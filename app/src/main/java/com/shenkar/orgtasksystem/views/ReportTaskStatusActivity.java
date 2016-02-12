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
    TextView taskLocation, taskCategory, taskPriority, taskTime, taskDate;
    Spinner statusSpinner;
    String statusLabel;
    Button photoButton;
    ImageView camPic;
    Bitmap bmp;

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
        this.photoButton = (Button) findViewById(R.id.photoButton);
        this.photoButton.setVisibility(View.INVISIBLE);
        this.camPic = (ImageView) findViewById(R.id.camPic);



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
                try {
                    bmp = this.controller.loadImageById(currentTask.id);
                    camPic.setImageBitmap(bmp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (currentTask.status.equals("PENDING")) {
                statusSpinner.setSelection(statusAdapter.getPosition("PENDING"));
            }
        }
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusLabel = parent.getItemAtPosition(position).toString();
                currentTask.status = statusLabel;
                if (statusLabel.equals("DONE")){
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
        //region accept status spinner
        Spinner acceptSpinner = (Spinner) findViewById(R.id.accept_status_spinner);
        ArrayAdapter<CharSequence> acceptAdapter = ArrayAdapter.createFromResource(this,R.array.accept_status_array, android.R.layout.simple_spinner_item);
        acceptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acceptSpinner.setAdapter(acceptAdapter);
        //endregion
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                this.controller.uploadImage(currentTask.id, imageBitmap);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveStatus(View view) throws ParseException {
        this.controller.updateTaskStatusByID(currentTask);
        Intent intent = new Intent(ReportTaskStatusActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
