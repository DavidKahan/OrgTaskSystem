package com.shenkar.orgtasksystem.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.controller.MVCController;
/**
 * Created by david on 17/12/2015.
 */
public class WaitingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
    MVCController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new MVCController(getActivity());
//        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecyclerAdapter(controller.loadWaitingTasks(),getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
