package com.alfoirazabal.studyquizmaker.gui.test.panel.questions;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;
import android.widget.Toast;

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
import com.alfoirazabal.studyquizmaker.domain.question.QuestionComparators;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMO;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionmc.AddQuestionMC;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionmo.AddQuestionMO;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.AddQuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questiontf.AddQuestionTF;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.recyclerview.AdapterQuestionView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        Objects.requireNonNull(getSupportActionBar()).setSubtitle(R.string.manage_questions);

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
            PopupMenu popupMenu = new PopupMenu(
                    getApplicationContext(),
                    fabtnAdd
            );
            popupMenu.inflate(R.menu.menu_question_add_question_type);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Intent intentAddQuestion = null;
                switch (menuItem.getItemId()) {
                    case R.id.item_simple_question:
                        intentAddQuestion = new Intent(
                                this, AddQuestionSimple.class
                        );
                        break;
                    case R.id.item_multiple_choice:
                        intentAddQuestion = new Intent(
                                this, AddQuestionMC.class
                        );
                        break;
                    case R.id.item_true_or_false:
                        intentAddQuestion = new Intent(
                                this, AddQuestionTF.class
                        );
                        break;
                    case R.id.item_multiple_options:
                        intentAddQuestion = new Intent(
                                this, AddQuestionMO.class
                        );
                        break;
                }
                intentAddQuestion.putExtra("TESTID", currentTestId);
                startActivity(intentAddQuestion);
                return true;
            });
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
            List<QuestionMC> questionsMC = db.questionMCDAO().getFromTest(currentTestId);
            for (QuestionMC questionMC : questionsMC) {
                QuestionOptionMC[] questionOptionMCS = db.questionOptionMCDAO()
                        .getFromQuestionMC(questionMC.id).toArray(new QuestionOptionMC[0]);
                questionMC.questionOptionMCs = questionOptionMCS;
            }
            questions.addAll(questionsMC);
            List<QuestionMO> questionsMO = db.questionMODAO().getFromTest(currentTestId);
            for (QuestionMO questionMO : questionsMO) {
                QuestionOptionMO[] questionOptionMOS = db.questionOptionMODAO()
                        .getFromQuestionMO(questionMO.id).toArray(new QuestionOptionMO[0]);
                questionMO.questionOptionMOs = questionOptionMOS;
            }
            questions.addAll(questionsMO);
            Comparator<Question> questionsComparator =
                    new QuestionComparators.CompareByDateCreated();
            questionsComparator = Collections.reverseOrder(questionsComparator);
            Collections.sort(questions, questionsComparator);
            runOnUiThread(() -> adapterQuestionView.notifyDataSetChanged());
        }).start();
    }
}
