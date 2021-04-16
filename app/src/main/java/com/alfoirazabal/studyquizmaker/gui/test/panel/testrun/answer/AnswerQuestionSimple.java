package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AnswerQuestionSimple extends AnswerQuestionActivity {

    private TextView txtQuestionTitle;
    private TextInputEditText txtResponse;
    private TextView txtQuestionScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_simple_question);

        Button btnPickQuestion = findViewById(R.id.btn_pick_question);
        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtResponse = findViewById(R.id.txt_response);
        txtQuestionScore = findViewById(R.id.txt_question_score);
        TextView txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        TextView txtCurrentQuestionProgress = findViewById(R.id.txt_current_question_progress);
        super.btnNext = findViewById(R.id.btn_next);
        super.btnPrevious = findViewById(R.id.btn_previous);

        super.testRun = (TestRun) getIntent().getSerializableExtra("TESTRUN");

        super.styleQuestionProgressIndicators(
                txtNumberOfQuestionsSolved, txtCurrentQuestionProgress
        );

        super.db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        QuestionSimpleResponse questionSimpleResponse =
                (QuestionSimpleResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        new Thread(() -> {
            QuestionSimple questionSimple =
                    db.questionSimpleDAO().getById(questionSimpleResponse.questionSimpleId);
            double maxQuestionScore = questionSimple.score;
            runOnUiThread(() -> {
                txtQuestionTitle.setText(questionSimple.title);
                txtResponse.setText(questionSimpleResponse.answered);
                txtQuestionScore.setText(String.valueOf(maxQuestionScore));
                super.setPreviousAndNextButtonsAndActions();
                super.setPickQuestionButtonAndAction(btnPickQuestion);
            });
        }).start();

    }


    @Override
    protected void setCurrentQuestionData() {
        QuestionSimpleResponse currentQuestionSimpleResponse =
                (QuestionSimpleResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        currentQuestionSimpleResponse.answered =
                Objects.requireNonNull(txtResponse.getText()).toString();
    }

}
