package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionmo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMO;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionMO extends AppCompatActivity {

    private TextInputLayout txtilTitle;
    private TextInputEditText txtTitle;
    private TextInputLayout txtilAnswer;
    private TextInputEditText txtAnswer;
    private TextInputLayout txtilScore;
    private TextInputEditText txtScore;
    private Button btnAddOption;
    private RecyclerView recyclerviewOptions;
    private Button btnAdd;

    private AppDatabase db;

    private QuestionMO currentQuestionMO;
    private List<QuestionOptionMO> questionOptionMOs;

    private AdapterMOQuestion adapterMOQuestion;

    private List<String> questionTitles;
    private SearchInList searchInListTitles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_mo_add);

        txtilTitle = findViewById(R.id.txtil_title);
        txtTitle = findViewById(R.id.txt_title);
        txtilAnswer = findViewById(R.id.txtil_answer);
        txtAnswer = findViewById(R.id.txt_answer);
        txtilScore = findViewById(R.id.txtil_score);
        txtScore = findViewById(R.id.txt_score);
        btnAddOption = findViewById(R.id.btn_add_option);
        recyclerviewOptions = findViewById(R.id.recyclerview_options);
        btnAdd = findViewById(R.id.btn_add);

        Bundle bundle = getIntent().getExtras();
        String testId = bundle.getString("TESTID");

        this.currentQuestionMO = new QuestionMO();

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        this.questionOptionMOs = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapterMOQuestion = new AdapterMOQuestion(this.questionOptionMOs);
        recyclerviewOptions.setLayoutManager(layoutManager);
        recyclerviewOptions.setAdapter(this.adapterMOQuestion);

        new Thread(() -> {
            questionTitles = new ArrayList<>();
            questionTitles.addAll(db.questionSimpleDAO().getAllTitles(testId));
            questionTitles.addAll(db.questionTFDAO().getAllTitles(testId));
            questionTitles.addAll(db.questionMCDAO().getAllTitles(testId));
            questionTitles.addAll(db.questionMODAO().getAllTitles(testId));
            this.searchInListTitles = new SearchInList(questionTitles);
            runOnUiThread(() -> {
                txtilTitle.setEnabled(true);
                txtilAnswer.setEnabled(true);
                txtilScore.setEnabled(true);
                btnAdd.setEnabled(true);
                btnAddOption.setEnabled(true);
            });
        }).start();

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (searchInListTitles.containsStringIgnoreCase(s.toString())) {
                    btnAdd.setEnabled(false);
                    txtTitle.setError(getString(R.string.msg_err_question_title_exists_already));
                }
                else {
                    btnAdd.setEnabled(true);
                    txtTitle.setError(null);
                }
            }
        });

        btnAddOption.setOnClickListener(v -> {
            QuestionOptionMO questionOptionMO = new QuestionOptionMO();
            questionOptionMO.questionMOId = this.currentQuestionMO.id;
            questionOptionMO.answerText = this.txtAnswer.getText().toString();
            questionOptionMO.score = Double.parseDouble(this.txtScore.getText().toString());
            this.txtAnswer.setText("");
            this.questionOptionMOs.add(questionOptionMO);
            this.adapterMOQuestion.notifyDataSetChanged();
        });

        btnAdd.setOnClickListener(v -> {
            btnAdd.setEnabled(false);
            this.currentQuestionMO.testId = testId;
            this.currentQuestionMO.title = txtTitle.getText().toString();
            new Thread(() -> {
                db.questionMODAO().insert(this.currentQuestionMO);
                for (QuestionOptionMO questionOptionMO : this.questionOptionMOs) {
                    db.questionOptionMODAO().insert(questionOptionMO);
                }
                finish();
            }).start();
        });
    }
}
