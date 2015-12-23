package com.shenkar.orgtasksystem.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.shenkar.orgtasksystem.model.MVCModel;
import com.shenkar.orgtasksystem.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 11/23/2015.
 */

public class MVCController {
    private MVCModel model;
    private List<String> members;
    private List<String> allTasks;
    private List<String> waitingTasks;

//    public MVCController(Context app_context, List<String> members) {
//        this.members = members;
//        allTasks = new ArrayList<String>();
//        waitingTasks = new ArrayList<String>();
//        model = new MVCModel(app_context);
//    }

    public MVCController(Context app_context) {
        members = new ArrayList<String>();
        allTasks = new ArrayList<String>();
        waitingTasks = new ArrayList<String>();
        model = new MVCModel(app_context);
    }

    public void deleteTask(final long id) {
        model.deleteMember("id='" + id + "'");
    }

    public void deleteAllMembers() {
        model.deleteMember(null);
    }

    public List<String> getMembers() {
        Cursor c = model.loadAllMembers();
        members.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                members.add(c.getString(0));
                c.moveToNext();
            }
            c.close();
        }
        return members;
    }

    public List<String> loadAllTasks() {
        Cursor c = model.loadAllTasks();
        allTasks.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                allTasks.add(c.getString(0)+"  "+c.getString(1)+"  "+c.getString(2));
                c.moveToNext();
            }
            c.close();
        }
        return allTasks;
    }

    public List<String> loadWaitingTasks() {
        Cursor c = model.loadWaitingTasks();
        waitingTasks.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                waitingTasks.add(c.getString(0));
                c.moveToNext();
            }
            c.close();
        }
        return waitingTasks;
    }


    public void addMember(String Email) {
        final ContentValues data = new ContentValues();
        data.put("email", Email);
        model.addMember(data);
    }

    public void deleteMember(String s) {
        model.deleteMember("email='" + s + "'");
    }

    public void addTask(Task task) {
        final ContentValues data = new ContentValues();
        data.put("description", task.description);
        data.put("category", task.category);
        data.put("priority", task.priority);
        data.put("assignedTeamMember", task.assignedTeamMember);
        data.put("dueDate", task.dueDate);
        data.put("dueTime", task.dueTime);
        data.put("longitude", task.longitude);
        data.put("latitude", task.latitude);
        data.put("status", task.status);
        model.addTask(data);
    }
}
