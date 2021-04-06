package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.recyclerviews.AdapterQuestionSimpleView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewQuestionsSimple extends AppCompatActivity {

    private RecyclerView recyclerviewSimpleQuestions;
    private FloatingActionButton fabtnAdd;

    private List<QuestionSimple> questionsSimple;
    private AdapterQuestionSimpleView adapterQuestionSimpleView;

    private AppDatabase db;

    private String currentTestId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_simple_view);

        recyclerviewSimpleQuestions = findViewById(R.id.recyclerview_simple_questions);
        fabtnAdd = findViewById(R.id.fabtn_add);

        getSupportActionBar().setSubtitle(R.string.questions_simple);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        Bundle bundle = getIntent().getExtras();
        currentTestId = bundle.getString("TESTID");

        questionsSimple = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerviewSimpleQuestions.setLayoutManager(layoutManager);
        adapterQuestionSimpleView = new AdapterQuestionSimpleView(questionsSimple);

        new Thread(() -> {
            Test currentTest = db.testDAO().getById(currentTestId);
            runOnUiThread(() -> {
                setTitle(currentTest.name);
                fabtnAdd.setEnabled(true);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionsSimple.clear();
        new Thread(() -> {
            questionsSimple.addAll(db.questionSimpleDAO().getFromTest(currentTestId));
            runOnUiThread(() -> adapterQuestionSimpleView.notifyDataSetChanged());
        }).start();
    }
}
