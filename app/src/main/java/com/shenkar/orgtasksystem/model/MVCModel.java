package com.shenkar.orgtasksystem.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

//    private static final String DB_NAME = "ots_db";
//    private static final int DB_VERSION = 1;
//
//    private static final String MEMBERS_TABLE_NAME = "members";
//    private static final String DB_CREATE_QUERY_MEMBERS = "CREATE TABLE " + MVCModel.MEMBERS_TABLE_NAME + " (_id integer primary key autoincrement, email text not null, password text not null, type integer);";
//
//    private static final String TASKS_TABLE_NAME = "tasks";
//    private static final String DB_CREATE_QUERY_TASKS = "CREATE TABLE " + MVCModel.TASKS_TABLE_NAME + " (_id integer primary key autoincrement, description text not null, category text not null, priority text not null, assignedTeamMember text not null, dueDate text not null, dueTime text not null, longitude text not null, latitude text not null, status text not null);";
//
//    private final SQLiteDatabase database;
//
//    private final SQLiteOpenHelper helper;
//
//    public MVCModel(final Context ctx) {
//        this.helper = new SQLiteOpenHelper(ctx, MVCModel.DB_NAME, null, MVCModel.DB_VERSION) {
//            @Override
//            public void onCreate(final SQLiteDatabase db) {
//                db.execSQL(MVCModel.DB_CREATE_QUERY_TASKS);
//                db.execSQL(MVCModel.DB_CREATE_QUERY_MEMBERS);
//            }
//
//            @Override
//            public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
//                db.execSQL("DROP TABLE IF EXISTS " + MVCModel.MEMBERS_TABLE_NAME);
//                db.execSQL("DROP TABLE IF EXISTS " + MVCModel.TASKS_TABLE_NAME);
//                this.onCreate(db);
//            }
//        };
//        this.database = this.helper.getWritableDatabase();
//    }
//
//    public void deleteMember(final String field_params) {
//        this.database.delete(MVCModel.MEMBERS_TABLE_NAME, field_params, null);
//    }
//
//    public void deleteTask(final String field_params) {
//        this.database.delete(MVCModel.TASKS_TABLE_NAME, field_params, null);
//    }

    public void addMember(final Member member) {
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

    public void addTask(Task task) {
        //Send to Parse
        ParseObject parseTask = new ParseObject("MemberTask");
        parseTask.put("description", task.description);
        parseTask.put("category", task.category);
        parseTask.put("priority", task.priority);
        parseTask.put("assignedTeamMember", task.assignedTeamMember);
        parseTask.put("dueDate", task.dueDate);
        parseTask.put("dueTime", task.dueTime);
        parseTask.put("location", task.location);
        parseTask.put("status", task.status);
        parseTask.saveInBackground();
    }

    public List<Task> loadDoneTasks(String memberName) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "DONE");
        query.orderByAscending("dueDate");
        if (memberName != null) {
            query.whereEqualTo("assignedTeamMember", memberName);
        }
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
            myObject.location = object.getString("location");
            myObject.priority = object.getString("priority");
            doneTasks.add(myObject);
            Log.d("score", "Retrieved " + results.size() + " scores");
        }
        return doneTasks;
    }

    public List<Task> loadWaitingTasks(String memberName) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "WAITING");
        query.orderByAscending("dueDate");
        if (memberName != null) {
            query.whereEqualTo("assignedTeamMember", memberName);
        }
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
            myObject.location = object.getString("location");
            myObject.priority = object.getString("priority");
            waitingTasks.add(myObject);
            Log.d("score", "Retrieved " + results.size() + " scores");
        }
        return waitingTasks;
    }

    public List<Task> loadPendingTasks(String memberName) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "PENDING");
        query.orderByAscending("dueDate");
        if (memberName != null) {
            query.whereEqualTo("assignedTeamMember", memberName);
        }
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
            myObject.location = object.getString("location");
            myObject.priority = object.getString("priority");
            pendingTasks.add(myObject);
            Log.d("score", "Retrieved " + results.size() + " scores");
        }
        return pendingTasks;
    }

    public List<String> loadAllMembers() throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        List<ParseUser> results = query.find();
        for (ParseUser member : results){
            String myMemberName = member.getString("username");
            members.add(myMemberName);
            Log.d("members", "Retrieved " + results.size() + " members");
        }
        return members;
    }

    public void createTeamName(String mTeamName) {
        ParseObject parseTeam = new ParseObject("TeamName");
        parseTeam.put("name", mTeamName);
        parseTeam.saveInBackground();
    }

    public void updateTaskStatusByID(final Task currentTask) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        // Retrieve the object by id
        query.getInBackground(currentTask.id , new GetCallback<ParseObject>() {
            public void done(ParseObject task, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data.
                    task.put("status", currentTask.status);
                    task.saveInBackground();
                }
            }
        });
    }
}
