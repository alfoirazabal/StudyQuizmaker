package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.recyclerviews;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.Question;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.questions.ViewQuestionMCResponse;

public class AdapterQuestionResponse extends RecyclerView.Adapter<AdapterQuestionResponse.ViewHolder> {

    private final QuestionResponse[] questionResponses;
    private final AppDatabase db;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtAnswer;
        private final TextView txtScored;
        private final TextView answeredQuestionLabel;
        private final ProgressBar progressbarScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtAnswer = itemView.findViewById(R.id.txt_answer);
            txtScored = itemView.findViewById(R.id.txt_scored);
            answeredQuestionLabel = itemView.findViewById(R.id.txt_answered_question_label);
            progressbarScore = itemView.findViewById(R.id.progressbar_score);

            this.progressbarScore.setMax(100);

            itemView.setOnClickListener(v -> {
                QuestionResponse questionResponse = questionResponses[getAdapterPosition()];
                Intent intentViewQuestion = new Intent(
                        itemView.getContext(), questionResponse.getViewQuestionResponseClass()
                );
                intentViewQuestion.putExtra("QUESTIONRESPONSE", questionResponse);
                itemView.getContext().startActivity(intentViewQuestion);;
            });
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

        public TextView getAnsweredQuestionLabel() { return answeredQuestionLabel; }

        public ProgressBar getProgressbarScore() {
            return progressbarScore;
        }
    }

    public AdapterQuestionResponse(
            QuestionResponse[] questionResponses,
            AppDatabase db
    ) {
        this.questionResponses = questionResponses;
        this.db = db;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public AdapterQuestionResponse.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_question_response,
                parent,
                false
        );

        return new AdapterQuestionResponse.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionResponse currentQuestionResponse = questionResponses[position];
        new Thread(() -> {
            Question currentQuestion = currentQuestionResponse.getQuestion(db);
            boolean isAnswered = currentQuestionResponse.isAnswered();
            String answered = "";
            if (isAnswered) {
                answered = currentQuestionResponse.getAnswered(db);
            }
            String finalAnswered = answered;
            new Handler(Looper.getMainLooper()).post(() -> {
                holder.getTxtTitle().setText(currentQuestion.getTitle());
                TextView answeredQuestionLabel = holder.getAnsweredQuestionLabel();
                TextView txtAnswer = holder.getTxtAnswer();
                if (isAnswered) {
                    answeredQuestionLabel.setVisibility(View.VISIBLE);
                    txtAnswer.setText(finalAnswered);
                    txtAnswer.setGravity(Gravity.LEFT);
                    txtAnswer.setTextColor(Color.BLACK);
                    txtAnswer.setTypeface(null, Typeface.NORMAL);
                }
                else {
                    answeredQuestionLabel.setVisibility(View.GONE);
                    txtAnswer.setText(R.string.unanswered);
                    txtAnswer.setGravity(Gravity.CENTER_HORIZONTAL);
                    txtAnswer.setTextColor(Color.RED);
                    txtAnswer.setTypeface(null, Typeface.ITALIC);
                }
                double totalScore = currentQuestion.getScore();
                double scored = currentQuestionResponse.getScore();
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
            int colorOrange = Color.rgb(252, 173, 0);
            textView.setTextColor(colorOrange);
        }
        else {
            int colorGreen = Color.rgb(0, 200, 0);
            textView.setTextColor(colorGreen);
        }
    }

    @Override
    public int getItemCount() {
        return this.questionResponses.length;
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
