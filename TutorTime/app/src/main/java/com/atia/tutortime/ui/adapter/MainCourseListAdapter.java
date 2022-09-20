package com.atia.tutortime.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.ui.activity.MainCourseDetailsActivity;

import java.util.ArrayList;

public class MainCourseListAdapter extends RecyclerView.Adapter<MainCourseListAdapter.myViewHolder> {

    private ArrayList<Courses> courseList;
    private Context context;

    public MainCourseListAdapter(ArrayList<Courses> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.main_course_list_items, parent, false);
        return new MainCourseListAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        Courses courses = courseList.get(position);
        holder.courseName.setText(courses.getCourseName());
        holder.courseClass.setText(courses.getcClass());
        String availabeSeat = courses.getAvailableSeat();
        if(Integer.parseInt(availabeSeat) > 0){
            holder.courseAvailableSeat.setText("Available");
        }
        else {
            holder.courseAvailableSeat.setText("Not available");
        }
        holder.title.setText(String.valueOf(courses.getCourseName().charAt(0))+String.valueOf(courses.getCourseName().charAt(1)));
        holder.courseFee.setText(courses.getCourseFee() + " TK");
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseClass, courseAvailableSeat, title, courseFee;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.main_course_name_id);
            courseClass = itemView.findViewById(R.id.main_class_id);
            courseAvailableSeat = itemView.findViewById(R.id.main_available_seat_id);
            title = itemView.findViewById(R.id.main_course_title_id);
            courseFee = itemView.findViewById(R.id.main_course_fee_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Courses courses = courseList.get(pos);
                    Intent intent = new Intent(context, MainCourseDetailsActivity.class);
                    intent.putExtra("availableSeat", courses.getAvailableSeat());
                    intent.putExtra("class", courses.getcClass());
                    intent.putExtra("courseName", courses.getCourseName());
                    intent.putExtra("endTime", courses.getEndDate());
                    intent.putExtra("media", courses.getMedia());
                    intent.putExtra("startTime", courses.getStartDate());
                    intent.putExtra("totalSeat", courses.getTotalSeat());
                    intent.putExtra("teacherId", courses.getTeacherId());
                    intent.putExtra("courseId", courses.getcId());
                    intent.putExtra("requestList", courses.getRequestList());
                    intent.putExtra("courseFee", courses.getCourseFee());
                    intent.putExtra("liveClass", courses.getLiveClass());
                    intent.putExtra("modelTest", courses.getModelTest());
                    intent.putExtra("notes", courses.getNotes());
                    intent.putExtra("finalExam", courses.getFinalExam());
                    context.startActivity(intent);
                    courseList.clear();
                }
            });
        }
    }
}
