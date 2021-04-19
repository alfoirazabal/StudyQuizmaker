package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.questions;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMO;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponseOption;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.guiextensions.MOCheckBox;

public class ViewQuestionMOResponse extends AppCompatActivity {

    private TextView txtQuestionTitle;
    private TextView txtScore;
    private LinearLayout layoutMOOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_result_question_mo);

        getSupportActionBar().setSubtitle(R.string.response_question_mo);

        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtScore = findViewById(R.id.txt_score);
        layoutMOOptions = findViewById(R.id.layout_mo_options);

        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        Bundle bundle = getIntent().getExtras();
        QuestionMOResponse questionMOResponse =
                (QuestionMOResponse) bundle.getSerializable("QUESTIONRESPONSE");

        Activity thisActivity = this;

        new Thread(() -> {
            QuestionMO questionMO = db.questionMODAO().getById(questionMOResponse.questionMOId);
            QuestionOptionMO[] questionOptionMOs = db.questionOptionMODAO()
                    .getFromQuestionMO(questionMO.id).toArray(new QuestionOptionMO[0]);
            QuestionMOResponseOption[] questionMOResponseOptions = db.questionMOResponseOptionDAO()
                    .getByQuestionMOResponse(questionMOResponse.id);
            runOnUiThread(() -> {
                txtQuestionTitle.setText(questionMO.title);
                questionMO.questionOptionMOs = questionOptionMOs;
                double scored = questionMOResponse.getScore();
                double questionTotalScore = questionMO.getScore();
                String scoreText = scored + "/" + questionTotalScore;
                txtScore.setText(scoreText);
                for (QuestionOptionMO questionOptionMO : questionOptionMOs) {
                    MOCheckBox moCheckBox = new MOCheckBox(thisActivity);
                    moCheckBox.setCanViewScore(true);
                    moCheckBox.setQuestionOptionMO(questionOptionMO);
                    layoutMOOptions.addView(moCheckBox);
                    if (questionOptionMO.score > 0) {
                        moCheckBox.setTextColor(Color.GREEN);
                    } else if (questionOptionMO.score < 0) {
                        moCheckBox.setTextColor(Color.RED);
                    }
                    for (QuestionMOResponseOption questionMOResponseOption : questionMOResponseOptions) {
                        if (questionMOResponseOption.questionOptionMOId.equals(questionOptionMO.id)) {
                            moCheckBox.setChecked(questionMOResponseOption.optionSelected);
                        }
                    }
                    moCheckBox.setOnCheckChangedListener((buttonView, isChecked) -> moCheckBox.setChecked(!isChecked));
                }
            });
        }).start();

    }
}
