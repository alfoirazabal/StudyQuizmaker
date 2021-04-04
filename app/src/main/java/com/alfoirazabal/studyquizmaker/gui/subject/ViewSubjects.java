package com.alfoirazabal.studyquizmaker.gui.subject;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.gui.subject.recyclerviews.AdapterSubjectView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.alfoirazabal.studyquizmaker.AppConstants.*;

public class ViewSubjects extends AppCompatActivity {

    private List<Subject> subjects;
    private AdapterSubjectView adapterSubject;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_view);

        getSupportActionBar().setSubtitle(R.string.subjects);

        initialSetting();

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
                DATABASE_LOCATION
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

    private void initialSetting() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        File pathToDatabase = new File(DATABASE_PATH);
        if (!pathToDatabase.exists()) {
            boolean directoriesCreated = pathToDatabase.mkdirs();
            if (!directoriesCreated) {
                throw new Error("The directories were not created!");
            }
        }
    }
}