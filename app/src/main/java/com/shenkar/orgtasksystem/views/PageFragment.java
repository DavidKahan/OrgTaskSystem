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

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MVCController controller;
    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerList);
        controller = new MVCController(getActivity());
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(controller.loadAllTasks(),getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
        return view;
    }


//    private void populateWaitingTasks() {
//        members = this.controller.getMembers();
//
//        this.lvEmails.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, members.toArray(new String[]{})));
//        this.lvEmails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
//                final TextView v = (TextView) view;
//                CreateTeamActivity.this.controller.deleteMember(v.getText().toString());
//                CreateTeamActivity.this.populateEmails();
//            }
//        });
//    }
}