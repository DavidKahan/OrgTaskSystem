package com.shenkar.orgtasksystem.controller;

import android.content.Context;
import android.graphics.Bitmap;
import com.parse.ParseException;
import com.shenkar.orgtasksystem.model.MVCModel;
import com.shenkar.orgtasksystem.model.Member;
import com.shenkar.orgtasksystem.model.Task;
import java.io.ByteArrayOutputStream;
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

    public String getTeamName() throws ParseException {
        return model.getTeamName();
    }

    public String getManagerName() throws ParseException {
        return model.getManagerName();
    }

    public List<Task> loadDoneTasks(String memberEmail) throws ParseException {
        return model.loadDoneTasks(memberEmail);
    }

    public List<Task> loadWaitingTasks(String memberEmail) throws ParseException {
        return model.loadWaitingTasks(memberEmail);
    }

    public List<Task> loadPendingTasks(String memberEmail) throws ParseException {
        return model.loadPendingTasks(memberEmail);
    }

    public void addMember(Member member) {
        model.addMember(member);
    }

    public void createTeamName(String mTeamName) {
        model.createTeamName(mTeamName);
    }

    public void addTask(Task task) throws ParseException {
        model.addTask(task);
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
