package com.shenkar.orgtasksystem.views;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.model.Task;
import com.shenkar.orgtasksystem.presenter.MVCController;

import java.util.List;


/**
 * Created by david on 27/10/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.MyViewHolder>{

    //private LayoutInflater inflater;
    private List<String> tasks;
    private Activity activity;
    private MVCController controller;

    public RecyclerAdapter(List<String> tasks) {
        this.tasks = tasks;
    }

    public RecyclerAdapter(List<String> tasks, Activity activity) {
        this.tasks = tasks;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String current = tasks.get(position);
        holder.task.setText(current);
        holder.taskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO if Manager or User
                Intent intent = new Intent(activity,CreateEditTaskActivity.class);
                //intent.putExtra("CurrentTask", currentTask);
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView task;
        View taskView;
        //Button done;

        public MyViewHolder(View itemView) {
            super(itemView);
            taskView = itemView;
            task = (TextView) itemView.findViewById(R.id.task);
            //done = (Button) itemView.findViewById(R.id.done_button);
        }
    }
}
