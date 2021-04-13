package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.customviews.TrueOrFalseSlider;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionTFResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

public class AnswerQuestionTF extends AnswerQuestionActivity {

    private static final int ANSWER_SCORE_SEEKBAR_PARTITIONS = 10;

    private Button btnPickQuestion;
    private TextView txtQuestionTitle;
    private TrueOrFalseSlider trueOrFalseSlider;
    private TextView txtAnswerStatus;
    private TextView txtNumberOfQuestionsSolved;
    private TextView txtCurrentQuestionProgress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_tf_question);

        btnPickQuestion = findViewById(R.id.btn_pick_question);
        txtQuestionTitle = findViewById(R.id.txt_question_title);
        trueOrFalseSlider = findViewById(R.id.true_or_false_slider);
        txtAnswerStatus = findViewById(R.id.txt_answer_status);
        txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        txtCurrentQuestionProgress = findViewById(R.id.txt_current_question_progress);
        super.btnNext = findViewById(R.id.btn_next);
        super.btnPrevious = findViewById(R.id.btn_previous);

        super.testRun = (TestRun) getIntent().getSerializableExtra("TESTRUN");

        super.styleQuestionProgressIndicators(
                txtNumberOfQuestionsSolved,
                txtCurrentQuestionProgress
        );

        super.db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        QuestionTFResponse questionTFResponse =
                (QuestionTFResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        new Thread(() -> {
            QuestionTF questionTF = db.questionTFDAO().getById(questionTFResponse.questionTFId);
            runOnUiThread(() -> {
                this.txtQuestionTitle.setText(questionTF.title);
            });
        }).start();

        trueOrFalseSlider.setSliderStatusChangedListener(() -> {
            switch(trueOrFalseSlider.getSliderStatus()) {
                case TRUE:
                    txtAnswerStatus.setText(R.string.is_true);
                    break;
                case FALSE:
                    txtAnswerStatus.setText(R.string.is_false);
                    break;
                case UNSET:
                    txtAnswerStatus.setText(R.string.unsetted);
                    break;
            }
        });

    }

    @Override
    protected void setCurrentQuestionData() {
        QuestionTFResponse currentResponse = (QuestionTFResponse)
                super.testRun.questionResponses[super.testRun.currentQuestionIndex];

    }


}
