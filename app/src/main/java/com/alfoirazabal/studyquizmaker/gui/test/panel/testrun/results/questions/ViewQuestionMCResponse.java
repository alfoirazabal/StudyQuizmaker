package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.questions;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMCResponse;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.guiextensions.MCRadioButton;

public class ViewQuestionMCResponse extends AppCompatActivity {

    private TextView txtQuestionTitle;
    private TextView txtScore;
    private RadioGroup rbtngroupQuestionOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_result_question_mc);

        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtScore = findViewById(R.id.txt_score);
        rbtngroupQuestionOptions = findViewById(R.id.rbtngroup_mc_options);

        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        Bundle bundle = getIntent().getExtras();
        QuestionMCResponse questionMCResponse =
                (QuestionMCResponse) bundle.getSerializable("QUESTIONRESPONSE");

        Activity thisActivity = this;

        new Thread(() -> {
            QuestionMC questionMC = db.questionMCDAO().getById(questionMCResponse.questionMCId);
            questionMC.questionOptionMCs = db.questionOptionMCDAO().getFromQuestionMC(questionMC.id)
                    .toArray(new QuestionOptionMC[0]);
            questionMCResponse.questionOptionMCs = questionMC.questionOptionMCs;
            runOnUiThread(() -> {
                txtQuestionTitle.setText(questionMC.title);
                String scoreText = questionMCResponse.getScore() + "/" + questionMC.getScore();
                txtScore.setText(scoreText);
                for (QuestionOptionMC questionOptionMC : questionMC.questionOptionMCs) {
                    MCRadioButton mcRadioButton = new MCRadioButton(thisActivity);
                    mcRadioButton.setQuestionOptionMC(questionOptionMC);
                    if (questionOptionMC.id.equals(questionMC.rightOption.id)) {
                        int colorGreen = Color.rgb(0, 201, 0);
                        mcRadioButton.setTextColor(colorGreen);
                    }
                    if (questionOptionMC.id.equals(questionMCResponse.questionMCOptionSelected)) {
                        mcRadioButton.setChecked(true);
                        if (!questionMCResponse.questionMCOptionSelected.equals(questionMC.rightOption.id)) {
                            if (questionOptionMC.score == 0) {
                                mcRadioButton.setTextColor(Color.RED);
                            }
                            else {
                                int colorOrange = Color.rgb(240, 157, 14);
                                mcRadioButton.setTextColor(colorOrange);
                            }
                        }
                    }
                    mcRadioButton.setOnCheckedChangeListener(
                            (buttonView, isChecked) -> mcRadioButton.setChecked(false)
                    );
                    rbtngroupQuestionOptions.addView(mcRadioButton);
                }
            });
        }).start();
    }
}
