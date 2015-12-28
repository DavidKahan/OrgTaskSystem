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
import com.shenkar.orgtasksystem.controller.MVCController;

import java.util.List;


/**
 * Created by david on 27/10/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.MyViewHolder>{

    //private LayoutInflater inflater;
    private List<Task> tasks;
    private Activity activity;
    private MVCController controller;
//    private Task current;

    public RecyclerAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public RecyclerAdapter(List<Task> tasks, Activity activity) {
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

        final Task current = tasks.get(position);
        holder.description.setText(current.description);
        holder.memberEmail.setText(current.assignedTeamMember);
        holder.dueDate.setText(current.dueDate);
        holder.taskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO if Manager or User
                Intent intent = new Intent(activity,ReportTaskStatusActivity.class);
                intent.putExtra("CurrentTask", current);
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView description, memberEmail, dueDate;
        View taskView;
        //Button done;

        public MyViewHolder(View itemView) {
            super(itemView);
            taskView = itemView;
            description = (TextView) itemView.findViewById(R.id.taskDescription);
            memberEmail= (TextView) itemView.findViewById(R.id.taskAssignedMember);
            dueDate = (TextView) itemView.findViewById(R.id.taskDueDate);
            //done = (Button) itemView.findViewById(R.id.done_button);
        }
    }
}
