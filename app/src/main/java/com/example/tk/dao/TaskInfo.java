package com.example.tk.dao;

public class TaskInfo {
    private String date;
    private String taskId;
    private String startTime;
    private String endTime;
    private String taskName;
    private String creator;
    private int creatorId;

    public TaskInfo() {
    }

    public TaskInfo(String date, String taskId, String startTime, String endTime,
                    String taskName, String creator, int creatorId) {
        this.date = date;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskName = taskName;
        this.creator = creator;
        this.creatorId = creatorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
}