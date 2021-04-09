package com.alfoirazabal.studyquizmaker.domain.testrun;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;

import java.util.Objects;

public class ViewRunResults extends AppCompatActivity {

    private TextView txtScored;
    private TextView txtQuestionsAnswered;
    private Button btnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_results);

        Objects.requireNonNull(getSupportActionBar()).setSubtitle(R.string.test_results);

        txtScored = findViewById(R.id.txt_scored);
        txtQuestionsAnswered = findViewById(R.id.txt_questions_answered);
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
                txtScored.setText(testRun.scoredPercentage + "%");
                txtQuestionsAnswered.setText(
                        testRun.numberOfAnsweredQuestions + "/" + testRun.numberOfTotalQuestions
                );
                btnOk.setEnabled(true);
            });
        }).start();

        btnOk.setOnClickListener(v -> finish());

    }
}
