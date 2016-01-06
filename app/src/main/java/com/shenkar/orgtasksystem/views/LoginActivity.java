package com.shenkar.orgtasksystem.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shenkar.orgtasksystem.R;

import java.util.List;

public class LoginActivity extends Activity {

    private EditText memberName,memberEmail,memberPass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.memberName = (EditText) this.findViewById(R.id.memberName);
        this.memberEmail = (EditText) this.findViewById(R.id.memberEmail);
        this.memberPass = (EditText) this.findViewById(R.id.memberPass);

        Parse.initialize(this, "irUzywS69MpTb5AXjCsamv3vUr7Oh39xzkZJzkto", "rTbKss08HgSA1zrhmSCVPPsn989IlaJsmK1OWRNY");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    public void login(View v){
        //TODO if Manager or regular user\ if user exists in Parse or not
        final String name, password, email;
        name = memberName.getText().toString();
        password = memberPass.getText().toString();
        email = memberEmail.getText().toString();

        //Check if User exists meaning he's not admin
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    if (count == 0) {
                        //Username doesn't exists -> he's the Manager
                        ParseUser user = new ParseUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);
                        // other fields can be set just like with ParseObject
                        //user.put("phone", "650-253-0000");

                        //Sign him up
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    //Log him in
                                    ParseUser.logInInBackground(memberName.getText().toString(), memberPass.getText().toString(), new LogInCallback() {
                                        public void done(ParseUser user, ParseException e) {
                                            if (user != null) {
                                                //The user is logged in.
                                                Intent intent = new Intent(LoginActivity.this, CreateTeamActivity.class);
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
                        //user is regular team member and is already signed up
                        ParseUser user = new ParseUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);

                        // other fields can be set just like with ParseObject
                        //user.put("phone", "650-253-0000");

                        //Log him in
                        ParseUser.logInInBackground(memberName.getText().toString(), memberPass.getText().toString(), new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    //The user is logged in.
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
}
