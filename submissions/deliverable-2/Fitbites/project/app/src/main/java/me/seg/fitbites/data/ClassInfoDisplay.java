package me.seg.fitbites.data;

import java.util.ArrayList;

import me.seg.fitbites.firebase.OnObjectFilled;

public class ClassInfoDisplay implements OnObjectFilled {
    private ArrayList<String> users;
    private ArrayList<String> className;
    private ArrayList<String> date;
    private OnObjectFilled ob;

    public ClassInfoDisplay(){
        this.ob = null;
    }
    public void setDate(ArrayList<String>date){
        this.date = date;
    }
    public void setUsers(ArrayList<String>users){
        this.users = users;
    }
    public void setClassName(ArrayList<String>className){
        this.className = className;
    }
    public ArrayList<String> getUsers(){
        return users;
    }
    public ArrayList<String> getDate(){
        return date;
    }
    public ArrayList<String> getClassName(){
        return className;
    }

    public void setObjectFilledListener(OnObjectFilled ob){
        this.ob = ob;
    }
    @Override
    public void onObjectReady() {

    }

}
