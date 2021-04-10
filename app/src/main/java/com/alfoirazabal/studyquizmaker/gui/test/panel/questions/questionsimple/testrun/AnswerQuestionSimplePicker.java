package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun.recyclerviews.AdapterQuestionSimplePicker;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun.runresults.ViewRunResults;
import com.alfoirazabal.studyquizmaker.helpers.testrun.TestRunProcessor;

public class AnswerQuestionSimplePicker extends AppCompatActivity {

    private TestRun currentTestRun;

    private RecyclerView recyclerviewQuestions;
    private Button btnFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_simplequestion_picker);

        recyclerviewQuestions = findViewById(R.id.recyclerview_questions);
        btnFinish = findViewById(R.id.btn_finish);

        Bundle bundle = getIntent().getExtras();
        currentTestRun = (TestRun) bundle.getSerializable("TESTRUN");

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL
        );
        this.recyclerviewQuestions.setLayoutManager(layoutManager);
        AdapterQuestionSimplePicker adapterQuestionSimplePicker = new AdapterQuestionSimplePicker(
                currentTestRun.questionSimpleResponses,
                Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        AppConstants.getDBLocation(getApplicationContext())
                ).build(),
                currentTestRun
        );
        this.recyclerviewQuestions.setAdapter(adapterQuestionSimplePicker);

        this.btnFinish.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.finish_test)
                    .setMessage(R.string.msg_confirmation_finish_test)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        finishTestRun();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

    }

    private void finishTestRun() {
        TestRunProcessor testRunProcessor = new TestRunProcessor(this.currentTestRun);
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(
                    getApplicationContext(),
                    AppDatabase.class,
                    AppConstants.getDBLocation(getApplicationContext())
            ).build();
            testRunProcessor.saveTestRunToDatabase(db);
            Intent intentViewResults =
                    new Intent(AnswerQuestionSimplePicker.this, ViewFinalResults.class);
            intentViewResults.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intentViewResults.putExtra("TESTRUNID", currentTestRun.id);
            this.startActivity(intentViewResults);
        }).start();
    }

    @Override
    public void onBackPressed() {
        Intent intentAnswerSimpleQuestion = new Intent(
                getApplicationContext(),
                AnswerQuestionSimple.class
        );
        intentAnswerSimpleQuestion.putExtra("TESTRUN", currentTestRun);
        intentAnswerSimpleQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(intentAnswerSimpleQuestion);
    }
}
