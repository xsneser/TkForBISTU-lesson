package com.example.tk.dao;

public class FriendInfo {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    private String username;
    private String friendname;

    @Override
    public String toString() {
        return "FriendInfo{" +
                "outmessage=" + id +
                ", username='" + username + '\'' +
                ", friendname='" + friendname + '\'' +
                '}';
    }

    public FriendInfo(){
        super();
    }
    public FriendInfo(int id, String un, String fn){
        super();
        this.id = id;
        this.username = un;
        this.friendname = fn;
    }
}
