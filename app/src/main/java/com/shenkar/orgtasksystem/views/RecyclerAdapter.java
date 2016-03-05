package com.shenkar.orgtasksystem.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseUser;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.model.Task;
import java.util.List;
/**
 * Created by david on 27/10/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.MyViewHolder>{

    private List<Task> tasks;
    private Activity activity;

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
        if (current.isNew.equals("1")){
            holder.taskView.setBackgroundColor(Color.rgb(244,33,21));
        }
        holder.description.setText(current.description);
        holder.memberEmail.setText(current.assignedTeamMember);
        holder.dueDate.setText(current.dueDate);
        holder.taskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUserType = Integer.parseInt(ParseUser.getCurrentUser().getString("type"));
                if (currentUserType == 0 && current.status.equals("WAITING")){
                    Intent intent = new Intent(activity,CreateEditTaskActivity.class);
                    intent.putExtra("CurrentTask", current);
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity,ReportTaskStatusActivity.class);
                    intent.putExtra("CurrentTask", current);
                    activity.startActivity(intent);
                }
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

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            taskView = itemView;
            description = (TextView) itemView.findViewById(R.id.taskDescription);
            memberEmail= (TextView) itemView.findViewById(R.id.taskAssignedMember);
            dueDate = (TextView) itemView.findViewById(R.id.taskDueDate);
        }
    }
}
