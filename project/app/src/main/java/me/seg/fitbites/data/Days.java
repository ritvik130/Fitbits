package me.seg.fitbites.data;

public enum Days {


    SUNDAY("SUNDAY", "Sun"),
    MONDAY("MONDAY", "Mon"),
    TUESDAY("TUESDAY", "Tues"),
    WEDNESDAY("WEDNESDAY", "Wed"),
    THURSDAY("THURSDAY", "Thurs"),
    FRIDAY("FRIDAY", "Fri"),
    SATURDAY("SATURDAY", "Sat");

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
