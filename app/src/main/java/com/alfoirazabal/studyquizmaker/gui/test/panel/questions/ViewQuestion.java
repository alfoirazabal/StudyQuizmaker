package com.alfoirazabal.studyquizmaker.gui.test.panel.questions;

import android.content.Intent;
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
import com.alfoirazabal.studyquizmaker.domain.question.Question;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.AddQuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.recyclerview.AdapterQuestionView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewQuestion extends AppCompatActivity {

    private FloatingActionButton fabtnAdd;

    private List<Question> questions;
    private AdapterQuestionView adapterQuestionView;

    private AppDatabase db;

    private String currentTestId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_simple_view);

        RecyclerView recyclerviewSimpleQuestions = findViewById(R.id.recyclerview_simple_questions);
        fabtnAdd = findViewById(R.id.fabtn_add);

        Objects.requireNonNull(getSupportActionBar()).setSubtitle(R.string.questions_simple);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        Bundle bundle = getIntent().getExtras();
        currentTestId = bundle.getString("TESTID");

        questions = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerviewSimpleQuestions.setLayoutManager(layoutManager);
        adapterQuestionView = new AdapterQuestionView(questions);
        recyclerviewSimpleQuestions.setAdapter(adapterQuestionView);

        fabtnAdd.setOnClickListener(v -> {
            Intent intentAddQuestionSimple = new Intent(
                    this, AddQuestionSimple.class
            );
            intentAddQuestionSimple.putExtra("TESTID", currentTestId);
            startActivity(intentAddQuestionSimple);
        });

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
        questions.clear();
        new Thread(() -> {
            questions.addAll(db.questionSimpleDAO().getFromTest(currentTestId));
            questions.addAll(db.questionTFDAO().getFromTest(currentTestId));
            questions.addAll(db.questionMCDAO().getFromTest(currentTestId));
            runOnUiThread(() -> adapterQuestionView.notifyDataSetChanged());
        }).start();
    }
}
