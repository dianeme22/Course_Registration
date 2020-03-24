package com.example.courseregistrationwaitinglist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.courseregistrationwaitinglist.database.model.Course;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "courses_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Course table
        db.execSQL(Course.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Course.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertStudent(String course, String student, String priority) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically
        values.put(Course.COLUMN_COURSE, course);
        values.put(Course.COLUMN_STUDENT, student);
        values.put(Course.COLUMN_PRIORITY, priority);

        // insert row
        long id = db.insert(Course.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Course getStudent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Course.TABLE_NAME,
                new String[]{Course.COLUMN_ID, Course.COLUMN_COURSE, Course.COLUMN_STUDENT, Course.COLUMN_PRIORITY, Course.COLUMN_TIMESTAMP},
                Course.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare course object
        Course course = new Course(
                cursor.getInt(cursor.getColumnIndex(Course.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_COURSE)),
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_STUDENT)),
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_PRIORITY)),
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Course.TABLE_NAME + " ORDER BY " +
                Course.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setId(cursor.getInt(cursor.getColumnIndex(Course.COLUMN_ID)));
                course.setCourse(cursor.getString(cursor.getColumnIndex(Course.COLUMN_COURSE)));
                course.setStudent(cursor.getString(cursor.getColumnIndex(Course.COLUMN_STUDENT)));
                course.setPriority(cursor.getString(cursor.getColumnIndex(Course.COLUMN_PRIORITY)));
                course.setTimestamp(cursor.getString(cursor.getColumnIndex(Course.COLUMN_TIMESTAMP)));

                courses.add(course);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return courses list
        return courses;
    }

    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + Course.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Course.COLUMN_COURSE, course.getCourse());
        values.put(Course.COLUMN_STUDENT, course.getStudent());
        values.put(Course.COLUMN_PRIORITY, course.getPriority());

        // updating row
        return db.update(Course.TABLE_NAME, values, Course.COLUMN_ID + " = ?",
                new String[]{String.valueOf(course.getId())});
    }

    public void deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Course.TABLE_NAME, Course.COLUMN_ID + " = ?",
                new String[]{String.valueOf(course.getId())});
        db.close();
    }
}
