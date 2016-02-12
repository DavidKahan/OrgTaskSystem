package com.shenkar.orgtasksystem.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.controller.MVCController;

public class TeamNameActivity extends AppCompatActivity {
    private MVCController controller;
    private EditText teamName;
    public Intent intent;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_name);

        intent = getIntent();
        if (intent.hasExtra("username")) {
            userType = intent.getIntExtra("type",0);
        }

        this.controller = new MVCController(this);
        this.teamName = (EditText) this.findViewById(R.id.teamName);
    }

    public void MoveToCreateTeam(View view) {
        String mTeamName =  teamName.getText().toString();
        this.controller.createTeamName(mTeamName);
        Intent intent = new Intent(TeamNameActivity.this, AddMembersActivity.class);
        intent.putExtra("type", userType );
        startActivity(intent);
    }
}
