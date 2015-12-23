//package com.shenkar.orgtasksystem.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by david on 16/11/2015.
// */
//public class MockDAO implements IDataAccess {
//
//    private List<Task> taskList = new ArrayList<Task>();
//    private String[] tasks = {"task 1","task 2", "task 3","task 4", "task 5","task 6","task 7","task 8","task 9","task 10","task 11","task 12","task 13","task 14","task 14","task 15","task 16","task 17","task 18","task 19","task 20"};
//    private static MockDAO instance;
//
//    public MockDAO() {
//        for (int i=0 ; i<tasks.length ; i++ ){
//            Task current = new Task();
//            current.task = tasks[i];
//            taskList.add(current);
//        }
//    }
//    //Singletone implementation.
//
//    public static MockDAO getInstance()
//    {
//        if(instance ==  null)
//            instance = new MockDAO();
//        return instance;
//    }
//
//    @Override
//    public List<Task> getTasks() {
//        return taskList;
//    }
//
//    @Override
//    public void addTask(String newTask) {
//        Task current = new Task();
//        current.task = newTask;
//        taskList.add(current);
//    }
//}
