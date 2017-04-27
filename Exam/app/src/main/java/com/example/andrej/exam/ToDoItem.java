package com.example.andrej.exam;


import java.io.Serializable;

public class ToDoItem implements Serializable {
    private String name;
    private String date;
    private boolean checked;

    public ToDoItem(String name, String date) {
        setName(name);
        setDate(date);
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean getCh() {return checked;}
    public void setCh(boolean status) {this.checked = status;}
}

