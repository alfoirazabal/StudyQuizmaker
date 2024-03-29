package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.helpers.dates.DateTimeDifference;

import java.text.DateFormat;
import java.util.Objects;

public class ViewFinalResults extends AppCompatActivity {

    private TextView txtScored;
    private TextView txtQuestionsAnswered;
    private TextView txtStartedOn;
    private TextView txtDuration;
    private Button btnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_results);

        Objects.requireNonNull(getSupportActionBar()).setSubtitle(R.string.test_results);

        txtScored = findViewById(R.id.txt_scored);
        txtQuestionsAnswered = findViewById(R.id.txt_questions_answered);
        txtStartedOn = findViewById(R.id.txt_started_on);
        txtDuration = findViewById(R.id.txt_duration);
        btnOk = findViewById(R.id.btn_ok);

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            String testRunId = bundle.getString("TESTRUNID");
            AppDatabase db = Room.databaseBuilder(
                    getApplicationContext(),
                    AppDatabase.class,
                    AppConstants.getDBLocation(getApplicationContext())
            ).build();
            TestRun testRun = db.testRunDAO().getById(testRunId);
            runOnUiThread(() -> {
                String scoredPrecentageIndicator = testRun.scoredPercentage + "%";
                String answeredQuestionsIndicator =
                        testRun.numberOfAnsweredQuestions + "/" + testRun.numberOfTotalQuestions;
                txtScored.setText(scoredPrecentageIndicator);
                txtQuestionsAnswered.setText(answeredQuestionsIndicator);
                DateFormat dateFormat =
                        android.text.format.DateFormat.getDateFormat(getApplicationContext());
                txtStartedOn.setText(dateFormat.format(testRun.dateTimeStarted));
                DateTimeDifference dateTimeDifference = new DateTimeDifference(
                        testRun.dateTimeStarted,
                        testRun.dateTimeFinished
                );
                txtDuration.setText(dateTimeDifference.toString());
                btnOk.setEnabled(true);
            });
        }).start();

        btnOk.setOnClickListener(v -> finish());

    }
}
