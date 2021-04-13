package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.AnswerQuestionPicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AnswerQuestionSimple extends AnswerQuestionActivity {

    private static final int ANSWER_SCORE_SEEKBAR_PARTITIONS = 10;

    private TextView txtQuestionTitle;
    private TextInputEditText txtResponse;
    private SeekBar seekbarAnswerScore;
    private TextView txtAnswerScore;
    private LinearLayout layoutAnswer;
    private Button btnViewOrHideAnswer;
    private TextView txtAnswer;

    private double maxQuestionScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_simple_question);

        Button btnPickQuestion = findViewById(R.id.btn_pick_question);
        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtResponse = findViewById(R.id.txt_response);
        seekbarAnswerScore = findViewById(R.id.seekbar_answer_score);
        txtAnswerScore = findViewById(R.id.txt_answer_score);
        layoutAnswer = findViewById(R.id.layout_answer);
        btnViewOrHideAnswer = findViewById(R.id.btn_view_or_hide_answer);
        txtAnswer = findViewById(R.id.txt_answer);
        TextView txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        TextView txtCurrentQuestionProgress = findViewById(R.id.txt_current_question_progress);
        super.btnNext = findViewById(R.id.btn_next);
        super.btnPrevious = findViewById(R.id.btn_previous);

        seekbarAnswerScore.setMax(ANSWER_SCORE_SEEKBAR_PARTITIONS);

        super.testRun = (TestRun) getIntent().getSerializableExtra("TESTRUN");

        super.styleQuestionProgressIndicators(
                txtNumberOfQuestionsSolved, txtCurrentQuestionProgress
        );

        super.db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        seekbarAnswerScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressInSeekbar, boolean fromUser) {
                double maxScoreInSeekbar = maxQuestionScore / ANSWER_SCORE_SEEKBAR_PARTITIONS;
                double scoreValueInText = seekBar.getProgress() * maxScoreInSeekbar;
                scoreValueInText = Math.round(scoreValueInText * 100.0) / 100.0;
                txtAnswerScore.setText(String.valueOf(scoreValueInText));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        btnViewOrHideAnswer.setOnClickListener(v -> switchAnswerView());

        QuestionSimpleResponse questionSimpleResponse =
                (QuestionSimpleResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        new Thread(() -> {
            QuestionSimple questionSimple =
                    db.questionSimpleDAO().getById(questionSimpleResponse.questionSimpleId);
            runOnUiThread(() -> {
                txtQuestionTitle.setText(questionSimple.title);
                txtAnswer.setText(questionSimple.answer);
                maxQuestionScore = questionSimple.score;
                txtResponse.setText(questionSimpleResponse.answered);
                double seekbarScoreProportion = ANSWER_SCORE_SEEKBAR_PARTITIONS / maxQuestionScore;
                int scoreInSeekbar = (int)(questionSimpleResponse.score * seekbarScoreProportion);
                seekbarAnswerScore.setProgress(scoreInSeekbar);

                super.setPreviousAndNextButtonsAndActions();
                super.setPickQuestionButtonAndAction(btnPickQuestion);
            });
        }).start();

    }

    private void switchAnswerView() {
        boolean isShownNow = layoutAnswer.getVisibility() == View.VISIBLE;
        if (isShownNow) {
            layoutAnswer.setVisibility(View.GONE);
            btnViewOrHideAnswer.setText(R.string.view_answer);
        }
        else {
            layoutAnswer.setVisibility(View.VISIBLE);
            btnViewOrHideAnswer.setText(R.string.hide_answer);
        }
    }


    @Override
    protected void setCurrentQuestionData() {
        QuestionSimpleResponse currentQuestionSimpleResponse =
                (QuestionSimpleResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        currentQuestionSimpleResponse.answered =
                Objects.requireNonNull(txtResponse.getText()).toString();
        currentQuestionSimpleResponse.score =
                Double.parseDouble(txtAnswerScore.getText().toString());
    }

}
