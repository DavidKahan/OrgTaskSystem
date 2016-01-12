package com.shenkar.orgtasksystem.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.parse.ParseException;
import com.shenkar.orgtasksystem.model.MVCModel;
import com.shenkar.orgtasksystem.model.Member;
import com.shenkar.orgtasksystem.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 11/23/2015.
 */

public class MVCController {
    private MVCModel model;
    private List<String> members;
//    private List<Task> allTasks;
//    private List<Task> waitingTasks;
//    private List<Task> pendingTasks;

    public MVCController(Context app_context) {
        members = new ArrayList<String>();
//        allTasks = new ArrayList<Task>();
//        waitingTasks = new ArrayList<Task>();
//        pendingTasks = new ArrayList<Task>();
        model = new MVCModel(app_context);
    }

    public void deleteTask(final long id) {
        model.deleteTask("id='" + id + "'");
    }

    public void deleteAllMembers() {
        model.deleteMember(null);
    }

    public List<String> getMembers() throws ParseException {
//        Cursor c = model.loadAllMembers();
//        members.clear();
//        if (c != null) {
//            c.moveToFirst();
//            while (c.isAfterLast() == false) {
//                members.add(c.getString(0));
//                c.moveToNext();
//            }
//            c.close();
//        }
//        return members;
        return model.loadAllMembers();
    }

    public List<Task> loadDoneTasks(String memberName) throws ParseException {
        return model.loadDoneTasks(memberName);
    }

    public List<Task> loadWaitingTasks(String memberName) throws ParseException {
//        Cursor c = model.loadWaitingTasks();
//        waitingTasks.clear();
//        if (c != null) {
//            c.moveToFirst();
//            while (c.isAfterLast() == false) {
//                Task tmp = new Task();
//                tmp.description = c.getString(0);
//                tmp.assignedTeamMember = c.getString(1);
//                tmp.dueDate = c.getString(2);
//                tmp.dueTime = c.getString(3);
//                tmp.category = c.getString(4);
//                tmp.priority = c.getString(5);
//                tmp.id = c.getString(6);
//                tmp.status = c.getString(7);
//                waitingTasks.add(tmp);
//                c.moveToNext();
//            }
//            c.close();
//        }
//        return waitingTasks;
        return model.loadWaitingTasks(memberName);
    }

    public List<Task> loadPendingTasks(String memberName) throws ParseException {
        return model.loadPendingTasks(memberName);
    }


    public void addMember(Member member) {
        final ContentValues data = new ContentValues();
        data.put("email", member.email);
        data.put("password", member.password);
        data.put("type", member.type);
        model.addMember(data, member);
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
        model.addTask(data, task);
    }

    public void updateTaskStatusByID(Task task) {
        final ContentValues data = new ContentValues();
        data.put("status", task.status);
//        data.put("priority", task.priority);
//        data.put("assignedTeamMember", task.assignedTeamMember);
//        data.put("dueDate", task.dueDate);
//        data.put("dueTime", task.dueTime);
//        data.put("longitude", task.longitude);
//        data.put("latitude", task.latitude);
//        data.put("status", task.status);
        model.updateTaskStatusByID(data, task.id);
    }

    public void deleteMember(String s) {
        model.deleteMember("email='" + s + "'");
    }
}
