package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.questions;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionTFResponse;

import java.util.Objects;

public class ViewQuestionTFResponse extends AppCompatActivity {

    private TextView txtQuestionTitle;
    private TextView txtScore;
    private TextView txtAnswered;
    private TextView txtRightAnswer;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_result_question_tf);

        Objects.requireNonNull(getSupportActionBar()).setSubtitle(R.string.response_question_tf);

        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtScore = findViewById(R.id.txt_score);
        txtAnswered = findViewById(R.id.txt_answered);
        txtRightAnswer = findViewById(R.id.txt_right_answer);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        Bundle bundle = getIntent().getExtras();
        QuestionTFResponse questionResponse =
                (QuestionTFResponse) bundle.getSerializable("QUESTIONRESPONSE");

        new Thread(() -> {
            QuestionTF questionTF = db.questionTFDAO().getById(questionResponse.questionTFId);
            String testName = db.testDAO().getById(questionTF.testId).name;
            runOnUiThread(() -> {
                getSupportActionBar().setTitle(testName);
                txtQuestionTitle.setText(questionTF.title);
                String scoreText = questionResponse.score + "/" + questionTF.score;
                txtScore.setText(scoreText);
                if (!questionResponse.isAnswered) {
                    txtAnswered.setText(R.string.unanswered);
                    txtAnswered.setTextColor(Color.RED);
                    if (questionResponse.askedTrueStatement) {
                        txtRightAnswer.setText(R.string.is_true);
                    }
                    else {
                        txtRightAnswer.setText(R.string.is_false);
                    }
                }
                else {
                    if (questionResponse.answeredCorrectly) {
                        if (questionResponse.askedTrueStatement) {
                            txtAnswered.setText(R.string.is_true);
                            txtRightAnswer.setText(R.string.is_true);
                        }
                        else {
                            txtAnswered.setText(R.string.is_false);
                            txtRightAnswer.setText(R.string.is_false);
                        }
                    }
                    else {
                        if (questionResponse.askedTrueStatement) {
                            txtAnswered.setText(R.string.is_false);
                            txtRightAnswer.setText(R.string.is_true);
                        }
                        else {
                            txtAnswered.setText(R.string.is_true);
                            txtRightAnswer.setText(R.string.is_false);
                        }
                    }
                }
            });
        }).start();
    }
}
