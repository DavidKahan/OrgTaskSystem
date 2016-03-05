package com.shenkar.orgtasksystem.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.parse.Parse;
import com.parse.ParseUser;
import com.shenkar.orgtasksystem.R;

public class SplashActivity extends Activity {
    ProgressBar pb;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pb = (ProgressBar) findViewById(R.id.progressBar);
//        ParseUser.getCurrentUser()
        //Move it to SplashActivity at the end
        Parse.initialize(this, "irUzywS69MpTb5AXjCsamv3vUr7Oh39xzkZJzkto", "rTbKss08HgSA1zrhmSCVPPsn989IlaJsmK1OWRNY");
        Thread tLoader = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (i <= 100){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(i);
                            }
                        });
                        Thread.sleep(50);
                        i++;
                    }
                    Thread.sleep(3000);
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tLoader.start();
    }
}
