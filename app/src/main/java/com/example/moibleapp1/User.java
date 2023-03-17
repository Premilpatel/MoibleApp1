package com.example.moibleapp1;

public class User {
    public String username;
    public String email;
    public static int ID = 111;

    public User(){
        username = "";
        email = "";
        ID++;
    }


    public User(String username, String email){
        this.username = username;
        this.email = email;
        ID++;
    }
    public int getID(){
        return ID;
    }

}
