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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_name);

        this.controller = new MVCController(this);
        this.teamName = (EditText) this.findViewById(R.id.teamName);
    }

    public void MoveToCreateTeam(View view) {
        String mTeamName =  teamName.getText().toString();
        this.controller.createTeamName(mTeamName);
        Intent intent = new Intent(TeamNameActivity.this, AddMembersActivity.class);
        //intent.putExtra("TeamName", teamName.getText().toString());
        startActivity(intent);
    }
}
