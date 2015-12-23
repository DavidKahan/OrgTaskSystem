//package com.shenkar.orgtasksystem.views;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.EditText;
//
////import com.shenkar.orgtasksystem.presenter.ITaskController;
//import com.shenkar.orgtasksystem.R;
//import com.shenkar.orgtasksystem.presenter.MVCController;
////import com.shenkar.orgtasksystem.presenter.TaskController;
//
//public class CreateTaskActivity extends AppCompatActivity {
//
//    private EditText task;
////    public ITaskController controller = new TaskController();
//    public MVCController controller = new MVCController(this);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_task);
//        task = (EditText)findViewById(R.id.edit_message);
//    }
//
//
//    public void addTask(View view){
//        String newTask = task.getText().toString();
//        controller.addTask(newTask);
//        Intent intent = new Intent(this,TaskListActivity.class);
//        startActivity(intent);
//    }
//
//
//}
