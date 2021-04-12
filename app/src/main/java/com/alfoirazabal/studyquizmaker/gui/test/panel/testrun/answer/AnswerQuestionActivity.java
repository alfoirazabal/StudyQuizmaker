package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
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
                this.testRun.currentQuestionIndex == this.testRun.questionSimpleResponses.length - 1;

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

    private void startNewAnswerQuestionSimpleActivity(NextQuestionDirection nextQuestionDirection) {
        Intent intentNewQuestion = new Intent(
                getApplicationContext(), AnswerQuestionSimple.class
        );
        intentNewQuestion.putExtra("TESTRUN", this.testRun);
        intentNewQuestion.putExtra("QUESTIONDIRECTION", nextQuestionDirection);
        intentNewQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(intentNewQuestion);
    }

}
