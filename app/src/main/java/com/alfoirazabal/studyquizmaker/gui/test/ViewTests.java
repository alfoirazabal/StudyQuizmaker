package com.alfoirazabal.studyquizmaker.gui.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import java.util.Collections;
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
                AppConstants.getDBLocation(getApplicationContext())
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

        fabtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddTest = new Intent(getApplicationContext(), AddTest.class);
                intentAddTest.putExtra("TOPICID", topicId);
                startActivity(intentAddTest);;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tests.clear();
        new Thread(() -> {
            tests.addAll(db.testDAO().getFromTopic(topicId));
            Collections.sort(tests);
            runOnUiThread(() -> adapterTest.notifyDataSetChanged());
        }).start();
    }
}
