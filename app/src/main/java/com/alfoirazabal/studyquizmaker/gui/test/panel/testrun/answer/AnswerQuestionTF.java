package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.customviews.TrueOrFalseSlider;

public class AnswerQuestionTF extends AnswerQuestionActivity {

    private static final int ANSWER_SCORE_SEEKBAR_PARTITIONS = 10;

    private Button btnPickQuestion;
    private TextView txtQuestionTitle;
    private TrueOrFalseSlider trueOrFalseSlider;
    private TextView txtNumberOfQuestionsSolved;
    private TextView txtCurrentQuestionProgress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_tf_question);

        btnPickQuestion = findViewById(R.id.btn_pick_question);
        txtQuestionTitle = findViewById(R.id.txt_question_title);
        trueOrFalseSlider = findViewById(R.id.true_or_false_slider);
        txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        super.btnNext = findViewById(R.id.btn_next);
        super.btnPrevious = findViewById(R.id.btn_previous);
    }

    @Override
    protected void setCurrentQuestionData() {

    }


}
