package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results;

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
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponseComparators;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionTFResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.recyclerviews.AdapterQuestionResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        recyclerviewAnswers.setLayoutManager(layoutManager);

        new Thread(() -> {
            String testRunId = getIntent().getExtras().getString("TESTRUNID");
            TestRun testRun = db.testRunDAO().getById(testRunId);
            QuestionSimpleResponse[] questionSimpleResponses =
                    db.questionSimpleResponseDAO().getResponsesFromTestRun(testRunId);
            QuestionTFResponse[] questionTFResponses =
                    db.questionTFResponseDAO().getResponsesFromTestRun(testRunId);
            List<QuestionResponse> questionResponses = new ArrayList<>();
            questionResponses.addAll(Arrays.asList(questionSimpleResponses));
            questionResponses.addAll(Arrays.asList(questionTFResponses));
            Collections.sort(questionResponses, new QuestionResponseComparators.CompareByAskOrder());
            testRun.questionResponses = questionResponses.toArray(new QuestionResponse[0]);
            runOnUiThread(() -> {
                AdapterQuestionResponse adapter = new AdapterQuestionResponse(
                        testRun.questionResponses,
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
