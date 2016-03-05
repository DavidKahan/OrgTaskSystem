package com.shenkar.orgtasksystem.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.model.Member;
import com.shenkar.orgtasksystem.controller.MVCController;

import java.util.ArrayList;
import java.util.List;

public class AddMembersActivity extends AppCompatActivity {
    private static final int TYPE_MANAGER = 0;
    private static final int TYPE_TEAM_MEMBER = 1;
    static final int EMAIL_REQUEST = 1;
    private EditText memberName, memberEmail, memberPass ;
    private Button btNewMem;
    private ListView lvEmails;
    private MVCController controller;
    private List<String> members;
    Member addedMember = new Member();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.memberName = (EditText) this.findViewById(R.id.memberName);
        this.memberEmail = (EditText) this.findViewById(R.id.memberEmail);
        this.memberPass = (EditText) this.findViewById(R.id.memberPass);
        this.btNewMem = (Button) this.findViewById(R.id.btNewMem);
        this.lvEmails = (ListView) this.findViewById(R.id.lvEmails);
        this.controller = new MVCController(this);

        try {
            this.populateEmails();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // region Email Fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Create the Intent */
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, members.toArray(new String[]{}));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi\n" +
                        "\tYou have been invited to be a team member in an OTS Team created by me.\n" +
                        "\tUse this link to download and install the App from Google Play.\n" +
                        "\t<LINK to Google Play download>\n");

                /* Send it off to the Activity-Chooser */
                startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), EMAIL_REQUEST);
                //onActivityResult(EMAIL_REQUEST,1,Intent.createChooser(emailIntent, "Send mail..."));
                //startActivity(Intent.createChooser(emailIntent, "Send mail..."));


            }
        });
        // endregion
    }

    private void populateEmails() throws ParseException {
        if (members != null) members.clear();
        members = this.controller.getMembers();
        this.lvEmails.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, members.toArray(new String[]{})));
//        this.lvEmails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
//                    final TextView v = (TextView) view;
//                    AddMembersActivity.this.controller.deleteMember(v.getText().toString());
//                try {
//                    AddMembersActivity.this.populateEmails();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            });
    }

    public void handleNewMember(View view){
        this.addedMember.name = memberName.getText().toString();
        this.addedMember.email = memberEmail.getText().toString();
        this.addedMember.password = memberPass.getText().toString();
        this.addedMember.type = TYPE_TEAM_MEMBER ;
        try {
            AddMembersActivity.this.controller.addMember(this.addedMember);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            AddMembersActivity.this.populateEmails();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.controller = new MVCController(this);
        memberName.setText("");
        memberEmail.setText("");
        memberPass.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: move to MainActivity
        Intent intent = new Intent(AddMembersActivity.this, CreateEditTaskActivity.class);
        intent.putExtra("type", TYPE_MANAGER);
        startActivity(intent);
//        // Check which request we're responding to
//        if (requestCode == EMAIL_REQUEST) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//
//            }
//        }
    }
}
