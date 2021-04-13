package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.recyclerviews;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;

import java.util.List;

public class AdapterSimpleQuestionScore extends RecyclerView.Adapter<AdapterSimpleQuestionScore.ViewHolder> {

    private static final int SEEKBAR_PARTITIONS = 10;

    private final List<QuestionSimpleResponse> questionSimpleResponses;

    private final AppDatabase db;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtQuestion;
        private final TextView txtRightAnswer;
        private final TextView txtAnswered;
        private final SeekBar seekbarScore;
        private final TextView txtScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = itemView.findViewById(R.id.txt_question);
            txtRightAnswer = itemView.findViewById(R.id.txt_right_answer);
            txtAnswered = itemView.findViewById(R.id.txt_answered);
            seekbarScore = itemView.findViewById(R.id.seekbar_score);
            txtScore = itemView.findViewById(R.id.txt_score);

            seekbarScore.setMax(SEEKBAR_PARTITIONS);
        }

        public TextView getTxtQuestion() {
            return txtQuestion;
        }

        public TextView getTxtRightAnswer() {
            return txtRightAnswer;
        }

        public TextView getTxtAnswered() {
            return txtAnswered;
        }

        public SeekBar getSeekbarScore() {
            return seekbarScore;
        }

        public TextView getTxtScore() {
            return txtScore;
        }
    }

    public AdapterSimpleQuestionScore(
            List<QuestionSimpleResponse> questionSimpleResponses,
            AppDatabase db
    ) {
        this.questionSimpleResponses = questionSimpleResponses;
        this.db = db;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public AdapterSimpleQuestionScore.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_simple_question_score,
                parent,
                false
        );

        return new AdapterSimpleQuestionScore.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSimpleQuestionScore.ViewHolder holder, int position) {
        QuestionSimpleResponse questionSimpleResponse = this.questionSimpleResponses.get(position);
        new Thread(() -> {
            QuestionSimple currentQuestion =
                    db.questionSimpleDAO().getById(questionSimpleResponse.questionSimpleId);
            double maxScore = currentQuestion.score;
            new Handler(Looper.getMainLooper()).post(() -> {
                holder.getTxtQuestion().setText(currentQuestion.title);
                holder.getTxtAnswered().setText(questionSimpleResponse.answered);
                holder.getTxtRightAnswer().setText(currentQuestion.answer);
                holder.getSeekbarScore().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekbarToScore(maxScore, progress, questionSimpleResponse);
                        holder.getTxtScore().setText(String.valueOf(questionSimpleResponse.score));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) { }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }
                });
                progressToSeekbar(maxScore, questionSimpleResponse.score, holder.getSeekbarScore());
                holder.getTxtScore().setText(String.valueOf(questionSimpleResponse.score));
            });
        }).start();
    }

    private void seekbarToScore(
            double maxScore,
            int seekbarProgress,
            QuestionSimpleResponse questionSimpleResponse
    ) {
        double prop = maxScore / SEEKBAR_PARTITIONS;
        questionSimpleResponse.score = (double)(Math.round((seekbarProgress * prop) * 100)) / 100;
    }

    private void progressToSeekbar(double maxScore, double score, SeekBar seekBar) {
        double prop = SEEKBAR_PARTITIONS / maxScore;
        int progressInSeekbar = (int)(score * prop);
        seekBar.setProgress(progressInSeekbar);
    }

    @Override
    public int getItemCount() {
        return this.questionSimpleResponses.size();
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
