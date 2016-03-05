package com.shenkar.orgtasksystem.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.parse.ParseException;
import com.shenkar.orgtasksystem.model.MVCModel;
import com.shenkar.orgtasksystem.model.Member;
import com.shenkar.orgtasksystem.model.Task;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 11/23/2015.
 */

public class MVCController {
    private MVCModel model;
    private List<String> members;
    private List<Task> doneTasks;
    private List<Task> waitingTasks;
    private List<Task> pendingTasks;

    public MVCController(Context app_context) {
        members = new ArrayList<String>();
        doneTasks = new ArrayList<Task>();
        waitingTasks = new ArrayList<Task>();
        pendingTasks = new ArrayList<Task>();
        model = new MVCModel(app_context);
    }

//    public void deleteTask(final long id) {
//        model.deleteTask("id='" + id + "'");
//    }
//
//    public void deleteAllMembers() {
//        model.deleteMember(null);
//    }

    public List<String> getMembers() throws ParseException {
        return model.loadAllMembers();
    }

    public String getTeamName() throws ParseException {
        return model.getTeamName();
    }

    public String getManagerName() throws ParseException {
        return model.getManagerName();
    }

    public List<Task> loadDoneTasksFromParse(String memberEmail) throws ParseException {
        return model.loadDoneTasksFromParse(memberEmail);
    }

    public List<Task> loadWaitingTasksFromParse(String memberEmail) throws ParseException {
        return model.loadWaitingTasksFromParse(memberEmail);
    }

    public List<Task> loadPendingTasksFromParse(String memberEmail) throws ParseException {
        return model.loadPendingTasksFromParse(memberEmail);
    }

    public List<Task> loadDoneTasks() {
        Cursor c = model.loadDoneTasks();
        doneTasks.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                Task tmp = new Task();
                tmp.description = c.getString(0);
                tmp.assignedTeamMember = c.getString(1);
                tmp.dueDate = c.getString(2);
                tmp.dueTime = c.getString(3);
                tmp.category = c.getString(4);
                tmp.priority = c.getString(5);
                tmp.parseID = c.getString(6);
                tmp.status = c.getString(7);
                doneTasks.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return doneTasks;
    }

    public List<Task> loadWaitingTasks() {
        Cursor c = model.loadWaitingTasks();
        waitingTasks.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                Task tmp = new Task();
                tmp.description = c.getString(0);
                tmp.assignedTeamMember = c.getString(1);
                tmp.dueDate = c.getString(2);
                tmp.dueTime = c.getString(3);
                tmp.category = c.getString(4);
                tmp.priority = c.getString(5);
                tmp.id = c.getString(6);
                tmp.status = c.getString(7);
                waitingTasks.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return waitingTasks;
    }

    public List<Task> loadPendingTasks() {
        Cursor c = model.loadPendingTasks();
        pendingTasks.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                Task tmp = new Task();
                tmp.description = c.getString(0);
                tmp.assignedTeamMember = c.getString(1);
                tmp.dueDate = c.getString(2);
                tmp.dueTime = c.getString(3);
                tmp.category = c.getString(4);
                tmp.priority = c.getString(5);
                tmp.id = c.getString(6);
                tmp.status = c.getString(7);
                pendingTasks.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return pendingTasks;
    }

    public void addMember(Member member) throws ParseException {
        model.addMember(member);
    }

    public void createTeamName(String mTeamName) {
        model.createTeamName(mTeamName);
    }

    public void addTask(Task task) throws ParseException {
        model.addTask(task);
    }

    public void deleteTask(Task task) throws ParseException {
        model.deleteTask(task);
    }

    public void updateTaskStatusByID(Task task) throws ParseException {
        model.updateTaskStatusByID(task);
    }

    public void uploadImage(String id, Bitmap imageBitmap) throws ParseException {
        // Convert bitmap to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        model.uploadImage(id, image);
    }

    public Bitmap loadImageById(String id) throws ParseException {
        return model.loadImageById(id);
    }

//    public void deleteMember(String s) {
//        model.deleteMember("email='" + s + "'");
//    }
}
