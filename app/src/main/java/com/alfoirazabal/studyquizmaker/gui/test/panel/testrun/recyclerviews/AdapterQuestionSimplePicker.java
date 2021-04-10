package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.recyclerviews;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.AnswerQuestionSimple;

public class AdapterQuestionSimplePicker extends
        RecyclerView.Adapter<AdapterQuestionSimplePicker.ViewHolder>{

    private static final int PROGRESSBAR_PARTITIONS = 10;

    private final QuestionSimpleResponse[] questionSimpleResponses;
    private final AppDatabase db;
    private final TestRun testRun;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtScore;
        private final ProgressBar progressbarScore;
        private final TextView txtAnswered;

        public ViewHolder(View view) {
            super(view);

            txtTitle = view.findViewById(R.id.txt_title);
            txtScore = view.findViewById(R.id.txt_score);
            progressbarScore = view.findViewById(R.id.progressbar_score);
            txtAnswered = view.findViewById(R.id.txt_answered);

            view.setOnClickListener(v -> {
                testRun.currentQuestionIndex = getAdapterPosition();
                Intent intentAnswerQuestion = new Intent(
                        view.getContext(),
                        AnswerQuestionSimple.class
                );
                intentAnswerQuestion.putExtra("TESTRUN", testRun);
                intentAnswerQuestion.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                view.getContext().startActivity(intentAnswerQuestion);
            });
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtScore() {
            return txtScore;
        }

        public ProgressBar getProgressbarScore() {
            return progressbarScore;
        }

        public TextView getTxtAnswered() {
            return txtAnswered;
        }
    }

    public AdapterQuestionSimplePicker(
            QuestionSimpleResponse[] questionSimpleResponses,
            AppDatabase db,
            TestRun testRun
    ) {
        this.questionSimpleResponses = questionSimpleResponses;
        this.db = db;
        this.testRun = testRun;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_testrun_question_picker,
                parent,
                false
        );

        return new AdapterQuestionSimplePicker.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionSimpleResponse currentResponse = this.questionSimpleResponses[position];
        new Thread(() -> {
            QuestionSimple currentSimpleQuestion =
                    db.questionSimpleDAO().getById(currentResponse.questionSimpleId);
            double maxQuestionScore = currentSimpleQuestion.score;
            double scored = currentResponse.score;
            new Handler(Looper.getMainLooper()).post(() -> {
                holder.getTxtTitle().setText(currentSimpleQuestion.title);
                String scoreIndicator = scored + "/" + maxQuestionScore;
                holder.getTxtScore().setText(scoreIndicator);
                ProgressBar scoreProgressBar = holder.getProgressbarScore();
                setProgressBar(scoreProgressBar, maxQuestionScore, scored);
                TextView txtAnswered = holder.getTxtAnswered();
                if (currentResponse.isAnswered) {
                    txtAnswered.setText(R.string.answered);
                    txtAnswered.setTextColor(Color.BLACK);
                }
                else {
                    txtAnswered.setText(R.string.unanswered);
                    txtAnswered.setTextColor(Color.RED);
                }
            });
        }).start();
    }

    private void setProgressBar(
            ProgressBar progressBar,
            double maxQuestionScore,
            double scored
    ) {
        double proportion = PROGRESSBAR_PARTITIONS / maxQuestionScore;
        int scoreInProgressBar = (int)(scored * proportion);
        progressBar.setMax(PROGRESSBAR_PARTITIONS);
        progressBar.setProgress(scoreInProgressBar);
    }

    @Override
    public int getItemCount() {
        return this.questionSimpleResponses.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
