package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.AnswerQuestionPicker;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.ViewFinalResults;
import com.alfoirazabal.studyquizmaker.helpers.testrun.TestRunProcessor;

import java.io.Serializable;

public abstract class AnswerQuestionActivity extends AppCompatActivity {

    protected enum NextQuestionDirection implements Serializable {
        FORWARD,
        BACKWARD
    }

    protected Button btnNext;
    protected Button btnPrevious;

    protected TestRun testRun;

    protected AppDatabase db;

    protected abstract void setCurrentQuestionData();

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
    }

    protected void setPreviousAndNextButtonsAndActions() {
        boolean isFirstQuestion = this.testRun.currentQuestionIndex == 0;
        boolean isLastQuestion =
                this.testRun.currentQuestionIndex == this.testRun.questionResponses.length - 1;

        if (isFirstQuestion) {
            btnPrevious.setEnabled(false);
            btnNext.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex++;
                startNewAnswerQuestionSimpleActivity(AnswerQuestionSimple.NextQuestionDirection.FORWARD);
            });
        }
        if (isLastQuestion) {
            btnNext.setText(R.string.finish);
            btnNext.setBackgroundColor(Color.RED);
            btnNext.setOnClickListener(v -> setCurrentQuestionDataAndFinish());
            btnPrevious.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex--;
                startNewAnswerQuestionSimpleActivity(AnswerQuestionSimple.NextQuestionDirection.BACKWARD);
            });
        }
        if (!isFirstQuestion && !isLastQuestion) {
            btnNext.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex++;
                startNewAnswerQuestionSimpleActivity(AnswerQuestionSimple.NextQuestionDirection.FORWARD);
            });
            btnPrevious.setOnClickListener(v -> {
                setCurrentQuestionData();
                this.testRun.currentQuestionIndex--;
                startNewAnswerQuestionSimpleActivity(AnswerQuestionSimple.NextQuestionDirection.BACKWARD);
            });
        }
    }

    private void setCurrentQuestionDataAndFinish() {
        setCurrentQuestionData();
        if (this.testRun.hasSimpleQuestions()) {
            Intent intentScoreSimpleQuestions =
                    new Intent(this, ScoreSimpleQuestions.class);
            intentScoreSimpleQuestions.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intentScoreSimpleQuestions.putExtra("TESTRUN", this.testRun);
            this.startActivity(intentScoreSimpleQuestions);
        }
        else {
            TestRunProcessor testRunProcessor = new TestRunProcessor(this.testRun);
            new Thread(() -> {
                testRunProcessor.saveTestRunToDatabase(db);
                Intent intentViewResults =
                        new Intent(this, ViewFinalResults.class);
                intentViewResults.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intentViewResults.putExtra("TESTRUNID", testRun.id);
                this.startActivity(intentViewResults);
            }).start();
        }
    }

    private void startNewAnswerQuestionSimpleActivity(NextQuestionDirection nextQuestionDirection) {
        Intent intentNewQuestion = new Intent(
                getApplicationContext(), this.testRun.questionResponses[this.testRun.currentQuestionIndex].getAnswerQuestionClass()
        );
        intentNewQuestion.putExtra("TESTRUN", this.testRun);
        intentNewQuestion.putExtra("QUESTIONDIRECTION", nextQuestionDirection);
        intentNewQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(intentNewQuestion);
    }

    protected void styleQuestionProgressIndicators(
            TextView txtNumberOfQuestionsSolved,
            TextView txtCurrentQuestionProgress
    ) {
        int numberOfQuestions = this.testRun.questionResponses.length;
        int numberOfAnswers = 0;
        for (int i = 0; i < this.testRun.questionResponses.length ; i++) {
            if (this.testRun.questionResponses[i].isAnswered()) {
                numberOfAnswers++;
            }
        }
        String questionsSolvedIndicator = numberOfAnswers + "/" + numberOfQuestions;
        txtNumberOfQuestionsSolved.setText(questionsSolvedIndicator);
        String currentQuestionProgressIndicator = (this.testRun.currentQuestionIndex + 1) + "/" +
                this.testRun.questionResponses.length;
        txtCurrentQuestionProgress.setText(currentQuestionProgressIndicator);
    }

    protected void setPickQuestionButtonAndAction(Button btnPickQuestion) {
        btnPickQuestion.setOnClickListener(v -> {
            setCurrentQuestionData();
            Intent intentPickQuestion = new Intent(
                    this,
                    AnswerQuestionPicker.class
            );
            intentPickQuestion.putExtra("TESTRUN", this.testRun);
            intentPickQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentPickQuestion);
        });
    }

}
