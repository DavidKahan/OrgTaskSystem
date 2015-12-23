//package com.shenkar.orgtasksystem.presenter;
//
//import com.shenkar.orgtasksystem.model.IDataAccess;
//import com.shenkar.orgtasksystem.model.MockDAO;
//import com.shenkar.orgtasksystem.model.Task;
//
//import java.util.List;
//
///**
// * Created by david on 12/11/2015.
// */
//public class TaskController implements ITaskController {
//
//    private IDataAccess dao;
//
//    public TaskController() {
//        dao = MockDAO.getInstance();
//    }
//
//    @Override
//    public List<Task> GetTasks() {
//        return dao.getTasks();
//    }
//
//    @Override
//    public void addTask(String newTask) {
//        dao.addTask(newTask);
//    }
//}
