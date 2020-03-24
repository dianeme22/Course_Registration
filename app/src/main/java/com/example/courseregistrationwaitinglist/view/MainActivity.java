package com.example.courseregistrationwaitinglist.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseregistrationwaitinglist.R;
import com.example.courseregistrationwaitinglist.database.DatabaseHelper;
import com.example.courseregistrationwaitinglist.database.model.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.courseregistrationwaitinglist.utils.MyDividerItemDecoration;
import com.example.courseregistrationwaitinglist.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private CourseAdapter mAdapter;
    private List<Course> coursesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noCoursesView;

    private DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noCoursesView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this);

        coursesList.addAll(db.getAllCourses());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCourseDialog(false, null, -1);
            }
        });

        mAdapter = new CourseAdapter(this, coursesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyCourses();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    /**
     * Inserting new course,student,priority in db
     * and refreshing the list
     */
    private void createCourse(String course, String student, String priority) {
        // inserting course in db and getting
        // newly inserted course id
        long id = db.insertStudent(course, student, priority);

        // get the newly inserted course from db
        Course n = db.getStudent(id);

        if (n != null) {
            // adding new course to array list at 0 position
            coursesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyCourses();
        }
    }

    /**
     * Updating course, student, priority in db and updating
     * item in the list by its position
     */
    private void updateCourse(String course, String student, String priority, int position) {
        Course n = coursesList.get(position);
        // updating course text
        n.setCourse(course);
        n.setStudent(student);
        n.setPriority(priority);

        // updating course, student, priority in db
        db.updateCourse(n);

        // refreshing the list
        coursesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyCourses();
    }

    /**
     * Deleting course, student, priority from SQLite and removing the
     * item from the list by its position
     */
    private void deleteCourse(int position) {
        // deleting the course from db
        db.deleteCourse(coursesList.get(position));

        // removing the course from the list
        coursesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyCourses();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showCourseDialog(true, coursesList.get(position), position);
                } else {
                    deleteCourse(position);
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a course, student, priority.
     * when shouldUpdate=true, it automatically displays old course, student, priority and changes the
     * button text to UPDATE
     */
    private void showCourseDialog(final boolean shouldUpdate, final Course course, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputCourse = view.findViewById(R.id.course);
        final EditText inputStudent = view.findViewById(R.id.student);
        final EditText inputPriority = view.findViewById(R.id.priority);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && course != null) {
            inputCourse.setText(course.getCourse());
            inputStudent.setText(course.getStudent());
            inputPriority.setText(course.getPriority());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputCourse.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter course!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating course, student, priority
                if (shouldUpdate && course != null) {
                    // update course by it's id
                    updateCourse(inputCourse.getText().toString(),
                            inputStudent.getText().toString(),
                            inputPriority.getText().toString(),
                            position);
                } else {
                    // create new course
                    createCourse(inputCourse.getText().toString(),
                            inputStudent.getText().toString(),
                            inputPriority.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty courses view
     */
    private void toggleEmptyCourses() {
        // you can check coursesList.size() > 0

        if (db.getStudentsCount() > 0) {
            noCoursesView.setVisibility(View.GONE);
        } else {
            noCoursesView.setVisibility(View.VISIBLE);
        }
    }
}