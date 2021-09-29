package me.seg.fitbites;

public class Admin extends UserData {

    private Admin(String firstName, String lastName, String userName, String password) {
        super();
        // username and password for should be this
        this.userName = "admin";
        this.password = "admin123";
    }


}
