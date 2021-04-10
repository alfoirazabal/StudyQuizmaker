package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.ViewRunResults;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.Objects;

public class AnswerQuestionSimple extends AppCompatActivity {

    private enum NextQuestionDirection implements Serializable {
        FORWARD,
        BACKWARD
    }

    private static final int ANSWER_SCORE_SEEKBAR_PARTITIONS = 10;

    private TextView txtQuestionTitle;
    private TextInputEditText txtResponse;
    private SeekBar seekbarAnswerScore;
    private TextView txtAnswerScore;
    private LinearLayout layoutAnswer;
    private Button btnViewOrHideAnswer;
    private TextView txtAnswer;
    private Button btnNext;
    private Button btnPrevious;

    private AppDatabase db;

    private double maxQuestionScore;

    private TestRun testRun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        NextQuestionDirection nextQuestionDirection =
                (NextQuestionDirection) getIntent().getSerializableExtra("QUESTIONDIRECTION");
        if (nextQuestionDirection != null) {
            switch (nextQuestionDirection) {
                case FORWARD:
                    this.overridePendingTransition(
                            R.anim.slide_appear_right_to_left,
                            R.anim.slide_dissapear_right_to_left
                    );
                    break;
                case BACKWARD:
                    this.overridePendingTransition(
                            R.anim.slide_appear_left_to_right,
                            R.anim.slide_dissapear_left_to_right
                    );
                    break;
            }
        }

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
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);

        seekbarAnswerScore.setMax(ANSWER_SCORE_SEEKBAR_PARTITIONS);

        this.testRun = (TestRun) getIntent().getSerializableExtra("TESTRUN");

        int numberOfQuestions = this.testRun.questionSimpleResponses.length;
        int numberOfAnswers = 0;
        for (int i = 0 ; i < this.testRun.questionSimpleResponses.length ; i++) {
            if (!this.testRun.questionSimpleResponses[i].answered.equals("")) {
                numberOfAnswers++;
            }
        }

        String questionsSolvedIndicator = numberOfAnswers + "/" + numberOfQuestions;
        txtNumberOfQuestionsSolved.setText(questionsSolvedIndicator);
        String currentQuestionProgressIndicator = (this.testRun.currentQuestionIndex + 1) + "/" +
                this.testRun.questionSimpleResponses.length;
        txtCurrentQuestionProgress.setText(currentQuestionProgressIndicator);

        db = Room.databaseBuilder(
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

                setPreviousAndNextButtonsAndActions();
            });
        }).start();

        btnPickQuestion.setOnClickListener(v -> {
            setCurrentQuestionData();
            Intent intentPickQuestion = new Intent(
                    AnswerQuestionSimple.this,
                    AnswerQuestionSimplePicker.class
            );
            intentPickQuestion.putExtra("TESTRUN", testRun);
            intentPickQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentPickQuestion);
        });

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
                startNewAnswerQuestionSimpleActivity(NextQuestionDirection.FORWARD);
            });
        }
        else if (
                isLastQuestion
        ) {
            btnNext.setText(R.string.finish);
            btnNext.setBackgroundColor(Color.RED);
            btnNext.setOnClickListener(v -> setCurrentQuestionDataAndFinish());
            btnPrevious.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex--;
                startNewAnswerQuestionSimpleActivity(NextQuestionDirection.BACKWARD);
            });
        }
        else {
            btnNext.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex++;
                startNewAnswerQuestionSimpleActivity(NextQuestionDirection.FORWARD);
            });
            btnPrevious.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex--;
                startNewAnswerQuestionSimpleActivity(NextQuestionDirection.BACKWARD);
            });
        }
    }

    private void setCurrentQuestionDataAndFinish() {
        setCurrentQuestionData();
        new Thread(() -> {
            int numberOfAnsweredQuestions = 0;
            double totalScore = 0;
            double totalScored = 0;
            for (int i = 0 ; i < this.testRun.questionSimpleResponses.length ; i++) {
                QuestionSimpleResponse currentQuestionSimpleResponse =
                        this.testRun.questionSimpleResponses[i];
                if (currentQuestionSimpleResponse.isAnswered) {
                    numberOfAnsweredQuestions++;
                }
                totalScore += db.questionSimpleDAO().getById(
                        currentQuestionSimpleResponse.questionSimpleId
                ).score;
                totalScored += currentQuestionSimpleResponse.score;
            }
            this.testRun.numberOfAnsweredQuestions = numberOfAnsweredQuestions;
            this.testRun.scoredPercentage = Math.round((totalScored / totalScore) * 100);
            db.testRunDAO().insert(testRun);
            for (int i = 0 ; i < this.testRun.questionSimpleResponses.length ; i++) {
                db.questionSimpleResponseDAO().insert(this.testRun.questionSimpleResponses[i]);
            }
            Intent intentViewResults =
                    new Intent(AnswerQuestionSimple.this, ViewRunResults.class);
            intentViewResults.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intentViewResults.putExtra("TESTRUNID", testRun.id);
            this.startActivity(intentViewResults);
        }).start();
    }

    private void setCurrentQuestionData() {
        QuestionSimpleResponse currentQuestionSimpleResponse =
                this.testRun.questionSimpleResponses[this.testRun.currentQuestionIndex];
        currentQuestionSimpleResponse.answered =
                Objects.requireNonNull(txtResponse.getText()).toString();
        currentQuestionSimpleResponse.score =
                Double.parseDouble(txtAnswerScore.getText().toString());
        currentQuestionSimpleResponse.isAnswered = !this.txtResponse.getText().toString().equals("");
    }

    private void startNewAnswerQuestionSimpleActivity(NextQuestionDirection nextQuestionDirection) {
        Intent intentNewQuestion = new Intent(
                getApplicationContext(), AnswerQuestionSimple.class
        );
        intentNewQuestion.putExtra("TESTRUN", this.testRun);
        intentNewQuestion.putExtra("QUESTIONDIRECTION", nextQuestionDirection);
        intentNewQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        AnswerQuestionSimple.this.startActivity(intentNewQuestion);
    }

}
