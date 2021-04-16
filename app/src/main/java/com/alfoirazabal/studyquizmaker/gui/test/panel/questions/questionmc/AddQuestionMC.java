package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionmc;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddQuestionMC extends AppCompatActivity {

    private double DEFAULT_QUESTION_OPTION_SCORE = 1;

    private TextInputLayout txtilTitle;
    private TextInputEditText txtTitle;
    private TextInputLayout txtilAnswer;
    private TextInputEditText txtAnswer;
    private TextInputLayout txtilScore;
    private TextInputEditText txtScore;
    private Button btnAdd;
    private Button btnAddOption;
    private RecyclerView recyclerviewOptions;

    private AppDatabase db;

    private SearchInList searchInListQuestionTitles;

    private String currentTestId;

    private List<QuestionOptionMC> questionOptionsMC;

    private QuestionMC currentQuestionMC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_mc_add);

        txtilTitle = findViewById(R.id.txtil_title);
        txtTitle = findViewById(R.id.txt_title);
        txtilAnswer = findViewById(R.id.txtil_answer);
        txtAnswer = findViewById(R.id.txt_answer);
        txtilScore = findViewById(R.id.txtil_score);
        txtScore = findViewById(R.id.txt_score);
        btnAdd = findViewById(R.id.btn_add);
        btnAddOption = findViewById(R.id.btn_add_option);
        recyclerviewOptions = findViewById(R.id.recyclerview_options);

        txtScore.setText(String.valueOf(DEFAULT_QUESTION_OPTION_SCORE));

        currentQuestionMC = new QuestionMC();

        questionOptionsMC = new ArrayList<>();

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        Bundle bundle = getIntent().getExtras();
        currentTestId = bundle.getString("TESTID");
        currentQuestionMC.testId = currentTestId;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        AdapterMCQuestion adapterMCQuestion = new AdapterMCQuestion(questionOptionsMC);
        recyclerviewOptions.setLayoutManager(layoutManager);
        recyclerviewOptions.setAdapter(adapterMCQuestion);

        new Thread(() -> {
            List<String> questionTitles = db.questionMCDAO().getAllTitles(currentTestId);
            questionTitles.addAll(db.questionTFDAO().getAllTitles(currentTestId));
            questionTitles.addAll(db.questionSimpleDAO().getAllTitles(currentTestId));
            searchInListQuestionTitles = new SearchInList(questionTitles);
            runOnUiThread(() -> {
                txtilTitle.setEnabled(true);
                txtilAnswer.setEnabled(true);
                txtilScore.setEnabled(true);
                btnAddOption.setEnabled(true);
                btnAdd.setEnabled(true);
            });
        }).start();

        txtilTitle.setOnClickListener(v -> {
            if (searchInListQuestionTitles.containsStringIgnoreCase(txtTitle.getText().toString())) {
                txtTitle.setError(getString(R.string.msg_err_question_title_exists_already));
                btnAdd.setEnabled(false);
            }
            else {
                txtTitle.setError(null);
                btnAdd.setEnabled(true);
            }
        });

        btnAddOption.setOnClickListener(v -> {
            hideSoftInputKeyboard();
            QuestionOptionMC questionOptionMC = new QuestionOptionMC();
            questionOptionMC.score = Double.parseDouble(txtScore.getText().toString());
            questionOptionMC.answerText = txtAnswer.getText().toString();
            questionOptionMC.questionMCId = currentQuestionMC.id;
            txtAnswer.setText("");
            questionOptionsMC.add(questionOptionMC);
            Collections.sort(questionOptionsMC);
            adapterMCQuestion.notifyDataSetChanged();
        });

        btnAdd.setOnClickListener(v -> {
            btnAdd.setEnabled(false);
            currentQuestionMC.title = txtTitle.getText().toString();
            new Thread(() -> {
                db.questionMCDAO().insert(currentQuestionMC);
                for (QuestionOptionMC questionOptionMC : questionOptionsMC) {
                    db.questionOptionMCDAO().insert(questionOptionMC);
                }
                finish();
            }).start();
        });

    }

    private void hideSoftInputKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                InputMethodManager imm =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
