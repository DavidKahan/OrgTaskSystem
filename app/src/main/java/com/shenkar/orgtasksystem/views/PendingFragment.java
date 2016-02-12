package com.shenkar.orgtasksystem.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenkar.orgtasksystem.R;

/**
 * Created by david on 29/12/2015.
 */
public class PendingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayout mTasksFrame;
    private TextView mNoTasksTxt, mNumTasks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerList);
        mTasksFrame = (LinearLayout) view.findViewById(R.id.tasks_frame);
        mNoTasksTxt = (TextView) view.findViewById(R.id.no_tasks);
        mNumTasks = (TextView) view.findViewById(R.id.num_tasks);
        MainActivity activity = (MainActivity) getActivity();
        int tasksNum = activity.mPendingAdapter.getItemCount();
        if ( tasksNum > 0 ) {
            mNoTasksTxt.setVisibility(View.GONE);
            mTasksFrame.setVisibility(View.VISIBLE);
            mNumTasks.setText(Integer.toString(tasksNum));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            mRecyclerView.setAdapter(activity.mPendingAdapter);
        }
        return view;
    }
}
