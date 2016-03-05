package com.shenkar.orgtasksystem.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shenkar.orgtasksystem.R;
import com.shenkar.orgtasksystem.views.MainActivity;
import com.shenkar.orgtasksystem.views.NotificationActivity;
import com.shenkar.orgtasksystem.views.ReportTaskStatusActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 11/23/2015.
 */
public class MVCModel {
    private Context context;
    private List<String> members = new ArrayList<String>();
    private List<Task> doneTasks = new ArrayList<Task>();
    private List<Task> waitingTasks = new ArrayList<Task>();
    private List<Task> pendingTasks = new ArrayList<Task>();
    private Task onlyNewTask = new Task();
    private int newTasksCounter = 0;
    private Bitmap bmp;
    private static final String DB_NAME = "ots_db";
    private static final int DB_VERSION = 1;

//    private static final String MEMBERS_TABLE_NAME = "members";
//    private static final String DB_CREATE_QUERY_MEMBERS = "CREATE TABLE " + MVCModel.MEMBERS_TABLE_NAME + " (_id integer primary key autoincrement, email text not null, password text not null, type integer);";

    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String DB_CREATE_QUERY_TASKS = "CREATE TABLE " + MVCModel.TASKS_TABLE_NAME + " (_id integer primary key autoincrement, parseID text, isNew text, description text not null, category text not null, priority text not null, assignedTeamMember text not null, dueDate text, dueTime text, location text not null, acceptStatus text not null, status text not null);";

    private final SQLiteDatabase database;
    private final SQLiteOpenHelper helper;

    public MVCModel(final Context ctx) {
        this.context = ctx;
        this.helper = new SQLiteOpenHelper(ctx, MVCModel.DB_NAME, null, MVCModel.DB_VERSION) {
            @Override
            public void onCreate(final SQLiteDatabase db) {
                db.execSQL(MVCModel.DB_CREATE_QUERY_TASKS);
//                db.execSQL(MVCModel.DB_CREATE_QUERY_MEMBERS);
            }

            @Override
            public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
//                db.execSQL("DROP TABLE IF EXISTS " + MVCModel.MEMBERS_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + MVCModel.TASKS_TABLE_NAME);
                this.onCreate(db);
            }
        };
        this.database = this.helper.getWritableDatabase();
    }
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
        //        creates errors for some reason
        try {
            ParseUser.become(session);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        ParseUser.becomeInBackground(session, new LogInCallback() {
//            @Override
//            public void done(ParseUser parseUser, ParseException e) {
//                if (parseUser != null) {
//                    // The current user is now set to user.
//                } else {
//                    // The token could not be validated.
//                }
//            }
//        });
    }

    public void addTask(Task task) throws ParseException {
        //Save Locally
        final ContentValues data = new ContentValues();
        data.put("isNew", task.isNew);
        data.put("description", task.description);
        data.put("category", task.category);
        data.put("priority", task.priority);
        data.put("assignedTeamMember", task.assignedTeamMember);
        data.put("dueDate", task.dueDate);
        data.put("dueTime", task.dueTime);
        data.put("location", task.location);
        data.put("acceptStatus", task.acceptStatus);
        data.put("status", task.status);
        this.database.insert(MVCModel.TASKS_TABLE_NAME, null, data);
        //Send to Parse
        ParseObject parseTask = new ParseObject("MemberTask");
        parseTask.put("isNew", task.isNew);
        parseTask.put("description", task.description);
        parseTask.put("category", task.category);
        parseTask.put("priority", task.priority);
        parseTask.put("assignedTeamMember", task.assignedTeamMember);
        parseTask.put("dueDate", task.dueDate);
        parseTask.put("dueTime", task.dueTime);
        parseTask.put("location", task.location);
        parseTask.put("acceptStatus", task.acceptStatus);
        parseTask.put("status", task.status);
        parseTask.save();
    }

    public void deleteTask(Task task) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("objectId", task.parseID);
        List<ParseObject> results = query.find();
        for (ParseObject object : results) {
            object.delete();
        }
    }

    public List<Task> loadWaitingTasksFromParse(String memberEmail) throws ParseException {
        this.newTasksCounter = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "WAITING");
        query.orderByAscending("dueDate");
        if (memberEmail != null) {
            query.whereEqualTo("assignedTeamMember", memberEmail);
        }
        List<ParseObject> results = query.find();
        for (ParseObject object : results) {
            Task myObject = new Task();
            myObject.parseID = object.getObjectId();
            myObject.isNew = object.getString("isNew");
            myObject.description = object.getString("description");
            myObject.assignedTeamMember = object.getString("assignedTeamMember");
            myObject.dueTime = object.getString("dueTime");
            myObject.acceptStatus = object.getString("acceptStatus");
            myObject.status = object.getString("status");
            myObject.category = object.getString("category");
            myObject.dueDate = object.getString("dueDate");
            myObject.location = object.getString("location");
            myObject.priority = object.getString("priority");
            if (myObject.isNew.equals("1")) {
                newTasksCounter++;
                onlyNewTask = myObject;
            }
            waitingTasks.add(myObject);
        }
        String currentUserType = ParseUser.getCurrentUser().getString("type");
        if (newTasksCounter == 1 && !currentUserType.equals("0")) {
            //notification for one
            Intent resultIntent = new Intent(context, ReportTaskStatusActivity.class);
            resultIntent.putExtra("CurrentTask", onlyNewTask);
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(ReportTaskStatusActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
            PendingIntent dismissIntent = NotificationActivity.getDismissIntent(1, context);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_drawer)
                            .setContentTitle("New Task!")
                            .setContentText("You have received a new Task!")
                            .addAction(R.drawable.ic_delete_black, "cancel", dismissIntent)
                            .addAction(R.drawable.ic_done_black, "view", resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());

        } else if (newTasksCounter > 1 && !currentUserType.equals("0")) {
            //notification for more than one.
            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            PendingIntent dismissIntent = NotificationActivity.getDismissIntent(2, context);
//            mBuilder.setContentIntent(resultPendingIntent);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_drawer)
                            .setContentTitle("New Tasks!")
                            .setContentText("You have received some new Tasks")
                            .addAction(R.drawable.ic_delete_black, "cancel", dismissIntent)
                            .addAction(R.drawable.ic_done_black, "view", resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(2, mBuilder.build());
        }
        return waitingTasks;
    }

    public List<Task> loadPendingTasksFromParse(String memberEmail) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "PENDING");
        query.orderByAscending("dueDate");
        if (memberEmail != null) {
            query.whereEqualTo("assignedTeamMember", memberEmail);
        }
        List<ParseObject> results = query.find();
        for (ParseObject object : results) {
            Task myObject = new Task();
            myObject.parseID = object.getObjectId();
            myObject.isNew = object.getString("isNew");
            myObject.description = object.getString("description");
            myObject.assignedTeamMember = object.getString("assignedTeamMember");
            myObject.dueTime = object.getString("dueTime");
            myObject.acceptStatus = object.getString("acceptStatus");
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

    public List<Task> loadDoneTasksFromParse(String memberEmail) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberTask");
        query.whereEqualTo("status", "DONE");
        query.orderByAscending("dueDate");
        if (memberEmail != null) {
            query.whereEqualTo("assignedTeamMember", memberEmail);
        }
        List<ParseObject> results = query.find();
        for (ParseObject object : results) {
            Task myObject = new Task();
            myObject.parseID = object.getObjectId();
            myObject.isNew = object.getString("isNew");
            myObject.description = object.getString("description");
            myObject.assignedTeamMember = object.getString("assignedTeamMember");
            myObject.dueTime = object.getString("dueTime");
            myObject.acceptStatus = object.getString("acceptStatus");
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

    public Cursor loadDoneTasks() {
        final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority", "parseID", "status"}, "status=?", new String[]{"DONE"}, null, null, "dueDate ASC");
        return c;
    }

    public Cursor loadWaitingTasks() {
        final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority", "_id", "status"}, "status=?", new String[]{"WAITING"}, null, null, "dueDate ASC");
        return c;
    }

    public Cursor loadPendingTasks() {
        final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority", "_id", "status"}, "status=?", new String[]{"PENDING"}, null, null, "dueDate ASC");
        return c;
    }

    public List<String> loadAllMembers() throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        List<ParseUser> results = query.find();
        for (ParseUser member : results) {
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

    public String getTeamName() throws ParseException {
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
        query.whereEqualTo("type", "0");
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
        ParseObject task = query.get(currentTask.parseID);
        task.put("isNew", currentTask.isNew);
        task.put("acceptStatus", currentTask.acceptStatus);
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
        ParseFile file = new ParseFile(id + ".png", image);
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
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ImageUpload");
        query.whereEqualTo("ImageName", id);
        List<ParseObject> results = null;
        results = query.find();
        for (ParseObject object : results) {
            ParseFile fileObject = (ParseFile) object.get("ImageFile");
            byte[] data = fileObject.getData();
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        return bmp;
    }
}
