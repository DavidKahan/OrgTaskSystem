//package com.shenkar.orgtasksystem.views;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.shenkar.orgtasksystem.presenter.ITaskController;
//import com.shenkar.orgtasksystem.R;
//import com.shenkar.orgtasksystem.presenter.TaskController;
//
//
//public class TaskListActivity extends AppCompatActivity {
//
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//    ITaskController controller;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_list);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
//        controller = new TaskController();
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter (see also next example)
//        mAdapter = new RecyclerAdapter(controller.GetTasks());
//        mRecyclerView.setAdapter(mAdapter);
//
//    }
//
//    public void moveToAdd(View view){
//        Intent intent = null;
//        intent = new Intent(this,CreateTaskActivity.class);
//        startActivity(intent);
//    }
//
//}
