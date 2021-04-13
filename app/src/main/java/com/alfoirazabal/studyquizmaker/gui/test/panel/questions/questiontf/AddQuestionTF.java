package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questiontf;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.alfoirazabal.studyquizmaker.helpers.questions.MaxScoresProcessor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class AddQuestionTF extends AppCompatActivity {

    private static final double DEFAULT_MAX_SCORE_IF_NONE_FOUND = 1;
    private static final double DEFAULT_SCORE = 1;
    private static final int DISCRETE_SEEKBAR_PARTITIONS = 10;

    private TextInputLayout txtilTitle;
    private TextInputLayout txtilAnswerTrue;
    private TextInputLayout txtilAnswerFalse;
    private TextInputEditText txtTitle;
    private TextInputEditText txtAnswerTrue;
    private TextInputEditText txtAnswerFalse;
    private TextInputLayout txtilScore;
    private TextInputEditText txtScore;
    private SeekBar seekbarScore;
    private TextView txtMaxScore;
    private Button btnAdd;

    private AppDatabase db;

    private SearchInList searchInList;

    private double maxScore;

    private String testId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_tf_add);

        txtilTitle = findViewById(R.id.txtil_title);
        txtilAnswerTrue = findViewById(R.id.txtil_answer_true);
        txtilAnswerFalse = findViewById(R.id.txtil_answer_false);
        txtTitle = findViewById(R.id.txt_title);
        txtAnswerTrue = findViewById(R.id.txt_answer_true);
        txtAnswerFalse = findViewById(R.id.txt_answer_false);
        txtilScore = findViewById(R.id.txtil_score);
        txtScore = findViewById(R.id.txt_score);
        seekbarScore = findViewById(R.id.seekbar_score);
        txtMaxScore = findViewById(R.id.txt_max_score);
        btnAdd = findViewById(R.id.btn_add);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            testId = bundle.getString("TESTID");
            List<String> questionTitles = db.questionTFDAO().getAllTitles(testId);
            questionTitles.addAll(db.questionMCDAO().getAllTitles(testId));
            questionTitles.addAll(db.questionSimpleDAO().getAllTitles(testId));
            searchInList = new SearchInList(questionTitles);
            MaxScoresProcessor maxScoresProcessor = new MaxScoresProcessor(db, testId);
            maxScore = maxScoresProcessor.getMaxScoreFromAllQuestions();
            if (maxScore == 0) maxScore = DEFAULT_MAX_SCORE_IF_NONE_FOUND;
            runOnUiThread(() -> {
                txtMaxScore.setText(String.valueOf(maxScore));
                txtilTitle.setEnabled(true);
                txtilScore.setEnabled(true);
                txtilAnswerTrue.setEnabled(true);
                txtilAnswerFalse.setEnabled(true);
                btnAdd.setEnabled(true);
                seekbarScore.setMax(DISCRETE_SEEKBAR_PARTITIONS);
                setValueOnScoreSeekbar(DEFAULT_SCORE);
                txtScore.setText(String.valueOf(DEFAULT_SCORE));
            });
        }).start();

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString();
                if (searchInList.containsStringIgnoreCase(currentText)) {
                    txtTitle.setError(
                            getString(R.string.msg_err_question_title_exists_already)
                    );
                    btnAdd.setEnabled(false);
                }
                else {
                    txtTitle.setError(null);
                    btnAdd.setEnabled(true);
                }
            }
        });

        seekbarScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressInBar, boolean fromUser) {
                double progressInScore =
                        ((double)progressInBar / (double)DISCRETE_SEEKBAR_PARTITIONS) * maxScore;
                progressInScore = Math.round(progressInScore * 100.0) / 100.0;
                if (fromUser) {
                    txtScore.setText(String.valueOf(progressInScore));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        txtScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double value = Double.parseDouble(s.toString());
                    setValueOnScoreSeekbar(value);
                }
                catch (NumberFormatException ignored) { }
            }
        });

        btnAdd.setOnClickListener(v -> {
            QuestionTF questionTF = new QuestionTF();
            questionTF.testId = testId;
            questionTF.title = txtTitle.getText().toString();
            questionTF.answerTrue = txtAnswerTrue.getText().toString();
            questionTF.answerFalse = txtAnswerFalse.getText().toString();
            questionTF.score =
                    Double.parseDouble(Objects.requireNonNull(txtScore.getText()).toString());
            new Thread(() -> {
                db.questionTFDAO().insert(questionTF);
                finish();
            }).start();
        });
    }

    private void setValueOnScoreSeekbar(double value) {
        int progressInSeekBar =
                (int)(value * (double)DISCRETE_SEEKBAR_PARTITIONS / maxScore);
        seekbarScore.setProgress(progressInSeekBar);
    }
}
