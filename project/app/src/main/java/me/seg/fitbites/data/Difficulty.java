package me.seg.fitbites.data;

public enum Difficulty {

    BEGINNER("BEGINNER");
    INTERMEDIATE(level:"INTERMEDIATE");
    EXPERIENCED(level: "EXPERIENCED");


    private String level;

    private Difficulty(String level) {
        this.level = level;
    }

    public String toString() {
        return level;
    }


}
