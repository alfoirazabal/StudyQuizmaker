package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun.recyclerviews.AdapterQuestionSimplePicker;

public class AnswerQuestionSimplePicker extends AppCompatActivity {

    private TestRun currentTestRun;

    private RecyclerView recyclerviewQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_simplequestion_picker);

        recyclerviewQuestions = findViewById(R.id.recyclerview_questions);

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
