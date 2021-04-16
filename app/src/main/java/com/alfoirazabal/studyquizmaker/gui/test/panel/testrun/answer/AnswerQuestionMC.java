package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMCResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.guiextensions.MCRadioButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AnswerQuestionMC extends AnswerQuestionActivity {

    private TextView txtQuestionTitle;
    private TextView txtQuestionScore;
    private TextView txtNumberOfQuestionsSolved;
    private TextView txtCurrentQuestionProgress;
    private Button btnPickQuestion;
    private Button btnClearCheck;

    private QuestionMC currentQuestionMC;

    private RadioGroup rbtngroupMCQuestionOptions;

    private QuestionMCResponse currentQuestionMCResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_question_mc);

        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtQuestionScore = findViewById(R.id.txt_question_score);
        txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        txtCurrentQuestionProgress = findViewById(R.id.txt_current_question_progress);
        btnPickQuestion = findViewById(R.id.btn_pick_question);
        rbtngroupMCQuestionOptions = findViewById(R.id.rbtngroup_mc_question_options);
        rbtngroupMCQuestionOptions.setOrientation(RadioGroup.VERTICAL);
        btnClearCheck = findViewById(R.id.btn_clear_check);

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

        currentQuestionMCResponse =
                (QuestionMCResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        new Thread(() -> {
            setRadioButtons(currentQuestionMCResponse.questionOptionMCs);
            currentQuestionMC =
                    db.questionMCDAO().getById(currentQuestionMCResponse.questionMCId);
            currentQuestionMC.questionOptionMCs = currentQuestionMCResponse.questionOptionMCs;
            double maxQuestionScore = currentQuestionMC.getScore();
            runOnUiThread(() -> {
                setSelectedQuestionOption(currentQuestionMCResponse.questionMCOptionSelected);
                txtQuestionTitle.setText(currentQuestionMC.title);
                txtQuestionScore.setText(String.valueOf(maxQuestionScore));
                super.setPreviousAndNextButtonsAndActions();
                super.setPickQuestionButtonAndAction(btnPickQuestion);

            });
        }).start();

        btnClearCheck.setOnClickListener(v -> {
            rbtngroupMCQuestionOptions.clearCheck();
        });

    }

    private void setSelectedQuestionOption(String questionMCOptionSelectedId) {
        if (questionMCOptionSelectedId == null) {
            return;
        }
        int rbtnChildrenCount = rbtngroupMCQuestionOptions.getChildCount();
        boolean optionSelected = false;
        for (int i = 0 ; !optionSelected && i < rbtnChildrenCount ; i++) {
            MCRadioButton mcRadioButton = (MCRadioButton) rbtngroupMCQuestionOptions.getChildAt(i);
            if (mcRadioButton.getQuestionOptionMC().id.equals(questionMCOptionSelectedId)) {
                mcRadioButton.setChecked(true);
                optionSelected = true;
            }
        }
    }

    private void setRadioButtons(QuestionOptionMC[] questionOptionMCs) {
        for (int i = 0 ; i < questionOptionMCs.length ; i++) {
            MCRadioButton mcRadioButton = new MCRadioButton(this);
            int finalI = i;
            runOnUiThread(() -> {
                mcRadioButton.setQuestionOptionMC(questionOptionMCs[finalI]);
                rbtngroupMCQuestionOptions.addView(mcRadioButton);

            });
        }
    }

    private QuestionOptionMC findSelectedQuestionOption() {
        int checkedRadioButtonId = rbtngroupMCQuestionOptions.getCheckedRadioButtonId();
        MCRadioButton mcRadioButton = findViewById(checkedRadioButtonId);
        QuestionOptionMC selectedQuestionOption = null;
        if (mcRadioButton == null) {
            selectedQuestionOption = null;
        }
        else {
            selectedQuestionOption = mcRadioButton.getQuestionOptionMC();
        }
        return selectedQuestionOption;
    }

    @Override
    protected void setCurrentQuestionData() {
        QuestionOptionMC selectedOptionMC = findSelectedQuestionOption();
        currentQuestionMCResponse =
                (QuestionMCResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        if (selectedOptionMC == null) {
            currentQuestionMCResponse.questionMCOptionSelected = null;
            currentQuestionMCResponse.score = 0;
        }
        else {
            currentQuestionMCResponse.questionMCOptionSelected = selectedOptionMC.id;
            currentQuestionMCResponse.score = selectedOptionMC.score;
        }
    }

}
