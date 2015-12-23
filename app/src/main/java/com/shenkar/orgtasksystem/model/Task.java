package com.shenkar.orgtasksystem.model;

import android.location.Geocoder;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
/**
 * Created by david on 27/10/2015.
 */
public class Task implements Serializable {
   public String category;
   public String priority;
   public String assignedTeamMember;
   public String dueDate;
   public String dueTime;
   public String longitude;
   public String latitude;
   public String status;
   public String description;
}
