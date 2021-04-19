package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMO;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponseOption;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.guiextensions.MOCheckBox;

public class AnswerQuestionMO extends AnswerQuestionActivity {

    private TextView txtQuestionTitle;
    private TextView txtQuestionScore;
    private Button btnPickQuestion;

    private QuestionMO currentQuestionMO;

    private LinearLayout layoutCheckboxOptions;
    private MOCheckBox[] moCheckBoxes;

    private QuestionMOResponse currentQuestionMOResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_question_mo);

        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtQuestionScore = findViewById(R.id.txt_question_score);
        TextView txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        TextView txtCurrentQuestionProgress = findViewById(R.id.txt_current_question_progress);
        btnPickQuestion = findViewById(R.id.btn_pick_question);

        layoutCheckboxOptions = findViewById(R.id.layout_checkbox_options);

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

        currentQuestionMOResponse =
                (QuestionMOResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        setCheckBoxes(currentQuestionMOResponse.questionOptionMOs);
        new Thread(() -> {
            currentQuestionMO =
                    db.questionMODAO().getById(currentQuestionMOResponse.questionMOId);
            currentQuestionMO.questionOptionMOs = currentQuestionMOResponse.questionOptionMOs;
            double maxQuestionScore = currentQuestionMO.getScore();
            runOnUiThread(() -> {
                setCheckedCheckBoxesOptions(currentQuestionMOResponse.questionMOResponseOptions);
                txtQuestionTitle.setText(currentQuestionMO.title);
                txtQuestionScore.setText(String.valueOf(maxQuestionScore));
                super.setPreviousAndNextButtonsAndActions();
                super.setPickQuestionButtonAndAction(btnPickQuestion);
            });
        }).start();
    }

    private void setCheckBoxes(QuestionOptionMO[] questionOptionMOs) {
        moCheckBoxes = new MOCheckBox[questionOptionMOs.length];
        for (int i = 0 ; i < questionOptionMOs.length ; i++) {
            MOCheckBox chkQuestionOption = new MOCheckBox(this);
            chkQuestionOption.setQuestionOptionMO(questionOptionMOs[i]);
            layoutCheckboxOptions.addView(chkQuestionOption);
            moCheckBoxes[i] = chkQuestionOption;
        }
    }

    private void setCheckedCheckBoxesOptions(QuestionMOResponseOption[] questionMOResponseOptions) {
        for (int i = 0 ; i < questionMOResponseOptions.length ; i++) {
            if (questionMOResponseOptions[i].optionSelected) {
                moCheckBoxes[i].setChecked(true);
            }
        }
    }

    @Override
    protected void setCurrentQuestionData() {
        for (int i = 0 ; i < currentQuestionMOResponse.questionMOResponseOptions.length ; i++) {
            currentQuestionMOResponse.questionMOResponseOptions[i].optionSelected =
                    moCheckBoxes[i].isChecked();
        }
    }
}
