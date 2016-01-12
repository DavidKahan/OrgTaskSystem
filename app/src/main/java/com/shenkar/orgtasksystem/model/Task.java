package com.shenkar.orgtasksystem.model;

import java.io.Serializable;

/**
 * Created by david on 27/10/2015.
 */
public class Task implements Serializable{
   public String id;
   public String category;
   public String priority;
   public String assignedTeamMember;
   public String dueDate;
   public String dueTime;
   public String location;
//   public String longitude;
//   public String latitude;
   public String status;
   public String description;
}
