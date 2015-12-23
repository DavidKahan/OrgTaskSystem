package com.shenkar.orgtasksystem.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenkar.orgtasksystem.presenter.ITaskController;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.presenter.MVCController;
//import com.shenkar.orgtasksystem.presenter.TaskController;

/**
 * Created by david on 17/12/2015.
 */
public class AllTasksFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    MVCController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new MVCController(getActivity());
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        // specify an adapter (see also next example)
        //mAdapter = new RecyclerAdapter(controller.GetTasks());
        mAdapter = new RecyclerAdapter(controller.loadAllTasks(),getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
