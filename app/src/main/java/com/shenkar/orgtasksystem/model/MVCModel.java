package com.shenkar.orgtasksystem.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David on 11/23/2015.
 */
public class MVCModel {
    private List<String> members    = new ArrayList<String>();
    private List<Task> doneTasks    = new ArrayList<Task>();
    private List<Task> waitingTasks = new ArrayList<Task>();
    private List<Task> pendingTasks = new ArrayList<Task>();
    private Bitmap     bmp                              ;

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
        try {
            user.signUp();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    ParseUser.logInInBackground(member.name, member.password, new LogInCallback() {
//                        @Override
//                        public void done(ParseUser parseUser, ParseException e) {
//                            ParseUser.becomeInBackground(session, new LogInCallback() {
//                                @Override
//                                public void done(ParseUser parseUser, ParseException e) {
//                                    if (parseUser != null) {
//                                        // The current user is now set to user.
//                                    } else {
//                                        // The token could not be validated.
//                                    }
//                                }
//                            });
//                        }
//                    });
//
//                } else {
//                    // Sighup failed. Look at the ParseException to see what happened.
//                }
//            }
//        });
    }

    public void addTask(Task task) throws ParseException {
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
        parseTask.save();
    }

    public List<Task> loadDoneTasks(String memberEmail) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "DONE");
        query.orderByAscending("dueDate");
        if (memberEmail != null) {
            query.whereEqualTo("assignedTeamMember", memberEmail);
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

    public List<Task> loadWaitingTasks(String memberEmail) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "WAITING");
        query.orderByAscending("dueDate");
        if (memberEmail != null) {
            query.whereEqualTo("assignedTeamMember", memberEmail);
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
        }
        return waitingTasks;
    }

    public List<Task> loadPendingTasks(String memberEmail) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "PENDING");
        query.orderByAscending("dueDate");
        if (memberEmail != null) {
            query.whereEqualTo("assignedTeamMember", memberEmail);
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
            String mMemberEmail = member.getString("email");
            members.add(mMemberEmail);
            Log.d("members", "Retrieved " + results.size() + " members");
        }
        return members;
    }

    public void createTeamName(String mTeamName) {
        ParseObject parseTeam = new ParseObject("TeamName");
        parseTeam.put("name", mTeamName);
        parseTeam.saveInBackground();
    }

    public String getTeamName() throws ParseException{
        String name = "";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TeamName");
        List<ParseObject> results = query.find();
        for (ParseObject teamName : results) {
            name = teamName.getString("name");
        }
        return name;
    }

    public String getManagerName() throws ParseException {
        final String[] manager = {""};
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("type","0");
        query.find();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
//                    ParseUser u = (ParseUser)objects.get(0);
//                    String name = u.getUsername();
                    manager[0] = objects.get(0).getUsername();
                } else {
                    // Something went wrong.
                }
            }
        });
        return manager[0];
    }

    public void updateTaskStatusByID(final Task currentTask) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        // Retrieve the object by id
        ParseObject task = query.get(currentTask.id);
        task.put("status", currentTask.status);
        task.save();
//        query.getInBackground(currentTask.id, new GetCallback<ParseObject>() {
//            public void done(ParseObject task, ParseException e) {
//                if (e == null) {
//                    // Now let's update it with some new data.
//
//                    try {
//                        task.save();
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    public void uploadImage(String id, byte[] image) throws ParseException {
        // Create the ParseFile
        ParseFile file = new ParseFile(id+".png", image);
        // Upload the image into Parse Cloud
        file.save();
        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject("ImageUpload");
        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", id);
        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);
        // Create the class and the columns
        imgupload.save();
    }

    public Bitmap loadImageById(String id) throws ParseException {
        // Locate the class table named "ImageUpload" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>( "ImageUpload");
        query.whereEqualTo("ImageName",id);
        List<ParseObject> results = null;
        results = query.find();
        for (ParseObject object : results) {
            ParseFile fileObject = (ParseFile) object.get("ImageFile");
            byte[] data = fileObject.getData();
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//            fileObject.getDataInBackground(new GetDataCallback() {
//                public void done(byte[] data, ParseException e) {
//                    if (e == null) {
//                        Log.d("test", "We've got data in data.");
//                        // Decode the Byte[] into
//                        // Bitmap
//                        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    } else {
//                        Log.d("test",
//                                "There was a problem downloading the data.");
//                    }
//                }
//            });
        }


        return bmp;
    }
}
