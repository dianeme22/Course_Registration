package com.example.courseregistrationwaitinglist.database.model;

public class Course {
    public static final String TABLE_NAME = "courses";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_STUDENT = "student";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String course;
    private String student;
    private String priority;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_COURSE + " TEXT,"
                    + COLUMN_STUDENT + " TEXT,"
                    + COLUMN_PRIORITY + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Course() {
    }

    public Course(int id, String course, String student, String priority, String timestamp) {
        this.id = id;
        this.course = course;
        this.student = student;
        this.priority = priority;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}