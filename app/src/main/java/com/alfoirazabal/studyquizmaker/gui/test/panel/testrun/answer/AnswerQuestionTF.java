package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.customviews.TrueOrFalseSlider;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionTFResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

public class AnswerQuestionTF extends AnswerQuestionActivity {

    private Button btnPickQuestion;
    private TextView txtQuestionTitle;
    private TextView txtQuestionAnswer;
    private TrueOrFalseSlider trueOrFalseSlider;
    private TextView txtAnswerStatus;

    private QuestionTF currentQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_tf_question);

        btnPickQuestion = findViewById(R.id.btn_pick_question);
        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtQuestionAnswer = findViewById(R.id.txt_question_answer);
        trueOrFalseSlider = findViewById(R.id.true_or_false_slider);
        txtAnswerStatus = findViewById(R.id.txt_answer_status);
        TextView txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        TextView txtCurrentQuestionProgress = findViewById(R.id.txt_current_question_progress);
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
            currentQuestion = db.questionTFDAO().getById(questionTFResponse.questionTFId);
            runOnUiThread(() -> {
                this.txtQuestionTitle.setText(currentQuestion.title);
                if (questionTFResponse.askedTrueStatement) {
                    this.txtQuestionAnswer.setText(currentQuestion.answerTrue);
                }
                else {
                    this.txtQuestionAnswer.setText(currentQuestion.answerFalse);
                }
                if (questionTFResponse.isAnswered) {
                    boolean choseTrue = (
                            questionTFResponse.askedTrueStatement && questionTFResponse.answeredCorrectly ||
                            !questionTFResponse.askedTrueStatement && !questionTFResponse.answeredCorrectly
                    );
                    if (choseTrue) {
                        this.trueOrFalseSlider.setStatus(TrueOrFalseSlider.SLIDER_STATUS.TRUE);
                    }
                    else {
                        this.trueOrFalseSlider.setStatus(TrueOrFalseSlider.SLIDER_STATUS.FALSE);
                    }
                }
                else {
                    this.trueOrFalseSlider.setStatus(TrueOrFalseSlider.SLIDER_STATUS.UNSET);
                }
                super.setPreviousAndNextButtonsAndActions();
                setPickQuestionButtonAndAction(btnPickQuestion);
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
        currentResponse.isAnswered = trueOrFalseSlider.getSliderStatus() != TrueOrFalseSlider.SLIDER_STATUS.UNSET;
        if (currentResponse.isAnswered) {
            boolean isFalseAnsweredFalse = !currentResponse.askedTrueStatement && trueOrFalseSlider.getSliderStatus() == TrueOrFalseSlider.SLIDER_STATUS.FALSE;
            boolean isTrueAnsweredTrue = currentResponse.askedTrueStatement && trueOrFalseSlider.getSliderStatus() == TrueOrFalseSlider.SLIDER_STATUS.TRUE;
            currentResponse.answeredCorrectly = isFalseAnsweredFalse || isTrueAnsweredTrue;
            if (currentResponse.answeredCorrectly) {
                currentResponse.score = currentQuestion.score;
            }
            else {
                currentResponse.score = 0;
            }
        }

    }


}
