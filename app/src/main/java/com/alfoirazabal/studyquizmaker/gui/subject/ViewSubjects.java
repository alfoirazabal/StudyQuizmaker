package com.alfoirazabal.studyquizmaker.gui.subject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.Manifest;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.gui.subject.recyclerviews.AdapterSubjectView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewSubjects extends AppCompatActivity {

    private RecyclerView recyclerviewSubjects;
    private FloatingActionButton fabnAdd;

    private List<Subject> subjects;
    private AdapterSubjectView adapterSubject;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_view);

        initialSetting();

        recyclerviewSubjects = findViewById(R.id.recyclerview_subjects);
        fabnAdd = findViewById(R.id.fabtn_add);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        this.recyclerviewSubjects.setLayoutManager(layoutManager);

        fabnAdd.setOnClickListener(v -> {
            // TODO
        });

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.DATABASE_LOCATION
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
            adapterSubject.notifyDataSetChanged();
        }).start();
    }

    private void initialSetting() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        new File(AppConstants.DATABASE_PATH).mkdirs();
    }
}