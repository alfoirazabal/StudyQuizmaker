package com.alfoirazabal.studyquizmaker.gui.test.panel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.AnswerQuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.ViewQuestionsSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.ViewTestRuns;

import java.util.List;
import java.util.Objects;

public class PanelTestView extends AppCompatActivity {

    private Button btnStart;
    private TextView txtAmountOfQuestions;
    private TextView txtAmountOfSimpleQuestions;
    private TextView txtAmountOfMCQuestions;
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

        Objects.requireNonNull(getSupportActionBar()).setSubtitle(R.string.questions);

        btnStart = findViewById(R.id.btn_start);
        txtAmountOfQuestions = findViewById(R.id.txt_amount_of_questions);
        txtAmountOfSimpleQuestions = findViewById(R.id.txt_amount_of_simple_questions);
        txtAmountOfMCQuestions =
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

        Bundle bundle = getIntent().getExtras();
        testId = bundle.getString("TESTID");

        new Thread(() -> {
            Test currentTest = db.testDAO().getById(testId);
            Topic currentTopic = db.topicDAO().getById(currentTest.topicId);
            Subject currentSubject = db.subjectDAO().getById(currentTopic.subjectId);
            runOnUiThread(() -> {
                setTitle(currentTest.name);
                txtTopic.setText(currentTopic.name);
                txtSubject.setText(currentSubject.name);
                btnStart.setEnabled(true);
                txtbtnViewScores.setEnabled(true);
                txtbtnManageQuestions.setEnabled(true);
            });
        }).start();

        txtbtnManageQuestions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
            popupMenu.inflate(R.menu.menu_test_panel_manage_questions);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                boolean resolved = true;
                int menuItemId = menuItem.getItemId();
                if (menuItemId == R.id.item_simple_questions) {
                    Intent intentViewSimpleQuestions =
                            new Intent(getApplicationContext(), ViewQuestionsSimple.class);
                    intentViewSimpleQuestions.putExtra("TESTID", testId);
                    startActivity(intentViewSimpleQuestions);
                }
                else if (menuItemId == R.id.item_multiple_choice) {
                    // TODO
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.msg_available_in_future_version,
                            Toast.LENGTH_LONG
                    ).show();
                }
                else if (menuItemId == R.id.item_true_or_false) {
                    // TODO
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.msg_available_in_future_version,
                            Toast.LENGTH_LONG
                    ).show();
                }
                else {
                    resolved = false;
                }
                return resolved;
            });
        });

        btnStart.setOnClickListener(v -> new Thread(() -> {
            List<QuestionSimple> questionSimples = db.questionSimpleDAO().getFromTest(testId);
            if (questionSimples.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(
                        getApplicationContext(),
                        R.string.msg_err_need_to_add_questions_first,
                        Toast.LENGTH_LONG
                ).show());
            }
            else {
                TestRun testRun = new TestRun();
                testRun.testId = testId;
                testRun.questionSimpleResponses = new QuestionSimpleResponse[questionSimples.size()];
                for (int i = 0 ; i < questionSimples.size() ; i++) {
                    testRun.questionSimpleResponses[i] = new QuestionSimpleResponse();
                    testRun.questionSimpleResponses[i].testRunId = testRun.id;
                    testRun.questionSimpleResponses[i].questionSimpleId = questionSimples.get(i).id;
                }
                testRun.numberOfTotalQuestions = testRun.questionSimpleResponses.length;
                Intent intentAnswerQuestion = new Intent(
                        getApplicationContext(),
                        AnswerQuestionSimple.class
                );
                intentAnswerQuestion.putExtra("TESTRUN", testRun);
                intentAnswerQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                PanelTestView.this.startActivity(intentAnswerQuestion);
            }
        }).start());

        txtbtnViewScores.setOnClickListener(v -> {
            Intent intentViewTestRuns = new Intent(
                    PanelTestView.this, ViewTestRuns.class
            );
            intentViewTestRuns.putExtra("TESTID", testId);
            startActivity(intentViewTestRuns);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            int amountOfQuestionsMC = db.questionMCDAO().getCountFromTest(testId);
            int amountOfQuestionsSimple = db.questionSimpleDAO().getCountFromTest(testId);
            int amountOfQuestionsTF = db.questionTFDAO().getCountFromTest(testId);
            int totalAmountOfQuestions = amountOfQuestionsMC + amountOfQuestionsSimple +
                    amountOfQuestionsTF;
            runOnUiThread(() -> {
                txtAmountOfMCQuestions.setText(String.valueOf(amountOfQuestionsMC));
                txtAmountOfSimpleQuestions.setText(String.valueOf(amountOfQuestionsSimple));
                txtAmountOfTrueOrFalseQuestions.setText(String.valueOf(amountOfQuestionsTF));
                txtAmountOfQuestions.setText(String.valueOf(totalAmountOfQuestions));
            });
        }).start();
    }
}
