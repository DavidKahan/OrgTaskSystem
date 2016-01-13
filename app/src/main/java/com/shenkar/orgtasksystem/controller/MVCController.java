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

    public MVCController(Context app_context) {
//        model = new MVCModel(app_context);
        model = new MVCModel();
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

    public List<Task> loadDoneTasks(String memberName) throws ParseException {
        return model.loadDoneTasks(memberName);
    }

    public List<Task> loadWaitingTasks(String memberName) throws ParseException {
        return model.loadWaitingTasks(memberName);
    }

    public List<Task> loadPendingTasks(String memberName) throws ParseException {
        return model.loadPendingTasks(memberName);
    }

    public void addMember(Member member) {
        model.addMember(member);
    }

    public void createTeamName(String mTeamName) {
        model.createTeamName(mTeamName);
    }

    public void addTask(Task task) {
        model.addTask(task);
    }

    public void updateTaskStatusByID(Task task) {
        model.updateTaskStatusByID(task);
    }

//    public void deleteMember(String s) {
//        model.deleteMember("email='" + s + "'");
//    }


}
