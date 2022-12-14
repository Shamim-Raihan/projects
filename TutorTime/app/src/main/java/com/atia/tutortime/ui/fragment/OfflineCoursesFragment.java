package com.atia.tutortime.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.ui.activity.TeacherCourseListActivity;
import com.atia.tutortime.ui.adapter.CourseListAdapter;
import com.atia.tutortime.ui.adapter.MainCourseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class OfflineCoursesFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference courseDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private String studentClass, status;
    private ArrayList<Courses> courseList;
    private RecyclerView recyclerView;
    private MainCourseListAdapter mainCourseListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_courses, container, false);

        recyclerView = view.findViewById(R.id.main_recyclerView_id);
        mAuth = FirebaseAuth.getInstance();
        courseDatabaseReference = FirebaseDatabase.getInstance().getReference("course");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");


        courseList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        courseList.clear();
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_box, null));
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();

        userDatabaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertDialog.show();
                status = snapshot.child("status").getValue(String.class);
                if (status.equals("teacher")) {
                    courseList.clear();
                    courseDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Courses courses = dataSnapshot.getValue(Courses.class);
                                courseList.add(courses);
                            }
                            mainCourseListAdapter = new MainCourseListAdapter(courseList, getContext());
                            recyclerView.setAdapter(mainCourseListAdapter);
                            alertDialog.dismiss();
                            mainCourseListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else if (status.equals("student")) {
                    studentClass = snapshot.child("sClass").getValue(String.class);
                    courseDatabaseReference.orderByChild("cClass").equalTo(studentClass).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            courseList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Courses courses = dataSnapshot.getValue(Courses.class);
                                courseList.add(courses);
                            }
                            mainCourseListAdapter = new MainCourseListAdapter(courseList, getContext());
                            recyclerView.setAdapter(mainCourseListAdapter);
                            alertDialog.dismiss();
                            mainCourseListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}