package com.shenkar.orgtasksystem.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shenkar.orgtasksystem.views.CreateTeamActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David on 11/23/2015.
 */
public class MVCModel {
    private List<String> members = new ArrayList<String>();
    private List<Task> doneTasks = new ArrayList<Task>();
    private List<Task> waitingTasks = new ArrayList<Task>();
    private List<Task> pendingTasks = new ArrayList<Task>();

    private static final String DB_NAME = "ots_db";
    private static final int DB_VERSION = 1;

    private static final String MEMBERS_TABLE_NAME = "members";
    private static final String DB_CREATE_QUERY_MEMBERS = "CREATE TABLE " + MVCModel.MEMBERS_TABLE_NAME + " (_id integer primary key autoincrement, email text not null, password text not null, type integer);";

    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String DB_CREATE_QUERY_TASKS = "CREATE TABLE " + MVCModel.TASKS_TABLE_NAME + " (_id integer primary key autoincrement, description text not null, category text not null, priority text not null, assignedTeamMember text not null, dueDate text not null, dueTime text not null, longitude text not null, latitude text not null, status text not null);";

    private final SQLiteDatabase database;

    private final SQLiteOpenHelper helper;

    public MVCModel(final Context ctx) {
        this.helper = new SQLiteOpenHelper(ctx, MVCModel.DB_NAME, null, MVCModel.DB_VERSION) {
            @Override
            public void onCreate(final SQLiteDatabase db) {
                db.execSQL(MVCModel.DB_CREATE_QUERY_TASKS);
                db.execSQL(MVCModel.DB_CREATE_QUERY_MEMBERS);
            }

            @Override
            public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + MVCModel.MEMBERS_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + MVCModel.TASKS_TABLE_NAME);
                this.onCreate(db);
            }
        };
        this.database = this.helper.getWritableDatabase();
    }

    public void deleteMember(final String field_params) {
        this.database.delete(MVCModel.MEMBERS_TABLE_NAME, field_params, null);
    }

    public void addMember(ContentValues data, final Member member) {
        this.database.insert(MVCModel.MEMBERS_TABLE_NAME, null, data);

        //Create new team member
        ParseUser user = new ParseUser();
        user.setUsername(member.name);
        user.setPassword(member.password);
        user.setEmail(member.email);
        user.put("type", String.valueOf(member.type));

        //Save current parse user session
        final String session = ParseUser.getCurrentUser().getSessionToken();

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser.logInInBackground(member.name, member.password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            ParseUser.becomeInBackground(session, new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (parseUser != null) {
                                        // The current user is now set to user.
                                    } else {
                                        // The token could not be validated.
                                    }
                                }
                            });
                        }
                    });

                } else {
                    // Sighup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    public void addTask(ContentValues data, Task task) {
        //Insert to local
        this.database.insert(MVCModel.TASKS_TABLE_NAME, null, data);

        //Send to Parse
        ParseObject parseTask = new ParseObject("MemberTask");
        parseTask.put("description", task.description);
        parseTask.put("category", task.category);
        parseTask.put("priority", task.priority);
        parseTask.put("assignedTeamMember", task.assignedTeamMember);
        parseTask.put("dueDate", task.dueDate);
        parseTask.put("dueTime", task.dueTime);
        parseTask.put("longitude", task.longitude);
        parseTask.put("latitude", task.latitude);
        parseTask.put("status", task.status);
        parseTask.saveInBackground();
    }

    public List<Task> loadDoneTasks() throws ParseException {
        final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority", "_id", "status"}, "status=?", new String[]{"DONE"}, null, null, "dueDate ASC");
//        return c;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "DONE");
        List<ParseObject> results = query.find();
        for (ParseObject object : results){
            Task myObject = new Task();
            myObject.id = object.getObjectId();
            myObject.description = object.getString("description");
            myObject.assignedTeamMember = object.getString("assignedTeamMember");
            myObject.dueTime = object.getString("dueTime");
            myObject.status = object.getString("status");
            myObject.category = object.getString("category");
            myObject.dueDate = object.getString("dueDate");
            myObject.latitude= object.getString("latitude");
            myObject.longitude = object.getString("longitude");
            myObject.priority = object.getString("priority");
            doneTasks.add(myObject);
            Log.d("score", "Retrieved " + results.size() + " scores");
        }
        return doneTasks;
    }

    public List<Task> loadWaitingTasks() throws ParseException {
        //final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority","_id", "status" }, "status=?", new String[] { "WAITING" }, null, null, "dueDate ASC");
        //return c;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "WAITING");
        List<ParseObject> results = query.find();
        for (ParseObject object : results){
            Task myObject = new Task();
            myObject.id = object.getObjectId();
            myObject.description = object.getString("description");
            myObject.assignedTeamMember = object.getString("assignedTeamMember");
            myObject.dueTime = object.getString("dueTime");
            myObject.status = object.getString("status");
            myObject.category = object.getString("category");
            myObject.dueDate = object.getString("dueDate");
            myObject.latitude= object.getString("latitude");
            myObject.longitude = object.getString("longitude");
            myObject.priority = object.getString("priority");
            waitingTasks.add(myObject);
            Log.d("score", "Retrieved " + results.size() + " scores");
        }
        return waitingTasks;
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> taskList, ParseException e) {
//                if (e == null) {
//                    for (int i = 0; i < taskList.size(); i++) {
//                        Task myObject = new Task();
//                        ParseObject object = taskList.get(i);
//                        myObject.id = object.getObjectId();
//                        myObject.description = (String) object.get("description");
//                        myObject.assignedTeamMember = (String) object.get("assignedTeamMember");
//                        myObject.dueTime = (String) object.get("dueTime");
//                        myObject.status = (String) object.get("status");
//                        myObject.category = (String) object.get("category");
//                        myObject.dueDate = (String) object.get("dueDate");
//                        myObject.latitude= (String) object.get("latitude");
//                        myObject.longitude = (String) object.get("longitude");
//                        myObject.priority = (String) object.get("priority");
//                        waitingTasks.add(myObject);
//                        Log.d("score", "Retrieved " + taskList.size() + " scores");
//                    }
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
//
//        return waitingTasks;
    }

    public List<Task> loadPendingTasks() throws ParseException {
        //final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority","_id", "status" }, "status=?", new String[] { "PENDING" }, null, null, "dueDate ASC");
        //return c;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "PENDING");
        List<ParseObject> results = query.find();
        for (ParseObject object : results){
            Task myObject = new Task();
            myObject.id = object.getObjectId();
            myObject.description = object.getString("description");
            myObject.assignedTeamMember = object.getString("assignedTeamMember");
            myObject.dueTime = object.getString("dueTime");
            myObject.status = object.getString("status");
            myObject.category = object.getString("category");
            myObject.dueDate = object.getString("dueDate");
            myObject.latitude= object.getString("latitude");
            myObject.longitude = object.getString("longitude");
            myObject.priority = object.getString("priority");
            pendingTasks.add(myObject);
            Log.d("score", "Retrieved " + results.size() + " scores");
        }
        return pendingTasks;
    }

    public List<String> loadAllMembers() throws ParseException {
//        final Cursor c = this.database.query(MVCModel.MEMBERS_TABLE_NAME, new String[]{"email"}, null, null, null, null, null);
//        return c;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        List<ParseUser> results = query.find();
        for (ParseUser member : results){
            String myMemberName = member.getString("username");
            members.add(myMemberName);
            Log.d("members", "Retrieved " + results.size() + " members");
        }
        return members;
    }

    public void deleteTask(final String field_params) {
        this.database.delete(MVCModel.TASKS_TABLE_NAME, field_params, null);
    }

    public void updateTaskStatusByID(ContentValues data, String id) {
        this.database.update(MVCModel.TASKS_TABLE_NAME, data, "_id=?", new String[] { id});
    }
}
