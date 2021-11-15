package me.seg.fitbites.data;

public enum Days {


    SUNDAY("Sunday", "Sun"),
    MONDAY("Monday", "Mon"),
    TUESDAY("Tuesday", "Tues"),
    WEDENSDAY("Wednesday", "Wed"),
    THURSDAY("Thursday", "Thurs"),
    FRIDAY("Friday", "Fri"),
    SATURDAY("Saturday", "Sat");

    private String form;
    private String sform;

    private Days(String f, String sf) {
        form = f;
        sform = sf;
    }

    public String toString() {
        return form;
    }

    public String toStringShort() {
        return sform;
    }

}
