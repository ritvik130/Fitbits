package me.seg.fitbites;

public abstract class UserData {
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String password;
    protected int age;
    protected String address;

    public UserData(String firstName,String lastName,String userName, String password,int age,String address){
        this.firstName=firstName;
        this.address=address;
        this.lastName=lastName;
        this.password=password;
        this.userName=userName;
        this.age=age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void removeAccount(UserData user){
        // removes the account of a user either instructor or member
        // access to admin

    }
}
