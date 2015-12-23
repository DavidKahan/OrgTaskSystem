package com.shenkar.orgtasksystem.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shenkar.orgtasksystem.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    public void login(View v){
        //TODO if Manager or regular user
        Intent intent = new Intent(LoginActivity.this, CreateEditTaskActivity.class);
        startActivity(intent);
    }
}
