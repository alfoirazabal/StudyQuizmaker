package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.recyclerviews.AdapterTestRun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewTestRuns extends AppCompatActivity {

    private String testId;

    private AppDatabase db;
    private AdapterTestRun adapterTestRun;
    private List<TestRun> testRuns;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testruns_view);

        getSupportActionBar().setSubtitle(R.string.test_runs);

        RecyclerView recyclerviewTestRuns = findViewById(R.id.recyclerview_testruns);

        this.testId = getIntent().getExtras().getString("TESTID");
        this.testRuns = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext()
        );
        recyclerviewTestRuns.setLayoutManager(layoutManager);
        adapterTestRun = new AdapterTestRun(
                this.testRuns
        );
        recyclerviewTestRuns.setAdapter(adapterTestRun);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            String testName = db.testDAO().getById(this.testId).name;
            runOnUiThread(() -> getSupportActionBar().setTitle(testName));
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.testRuns.clear();
        new Thread(() -> {
            this.testRuns.addAll(db.testRunDAO().getRunsFromTest(this.testId));
            Collections.sort(this.testRuns);
            runOnUiThread(() -> adapterTestRun.notifyDataSetChanged());
        }).start();
    }
}
