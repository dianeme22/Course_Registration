package com.example.courseregistrationwaitinglist.view;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseregistrationwaitinglist.R;
import com.example.courseregistrationwaitinglist.database.model.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder>{
    private Context context;
    private List<Course> courseList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView course;
        public TextView student;
        public TextView priority;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            course = view.findViewById(R.id.course);
            student = view.findViewById(R.id.student);
            priority = view.findViewById(R.id.priority);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public CourseAdapter(Context context, List<Course> notesList) {
        this.context = context;
        this.courseList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.course.setText(course.getCourse());
        holder.student.setText(course.getStudent());
        holder.priority.setText(course.getPriority());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(course.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

}
