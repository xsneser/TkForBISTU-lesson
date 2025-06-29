package com.example.tk.activity;

public class Plan {
    private int id;
    private String date;
    private String time;
    private String content;

    public Plan(int id, String date, String time, String content) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}