package me.seg.fitbites;

import java.util.HashMap;

public class FitClass {
    private String className;
    private String description;

    public FitClass(String className, String description) {
        this.className = className;
        this.description = description;

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static FitClass createClass(){
        // creates a new Class
        // access to admin
        return null;

    }

    public void deleteClass(FitClass cla){
        // deletes a class
        // access to admin
    }

    public void editClass(String className, String description){
        // edits a class
        // access to admin
        setClassName(className);
        setDescription(description);
    }
    public FitClass searchClass(String className){
        // returns the searched class
        return null;
    }

    public void cancelClass(FitClass cla){
        //cancels a class
        // access to instructor
        System.out.println(cla+"class has been cancelled. ");
    }
    public void enrolClass(FitClass cla){
        // enrols in class
        System.out.println(cla+"has been enrolled");
    }
    public void dropClass(FitClass cla){
        // drops class
        System.out.println(cla+"has been dropped");
    }

    public HashMap<String, Object> toHashmap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("Name", className);
        result.put("Description", description);

        return result;
    }

}