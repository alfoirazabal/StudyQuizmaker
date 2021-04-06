package com.alfoirazabal.studyquizmaker.gui.test.panel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.Topic;

public class PanelTestView extends AppCompatActivity {

    private Button btnStart;
    private TextView txtAmountOfQuestions;
    private TextView txtAmountOfSimpleQuestions;
    private TextView txtAmountOfMultipleChoiceQuestions;
    private TextView txtAmountOfTrueOrFalseQuestions;
    private TextView txtTopic;
    private TextView txtSubject;
    private TextView txtbtnViewScores;
    private TextView txtbtnManageQuestions;

    private String testId;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_panel_view);

        getSupportActionBar().setSubtitle(R.string.questions);

        btnStart = findViewById(R.id.btn_start);
        txtAmountOfQuestions = findViewById(R.id.txt_amount_of_questions);
        txtAmountOfSimpleQuestions = findViewById(R.id.txt_amount_of_simple_questions);
        txtAmountOfMultipleChoiceQuestions =
                findViewById(R.id.txt_amount_of_multiple_choice_questions);
        txtAmountOfTrueOrFalseQuestions = findViewById(R.id.txt_amount_of_true_or_false_questions);
        txtTopic = findViewById(R.id.txt_topic);
        txtSubject = findViewById(R.id.txt_subject);
        txtbtnViewScores = findViewById(R.id.txtbtn_view_scores);
        txtbtnManageQuestions = findViewById(R.id.txtbtn_manage_questions);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            testId = bundle.getString("TESTID");
            Test currentTest = db.testDAO().getById(testId);
            Topic currentTopic = db.topicDAO().getById(currentTest.topicId);
            Subject currentSubject = db.subjectDAO().getById(currentTopic.subjectId);
            int amountOfQuestionsMC = db.questionMCDAO().getCountFromTest(testId);
            int amountOfQuestionsSimple = db.questionSimpleDAO().getCountFromTest(testId);
            int amountOfQuestionsTF = db.questionTFDAO().getCountFromTest(testId);
            int totalAmountOfQuestions = amountOfQuestionsMC + amountOfQuestionsSimple +
                    amountOfQuestionsTF;
            runOnUiThread(() -> {
                setTitle(currentTest.name);
                txtTopic.setText(currentTopic.name);
                txtSubject.setText(currentSubject.name);
                txtAmountOfQuestions.setText(String.valueOf(totalAmountOfQuestions));
                txtAmountOfSimpleQuestions.setText(String.valueOf(amountOfQuestionsSimple));
                txtAmountOfMultipleChoiceQuestions.setText(String.valueOf(amountOfQuestionsMC));
                txtAmountOfTrueOrFalseQuestions.setText(String.valueOf(amountOfQuestionsTF));
                btnStart.setEnabled(true);
                txtbtnViewScores.setEnabled(true);
                txtbtnManageQuestions.setEnabled(true);
            });
        }).start();
    }
}
