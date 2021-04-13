package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.recyclerviews.AdapterSimpleQuestionScore;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.ViewFinalResults;
import com.alfoirazabal.studyquizmaker.helpers.testrun.TestRunProcessor;

import java.util.List;

public class ScoreSimpleQuestions extends AppCompatActivity {

    private RecyclerView recyclerviewScores;
    private Button btnConfirmScores;

    private TestRun testRun;
    private List<QuestionSimpleResponse> questionSimpleResponses;

    private AppDatabase db;

    private AdapterSimpleQuestionScore adapterSimpleQuestionScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_simple_score);

        recyclerviewScores = findViewById(R.id.recyclerview_scores);
        btnConfirmScores = findViewById(R.id.btn_confirm_scores);

        testRun = (TestRun) getIntent().getSerializableExtra("TESTRUN");

        questionSimpleResponses = testRun.getQuestionSimpleResponses();

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapterSimpleQuestionScore =
                new AdapterSimpleQuestionScore(questionSimpleResponses, db);
        recyclerviewScores.setLayoutManager(layoutManager);
        recyclerviewScores.setAdapter(adapterSimpleQuestionScore);

        btnConfirmScores.setOnClickListener(v -> {
            TestRunProcessor testRunProcessor = new TestRunProcessor(this.testRun);
            new Thread(() -> {
                testRunProcessor.saveTestRunToDatabase(db);
                Intent intentViewResults =
                        new Intent(this, ViewFinalResults.class);
                intentViewResults.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intentViewResults.putExtra("TESTRUNID", testRun.id);
                this.startActivity(intentViewResults);
            }).start();
        });

    }
}
