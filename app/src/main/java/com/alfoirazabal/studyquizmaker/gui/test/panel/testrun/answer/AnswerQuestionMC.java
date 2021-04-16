package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMCResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.recyclerviews.AdapterMCQuestionResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AnswerQuestionMC extends AnswerQuestionActivity {

    private TextView txtQuestionTitle;
    private TextView txtQuestionScore;
    private RecyclerView recyclerviewMCOptions;
    private TextView txtNumberOfQuestionsSolved;
    private TextView txtCurrentQuestionProgress;
    private Button btnPickQuestion;

    private QuestionMC currentQuestionMC;

    private List<QuestionOptionMC> questionOptionMCs;
    private AdapterMCQuestionResponse adapterMCQuestionResponse;

    private QuestionMCResponse currentQuestionMCResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrun_question_mc);

        txtQuestionTitle = findViewById(R.id.txt_question_title);
        txtQuestionScore = findViewById(R.id.txt_question_score);
        recyclerviewMCOptions = findViewById(R.id.recyclerview_mc_options);
        txtNumberOfQuestionsSolved = findViewById(R.id.txt_number_of_questions_solved);
        txtCurrentQuestionProgress = findViewById(R.id.txt_current_question_progress);
        btnPickQuestion = findViewById(R.id.btn_pick_question);

        super.btnNext = findViewById(R.id.btn_next);
        super.btnPrevious = findViewById(R.id.btn_previous);

        super.testRun = (TestRun) getIntent().getSerializableExtra("TESTRUN");

        super.styleQuestionProgressIndicators(
                txtNumberOfQuestionsSolved, txtCurrentQuestionProgress
        );

        questionOptionMCs = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerviewMCOptions.setLayoutManager(layoutManager);

        super.db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        currentQuestionMCResponse =
                (QuestionMCResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        new Thread(() -> {
            currentQuestionMC =
                    db.questionMCDAO().getById(currentQuestionMCResponse.questionMCId);
            questionOptionMCs = db.questionOptionMCDAO().getFromQuestionMC(currentQuestionMC.id);
            adapterMCQuestionResponse =
                    new AdapterMCQuestionResponse(questionOptionMCs);
            recyclerviewMCOptions.setAdapter(adapterMCQuestionResponse);
            currentQuestionMC.questionOptionMCs = questionOptionMCs.toArray(new QuestionOptionMC[0]);
            double maxQuestionScore = currentQuestionMC.getScore();
            QuestionOptionMC finalSelectedQuestionOption = findSelectedQuestionOption();
            runOnUiThread(() -> {
                adapterMCQuestionResponse.notifyDataSetChanged();
                if (finalSelectedQuestionOption != null) {
                    adapterMCQuestionResponse.setSelectedQuestionOption(finalSelectedQuestionOption);
                }
                txtQuestionTitle.setText(currentQuestionMC.title);
                txtQuestionScore.setText(String.valueOf(maxQuestionScore));
                super.setPreviousAndNextButtonsAndActions();
                super.setPickQuestionButtonAndAction(btnPickQuestion);

            });
        }).start();

    }

    private QuestionOptionMC findSelectedQuestionOption() {
        Iterator<QuestionOptionMC> itQuestionOptionMC = questionOptionMCs.iterator();
        QuestionOptionMC selectedQuestionOption = null;
        while (selectedQuestionOption == null && itQuestionOptionMC.hasNext()) {
            QuestionOptionMC currentQuestionOptionMC = itQuestionOptionMC.next();
            if (currentQuestionOptionMC.id.equals(currentQuestionMCResponse.questionMCOptionSelected)) {
                selectedQuestionOption = currentQuestionOptionMC;
            }
        }
        return selectedQuestionOption;
    }

    @Override
    protected void setCurrentQuestionData() {
        QuestionMCResponse currentQuestionMCResponse =
                (QuestionMCResponse) super.testRun.questionResponses[super.testRun.currentQuestionIndex];
        currentQuestionMCResponse.testRunId = super.testRun.id;
        currentQuestionMCResponse.questionMCId = currentQuestionMC.id;
        QuestionOptionMC selectedOption = adapterMCQuestionResponse.getSelectedQuestionOption();
        if (selectedOption != null) {
            currentQuestionMCResponse.questionMCOptionSelected = selectedOption.id;
            currentQuestionMCResponse.score = selectedOption.score;
        }
        else {
            currentQuestionMCResponse.questionMCOptionSelected = null;
            currentQuestionMCResponse.score = 0;
        }
    }

}
