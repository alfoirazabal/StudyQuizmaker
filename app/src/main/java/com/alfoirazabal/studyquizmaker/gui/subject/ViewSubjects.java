package com.alfoirazabal.studyquizmaker.gui.subject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.gui.subject.recyclerviews.AdapterSubjectView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewSubjects extends AppCompatActivity {

    private List<Subject> subjects;
    private AdapterSubjectView adapterSubject;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_view);

        getSupportActionBar().setSubtitle(R.string.subjects);

        RecyclerView recyclerviewSubjects = findViewById(R.id.recyclerview_subjects);
        FloatingActionButton fabnAdd = findViewById(R.id.fabtn_add);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerviewSubjects.setLayoutManager(layoutManager);

        fabnAdd.setOnClickListener(v -> {
            Intent intentAddSubject = new Intent(ViewSubjects.this, AddSubject.class);
            ViewSubjects.this.startActivity(intentAddSubject);
        });

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        subjects = new ArrayList<>();
        adapterSubject = new AdapterSubjectView(subjects);
        recyclerviewSubjects.setAdapter(adapterSubject);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            subjects.clear();
            subjects.addAll(db.subjectDAO().getAll());
            runOnUiThread(() -> adapterSubject.notifyDataSetChanged());
        }).start();
    }
}