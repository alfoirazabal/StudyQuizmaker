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
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;

public class ViewQuestionSingleResponse extends AppCompatActivity {

    private TextView txtQuestionTitle;
    private TextView txtScore;
    private TextView txtAnswered;
    private TextView txtRightAnswer;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_result_question_simple);

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
        QuestionSimpleResponse questionResponse =
                (QuestionSimpleResponse) bundle.getSerializable("QUESTIONRESPONSE");

        new Thread(() -> {
            QuestionSimple questionSimple =
                    db.questionSimpleDAO().getById(questionResponse.questionSimpleId);
            runOnUiThread(() -> {
                txtQuestionTitle.setText(questionSimple.title);
                String scoreText =questionResponse.score + "/" + questionSimple.score;
                txtScore.setText(scoreText);
                if (!questionResponse.isAnswered()) {
                    txtAnswered.setText(R.string.unanswered);
                    txtAnswered.setTextColor(Color.RED);
                }
                else {
                    txtAnswered.setText(questionResponse.answered);
                }
                txtRightAnswer.setText(questionSimple.answer);
            });
        }).start();
    }
}
