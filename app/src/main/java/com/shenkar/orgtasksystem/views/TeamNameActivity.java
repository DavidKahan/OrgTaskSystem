package com.shenkar.orgtasksystem.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.shenkar.orgtasksystem.R;

public class TeamNameActivity extends AppCompatActivity {

    private EditText teamName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_name);

        this.teamName = (EditText) this.findViewById(R.id.teamName);
    }

    public void MoveToCreateTeam(View view) {
        Intent intent = new Intent(TeamNameActivity.this, CreateTeamActivity.class);
        intent.putExtra("TeamName", teamName.getText().toString());
        startActivity(intent);
    }
}
