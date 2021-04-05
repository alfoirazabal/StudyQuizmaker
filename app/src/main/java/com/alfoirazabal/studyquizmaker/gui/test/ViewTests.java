package com.alfoirazabal.studyquizmaker.gui.test;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.gui.test.recyclerviews.AdapterTestView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewTests extends AppCompatActivity {

    private RecyclerView recyclerviewTests;
    private FloatingActionButton fabtnAdd;

    private String topicId;

    private List<Test> tests;
    private AdapterTestView adapterTest;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_view);

        getSupportActionBar().setSubtitle(getString(R.string.tests));

        recyclerviewTests = findViewById(R.id.recyclerview_tests);
        fabtnAdd = findViewById(R.id.fabtn_add);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.DATABASE_LOCATION
        ).build();

        tests = new ArrayList<>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                3,
                StaggeredGridLayoutManager.VERTICAL
        );
        this.recyclerviewTests.setLayoutManager(layoutManager);
        adapterTest = new AdapterTestView(tests);
        recyclerviewTests.setAdapter(adapterTest);

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            topicId = bundle.getString("TOPICID");
            Topic currentTopic = db.topicDAO().getById(topicId);
            runOnUiThread(() -> {
                getSupportActionBar().setTitle(currentTopic.name);
                fabtnAdd.setEnabled(true);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tests.clear();
        new Thread(() -> {
            tests.addAll(db.testDAO().getFromTopic(topicId));
            runOnUiThread(() -> adapterTest.notifyDataSetChanged());
        }).start();
    }
}
