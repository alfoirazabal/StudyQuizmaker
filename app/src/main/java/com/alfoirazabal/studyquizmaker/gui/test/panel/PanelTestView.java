package com.alfoirazabal.studyquizmaker.gui.test.panel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import com.alfoirazabal.studyquizmaker.domain.question.Question;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.ViewQuestion;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.ViewTestRuns;
import com.alfoirazabal.studyquizmaker.helpers.ArrayShuffler;

import java.util.ArrayList;
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
            Intent intentViewQuestions =
                    new Intent(getApplicationContext(), ViewQuestion.class);
            intentViewQuestions.putExtra("TESTID", testId);
            startActivity(intentViewQuestions);
        });

        btnStart.setOnClickListener(v -> new Thread(() -> {
            List<Question> questions = new ArrayList<>();
            questions.addAll(db.questionSimpleDAO().getFromTest(testId));
            questions.addAll(db.questionMCDAO().getFromTest(testId));
            questions.addAll(db.questionTFDAO().getFromTest(testId));
            if (questions.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(
                        getApplicationContext(),
                        R.string.msg_err_need_to_add_questions_first,
                        Toast.LENGTH_LONG
                ).show());
            }
            else {
                TestRun testRun = new TestRun();
                testRun.testId = testId;
                testRun.questionResponses = new QuestionResponse[questions.size()];
                for (int i = 0 ; i < questions.size() ; i++) {
                    Question currentQuestion = questions.get(i);
                    testRun.questionResponses[i] = currentQuestion.getQuestionResponseObject();
                    testRun.questionResponses[i].setTestRunId(testRun.id);
                    testRun.questionResponses[i].setQuestionId(currentQuestion.getId());
                }
                ArrayShuffler<QuestionResponse> shuffleQuestionResponse = new ArrayShuffler<>();
                shuffleQuestionResponse.shuffleFisherYates(testRun.questionResponses);
                testRun.numberOfTotalQuestions = testRun.questionResponses.length;
                Intent intentAnswerQuestion = new Intent(
                        getApplicationContext(),
                        testRun.questionResponses[0].getAnswerQuestionClass()
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
