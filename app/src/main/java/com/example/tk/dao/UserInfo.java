package com.example.tk.dao;

public class UserInfo {
    private int id;
    private String username;
    private String paswd;

    public UserInfo(){
        super();
    }

    public UserInfo(int id, String username, String paswd) {
        this.id = id;
        this.username = username;
        this.paswd = paswd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaswd() { // 与字段名保持一致
        return paswd;
    }

    public void setPaswd(String paswd) {
        this.paswd = paswd;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", paswd='" + paswd + '\'' +
                '}';
    }
}