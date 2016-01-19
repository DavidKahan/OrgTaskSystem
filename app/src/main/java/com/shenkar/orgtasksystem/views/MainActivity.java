package com.shenkar.orgtasksystem.views;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.model.Task;
import com.shenkar.orgtasksystem.controller.MVCController;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {
    private MVCController controller;
    public Intent intent;
    private MenuItem mSyncMenuItem = null;
    private SwitchCompat loggedSwitch;
    private TextView tvSync;
    private ListView mDrawerList;
    private List<String> members;
    private String memberName;
    public RecyclerView.Adapter mWaitingAdapter,mPendingAdapter,mDoneAdapter;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvSync = (TextView) findViewById(R.id.syncSettings);

        intent = getIntent();
        if (intent.hasExtra("username")) {
            memberName = intent.getStringExtra("username");
        } else {
            memberName = null;
        }

        setUpdatingData();

        //set sync time to 5 minutes as default
        myTimer = new Timer();
        TimerTask sync =new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        };
        myTimer.schedule(sync,0,1000*60*5);

        //region Pager and Tabs
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //endregion

        //region fab and drawer
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateEditTaskActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        try {
            members = this.controller.getMembers();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, members.toArray(new String[]{})));


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        loggedSwitch = (SwitchCompat) findViewById(R.id.loggedSwitch);
        loggedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        //endregion
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);
        mSyncMenuItem = menu.findItem(R.id.action_sync);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            setUpdatingData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpdatingData() {
        startSyncAnimation();
        this.controller = new MVCController(this);
        try {
            mWaitingAdapter = new RecyclerAdapter(controller.loadWaitingTasks(memberName),this);
            mPendingAdapter = new RecyclerAdapter(controller.loadPendingTasks(memberName),this);
            mDoneAdapter = new RecyclerAdapter(controller.loadDoneTasks(memberName),this);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        stopSyncAnimation();
    }

    public void stopSyncAnimation() {
        if (mSyncMenuItem != null && mSyncMenuItem.getActionView() != null) {
            mSyncMenuItem.getActionView().clearAnimation();
            mSyncMenuItem.setActionView(null);
        }
    }

    private void startSyncAnimation() {
        CharSequence text = "Searching for new Tasks..";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
        if (mSyncMenuItem != null && mSyncMenuItem.getActionView() == null) {
            ImageView syncImageView = new ImageView(this);
            int dp = 12;
            int padding_in_px = (int) (dp * Resources.getSystem().getDisplayMetrics().density);
            syncImageView.setPadding(padding_in_px, 0, padding_in_px, 0);
            syncImageView.setImageDrawable(mSyncMenuItem.getIcon());
            syncImageView.setScaleType(ImageView.ScaleType.CENTER);
            mSyncMenuItem.setActionView(syncImageView);

            Animation syncAnimation = AnimationUtils.loadAnimation(this, R.anim.sync_animation);
            //syncAnimation.setDuration(3000);
            syncAnimation.setRepeatCount(Animation.INFINITE);
            syncImageView.startAnimation(syncAnimation);


        }

    }

    public void moveToAddMember(View view) {
        Intent intent = new Intent(MainActivity.this, AddMembersActivity.class);
        startActivity(intent);
    }

    public void syncSettingsDialog(View view) {
        MyAlert alert = new MyAlert();
        alert.show(getFragmentManager(), "My Alert");
    }

    public void setSyncMin(int min){
        tvSync.setText("Check for new tasks every <"+min+"> minutes");
        myTimer.cancel();
        myTimer = new Timer();
        TimerTask sync =new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        };
        myTimer.schedule(sync,0,1000*60*min);
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            setUpdatingData();
        }
    };
}
