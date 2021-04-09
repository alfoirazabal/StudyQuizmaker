package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AnswerQuestionSimple extends AppCompatActivity {

    private static int ANSWER_SCORE_SEEKBAR_PARTITIONS = 10;

    private TextView txtQuestionTitle;
    private TextInputLayout txtilResponse;
    private TextInputEditText txtResponse;
    private SeekBar seekbarAnswerScore;
    private TextView txtAnswerScore;
    private LinearLayout layoutAnswer;
    private Button btnViewOrHideAnswer;
    private TextView txtAnswer;
    private TextView txtNumberOfQuestionsSolved;
    private Button btnNext;
    private Button btnPrevious;

    private AppDatabase db;

    private double maxQuestionScore;

    private TestRun testRun;
    
    private int numberOfAnswers;
    private int numberOfQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_simple_question);

        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtilResponse = findViewById(R.id.txtil_response);
        txtResponse = findViewById(R.id.txt_response);
        seekbarAnswerScore = findViewById(R.id.seekbar_answer_score);
        txtAnswerScore = findViewById(R.id.txt_answer_score);
        layoutAnswer = findViewById(R.id.layout_answer);
        btnViewOrHideAnswer = findViewById(R.id.btn_view_or_hide_answer);
        txtAnswer = findViewById(R.id.txt_answer);
        txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);

        seekbarAnswerScore.setMax(ANSWER_SCORE_SEEKBAR_PARTITIONS);

        this.testRun = (TestRun) getIntent().getSerializableExtra("TESTRUN");

        numberOfQuestions = this.testRun.questionSimpleResponses.length;
        numberOfAnswers = 0;
        for (int i = 0 ; i < this.testRun.questionSimpleResponses.length ; i++) {
            if (!this.testRun.questionSimpleResponses[i].answered.equals("")) {
                numberOfAnswers++;
            }
        }

        this.txtNumberOfQuestionsSolved.setText(numberOfAnswers + "/" + numberOfQuestions);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        QuestionSimpleResponse questionSimpleResponse =
                this.testRun.questionSimpleResponses[this.testRun.currentQuestionIndex];
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
            });
        }).start();

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

        setPreviousAndNextButtonsAndActions();

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

    private void setPreviousAndNextButtonsAndActions() {
        boolean isFirstQuestion = this.testRun.currentQuestionIndex == 0;
        boolean isLastQuestion =
                this.testRun.currentQuestionIndex == this.testRun.questionSimpleResponses.length - 1;

        if (isFirstQuestion) {
            btnPrevious.setText(R.string.exit);
            btnPrevious.setBackgroundColor(Color.RED);
            btnPrevious.setOnClickListener(v -> finish());
            btnNext.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex++;
                startNewAnswerQuestionSimpleActivity();
            });
        }
        else if (
                isLastQuestion
        ) {
            btnNext.setText(R.string.finish);
            btnNext.setBackgroundColor(Color.RED);
            btnNext.setOnClickListener(v -> {
                setCurrentQuestionDataAndFinish();
            });
            btnPrevious.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex--;
                startNewAnswerQuestionSimpleActivity();
            });
        }
        else {
            btnNext.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex++;
                startNewAnswerQuestionSimpleActivity();
            });
            btnPrevious.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex--;
                startNewAnswerQuestionSimpleActivity();
            });
        }
    }

    private void setCurrentQuestionDataAndFinish() {
        setCurrentQuestionData();
        new Thread(() -> {
            db.testRunDAO().insert(testRun);
            for (int i = 0 ; i < this.testRun.questionSimpleResponses.length ; i++) {
                db.questionSimpleResponseDAO().insert(this.testRun.questionSimpleResponses[i]);
            }
            finish();
        }).start();
    }

    private void setCurrentQuestionData() {
        QuestionSimpleResponse currentQuestionSimpleResponse =
                this.testRun.questionSimpleResponses[this.testRun.currentQuestionIndex];
        currentQuestionSimpleResponse.answered = txtResponse.getText().toString();
        currentQuestionSimpleResponse.score =
                Double.parseDouble(txtAnswerScore.getText().toString());
    }

    private void startNewAnswerQuestionSimpleActivity() {
        Intent intentNewQuestion = new Intent(
                getApplicationContext(), AnswerQuestionSimple.class
        );
        intentNewQuestion.putExtra("TESTRUN", this.testRun);
        intentNewQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        AnswerQuestionSimple.this.startActivity(intentNewQuestion);
    }

}
