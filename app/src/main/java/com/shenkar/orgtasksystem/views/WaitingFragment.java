package com.shenkar.orgtasksystem.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.controller.MVCController;
/**
 * Created by david on 17/12/2015.
 */
public class WaitingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RelativeLayout mTasksFrame;
    private TextView mNoTasksTxt, mNumTasks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerList);
        mTasksFrame = (RelativeLayout) view.findViewById(R.id.tasks_frame);
        mNoTasksTxt = (TextView) view.findViewById(R.id.no_tasks);
        mNumTasks = (TextView) view.findViewById(R.id.num_tasks);

        MainActivity activity = (MainActivity) getActivity();
        int tasksNum = activity.mWaitingAdapter.getItemCount();
        //Check if there are tasks to display
        if ( tasksNum > 0 ) {
            mNoTasksTxt.setVisibility(View.GONE);
            mTasksFrame.setVisibility(View.VISIBLE);
            mNumTasks.setText(Integer.toString(tasksNum));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            mRecyclerView.setAdapter(activity.mWaitingAdapter);
            mRecyclerView.invalidate();
        }
        return view;
    }

}
