package me.seg.fitbites;

public class FitClassType {
    private String uid;
    private String className;
    private String description;

    public FitClassType(String uid, String className, String description) {
        this.uid = uid;
        this.className = className;
        this.description = description;
    }

    public FitClassType() {}

    public String getUid() {
        return uid;
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
}
