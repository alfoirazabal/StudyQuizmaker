package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple;

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
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.UpdateGUIClass;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.alfoirazabal.studyquizmaker.helpers.questions.MaxScoresProcessor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class UpdateQuestionSimple extends AppCompatActivity implements UpdateGUIClass {
    private static final int DISCRETE_SEEKBAR_PARTITIONS = 10;

    private TextInputLayout txtilTitle;
    private TextInputLayout txtilAnswer;
    private TextInputLayout txtilScore;
    private TextInputEditText txtTitle;
    private TextInputEditText txtAnswer;
    private TextInputEditText txtScore;
    private SeekBar seekbarScore;
    private TextView txtMaxScore;
    private Button btnUpdate;

    private AppDatabase db;

    private String currentTestId;
    private String currentSimpleQuestionId;

    private QuestionSimple currentQuestionSimple;

    private SearchInList searchInList;

    private double maxScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_simple_edit);

        txtilTitle = findViewById(R.id.txtil_title);
        txtilAnswer = findViewById(R.id.txtil_answer);
        txtilScore = findViewById(R.id.txtil_score);
        txtTitle = findViewById(R.id.txt_title);
        txtAnswer = findViewById(R.id.txt_answer);
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
            currentTestId = bundle.getString("TESTID");
            currentSimpleQuestionId = bundle.getString("QUESTIONID");
            currentQuestionSimple = db.questionSimpleDAO().getById(currentSimpleQuestionId);
            List<String> questionSimpleTitles = db.questionSimpleDAO().getAllTitles(currentTestId);
            searchInList = new SearchInList(questionSimpleTitles);
            searchInList.deleteIgnoreCase(currentQuestionSimple.title);
            MaxScoresProcessor maxScoresProcessor = new MaxScoresProcessor(db, currentTestId);
            maxScore = maxScoresProcessor.getMaxScoreFromAllQuestions();
            runOnUiThread(() -> {
                txtMaxScore.setText(String.valueOf(maxScore));
                txtilTitle.setEnabled(true);
                txtilAnswer.setEnabled(true);
                txtilScore.setEnabled(true);
                btnUpdate.setEnabled(true);
                txtTitle.setText(currentQuestionSimple.title);
                txtAnswer.setText(currentQuestionSimple.answer);
                txtScore.setText(String.valueOf(currentQuestionSimple.score));
                seekbarScore.setMax(DISCRETE_SEEKBAR_PARTITIONS);
                setValueOnScoreSeekbar(currentQuestionSimple.score);
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
                            getString(R.string.msg_err_questionsimple_title_exists_already)
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
            currentQuestionSimple.title = Objects.requireNonNull(txtTitle.getText()).toString();
            currentQuestionSimple.answer = Objects.requireNonNull(txtAnswer.getText()).toString();
            currentQuestionSimple.score =
                    Double.parseDouble(Objects.requireNonNull(txtScore.getText()).toString());
            new Thread(() -> {
                db.questionSimpleDAO().update(currentQuestionSimple);
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
