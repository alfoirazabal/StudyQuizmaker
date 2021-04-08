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
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.ViewQuestionsSimple;

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

        getSupportActionBar().setSubtitle(R.string.questions);

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
