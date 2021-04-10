package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.testrun.results.recyclerviews;

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

public class AdapterQuestionSimpleResponse extends RecyclerView.Adapter<AdapterQuestionSimpleResponse.ViewHolder> {

    private final QuestionSimpleResponse[] questionSimpleResponses;
    private final AppDatabase db;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtAnswer;
        private final TextView txtScored;
        private final ProgressBar progressbarScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtAnswer = itemView.findViewById(R.id.txt_answer);
            txtScored = itemView.findViewById(R.id.txt_scored);
            progressbarScore = itemView.findViewById(R.id.progressbar_score);

            this.progressbarScore.setMax(100);
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtAnswer() {
            return txtAnswer;
        }

        public TextView getTxtScored() {
            return txtScored;
        }

        public ProgressBar getProgressbarScore() {
            return progressbarScore;
        }
    }

    public AdapterQuestionSimpleResponse(
            QuestionSimpleResponse[] questionSimpleResponses,
            AppDatabase db
    ) {
        this.questionSimpleResponses = questionSimpleResponses;
        this.db = db;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public AdapterQuestionSimpleResponse.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_question_simple_response,
                parent,
                false
        );

        return new AdapterQuestionSimpleResponse.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionSimpleResponse currentQuestionSimpleResponse = questionSimpleResponses[position];
        new Thread(() -> {
            QuestionSimple currentQuestionSimple = db.questionSimpleDAO().getById(
                    currentQuestionSimpleResponse.questionSimpleId
            );
            new Handler(Looper.getMainLooper()).post(() -> {
                holder.getTxtTitle().setText(currentQuestionSimple.title);
                holder.getTxtAnswer().setText(currentQuestionSimpleResponse.answered);
                double totalScore = currentQuestionSimple.score;
                double scored = currentQuestionSimpleResponse.score;
                double scoredPercentage = (scored / totalScore) * 100;
                holder.getProgressbarScore().setProgress((int)scoredPercentage);
                String scoredIndicatorText = scored + "/" + totalScore;
                holder.getTxtScored().setText(scoredIndicatorText);
                colorScoredText(holder.getTxtScored(), scored, totalScore);
            });
        }).start();
    }

    private void colorScoredText(TextView textView, double scoreReached, double scoreTotal) {
        if (scoreReached == 0) {
            textView.setTextColor(Color.RED);
        }
        else if (scoreReached > 0 && scoreReached < scoreTotal) {
            textView.setTextColor(Color.YELLOW);
        }
        else {
            textView.setTextColor(Color.GREEN);
        }
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
