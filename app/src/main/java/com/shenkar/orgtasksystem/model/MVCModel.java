package com.shenkar.orgtasksystem.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by David on 11/23/2015.
 */
public class MVCModel {
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
    public Cursor loadAllMembers() {
        final Cursor c = this.database.query(MVCModel.MEMBERS_TABLE_NAME, new String[]{"email"}, null, null, null, null, null);
        return c;
    }
    public void addMember(ContentValues data) {
        this.database.insert(MVCModel.MEMBERS_TABLE_NAME, null, data);
    }
    public void addTask(ContentValues data) {
        this.database.insert(MVCModel.TASKS_TABLE_NAME, null, data);
    }

    public Cursor loadDoneTasks() {
        final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority","_id", "status" }, "status=?", new String[] { "DONE" }, null, null, "dueDate ASC");
        return c;
    }
    public Cursor loadWaitingTasks() {
        final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority","_id", "status" }, "status=?", new String[] { "WAITING" }, null, null, "dueDate ASC");
        return c;
    }
    public Cursor loadPendingTasks() {
        final Cursor c = this.database.query(MVCModel.TASKS_TABLE_NAME, new String[]{"description", "assignedTeamMember", "dueDate", "dueTime", "category", "priority","_id", "status" }, "status=?", new String[] { "PENDING" }, null, null, "dueDate ASC");
        return c;
    }

    public void deleteTask(final String field_params) {
        this.database.delete(MVCModel.TASKS_TABLE_NAME, field_params, null);
    }
    public void updateTaskStatusByID(ContentValues data, String id) {
        this.database.update(MVCModel.TASKS_TABLE_NAME, data, "_id=?", new String[] { id});
    }
}
