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

public class UpdateQuestionTF extends AppCompatActivity {
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
    private Button btnUpdate;

    private AppDatabase db;

    private SearchInList searchInList;

    private QuestionTF currentQuestionTF;

    private double maxScore;

    private String testId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_tf_edit);

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
        btnUpdate = findViewById(R.id.btn_update);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            testId = bundle.getString("TESTID");
            String currentTFQuestionId = bundle.getString("QUESTIONID");
            List<String> questionTitles = db.questionTFDAO().getAllTitles(testId);
            questionTitles.addAll(db.questionMCDAO().getAllTitles(testId));
            questionTitles.addAll(db.questionSimpleDAO().getAllTitles(testId));
            MaxScoresProcessor maxScoresProcessor = new MaxScoresProcessor(db, testId);
            maxScore = maxScoresProcessor.getMaxScoreFromAllQuestions();
            currentQuestionTF = db.questionTFDAO().getById(currentTFQuestionId);
            searchInList = new SearchInList(questionTitles);
            searchInList.deleteIgnoreCase(currentQuestionTF.title);
            runOnUiThread(() -> {
                txtMaxScore.setText(String.valueOf(maxScore));
                txtilTitle.setEnabled(true);
                txtilAnswerTrue.setEnabled(true);
                txtilAnswerFalse.setEnabled(true);
                txtilScore.setEnabled(true);
                txtTitle.setText(currentQuestionTF.title);
                txtAnswerTrue.setText(currentQuestionTF.answerTrue);
                txtAnswerFalse.setText(currentQuestionTF.answerFalse);
                txtScore.setText(String.valueOf(currentQuestionTF.score));
                seekbarScore.setMax(DISCRETE_SEEKBAR_PARTITIONS);
                btnUpdate.setEnabled(true);
                setValueOnScoreSeekbar(currentQuestionTF.score);
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
                    btnUpdate.setEnabled(false);
                }
                else {
                    txtTitle.setError(null);
                    btnUpdate.setEnabled(true);
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

        btnUpdate.setOnClickListener(v -> {
            currentQuestionTF.title = Objects.requireNonNull(txtTitle.getText()).toString();
            currentQuestionTF.answerTrue =
                    Objects.requireNonNull(txtAnswerTrue.getText()).toString();
            currentQuestionTF.answerFalse =
                    Objects.requireNonNull(txtAnswerFalse.getText()).toString();
            currentQuestionTF.score =
                    Double.parseDouble(Objects.requireNonNull(txtScore.getText()).toString());
            currentQuestionTF.updateModifiedDate();
            new Thread(() -> {
                db.questionTFDAO().update(currentQuestionTF);
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
