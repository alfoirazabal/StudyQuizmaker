package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun.runresults;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun.runresults.recyclerviews.AdapterQuestionSimpleResponse;

public class ViewRunResults extends AppCompatActivity {

    private RecyclerView recyclerviewAnswers;
    private ProgressBar progressBarScore;
    private TextView txtScored;

    private AppDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_view_run_results);

        recyclerviewAnswers = findViewById(R.id.recyclerview_answers);
        progressBarScore = findViewById(R.id.progressbar_score);
        txtScored = findViewById(R.id.txt_scored);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                3,
                StaggeredGridLayoutManager.VERTICAL
        );
        recyclerviewAnswers.setLayoutManager(layoutManager);

        new Thread(() -> {
            String testRunId = getIntent().getExtras().getString("TESTRUNID");
            TestRun testRun = db.testRunDAO().getById(testRunId);
            testRun.questionSimpleResponses =
                    db.questionSimpleResponseDAO().getResponsesFromTestRun(testRunId);
            runOnUiThread(() -> {
                AdapterQuestionSimpleResponse adapter = new AdapterQuestionSimpleResponse(
                        testRun.questionSimpleResponses,
                        db
                );
                recyclerviewAnswers.setAdapter(adapter);
                progressBarScore.setMax(100);
                progressBarScore.setProgress((int)testRun.scoredPercentage);
                String scoredTextIndicator = testRun.scoredPercentage + "%";
                txtScored.setText(scoredTextIndicator);
            });
        }).start();

    }
}
