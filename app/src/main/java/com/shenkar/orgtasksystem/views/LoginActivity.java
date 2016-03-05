package com.shenkar.orgtasksystem.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.CountCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.controller.MVCController;

public class LoginActivity extends Activity {

    private EditText memberName,memberEmail,memberPass ;
    private MVCController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.controller = new MVCController(this);
        this.memberName = (EditText) this.findViewById(R.id.memberName);
        this.memberEmail = (EditText) this.findViewById(R.id.memberEmail);
        this.memberPass = (EditText) this.findViewById(R.id.memberPass);


    }

    public void login(View v){
        final String name, password, email;
        name = memberName.getText().toString();
        password = memberPass.getText().toString();
        email = memberEmail.getText().toString();

        //Check if User exists meaning he's not admin
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {
                    if (count == 0) {
                        //Username doesn't exists -> he's our new Manager
                        ParseUser user = new ParseUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.put("type", "0");
                        //Sign him up
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    //Log him in
                                    ParseUser.logInInBackground(name, password, new LogInCallback() {
                                        public void done(ParseUser user, ParseException e) {
                                            if (user != null) {
                                                //The user is logged in.
                                                Intent intent = new Intent(LoginActivity.this, TeamNameActivity.class);
                                                startActivity(intent);
                                            } else {
                                                // Login failed. Look at the ParseException to see what happened.
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        ParseUser.logInInBackground(memberName.getText().toString(), memberPass.getText().toString(), new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    try {
                                        showMemberToast();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    startActivity(intent);
                                } else {
                                    // Login failed. Look at the ParseException to see what happened.
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void showMemberToast() throws ParseException {
        String teamName = controller.getTeamName();
//        String managerName = controller.getManagerName();
        CharSequence text = "You have been added to Team:"+teamName;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }
}
