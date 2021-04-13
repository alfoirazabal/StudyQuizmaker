package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.recyclerviews;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.Question;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

public class AdapterQuestionPicker extends
        RecyclerView.Adapter<AdapterQuestionPicker.ViewHolder>{

    private final QuestionResponse[] questionResponses;
    private final AppDatabase db;
    private final TestRun testRun;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtQuestionScore;
        private final TextView txtAnswered;

        public ViewHolder(View view) {
            super(view);

            txtTitle = view.findViewById(R.id.txt_title);
            txtQuestionScore = view.findViewById(R.id.txt_question_score);
            txtAnswered = view.findViewById(R.id.txt_answered);

            view.setOnClickListener(v -> {
                testRun.currentQuestionIndex = getAdapterPosition();
                Intent intentAnswerQuestion = new Intent(
                        view.getContext(),
                        testRun.questionResponses[testRun.currentQuestionIndex].getAnswerQuestionClass()
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
            return txtQuestionScore;
        }

        public TextView getTxtAnswered() {
            return txtAnswered;
        }
    }

    public AdapterQuestionPicker(
            QuestionResponse[] questionResponses,
            AppDatabase db,
            TestRun testRun
    ) {
        this.questionResponses = questionResponses;
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

        return new AdapterQuestionPicker.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionResponse currentResponse = this.questionResponses[position];
        new Thread(() -> {
            Question currentQuestion = currentResponse.getQuestion(db);
            double maxQuestionScore = currentQuestion.getScore();
            new Handler(Looper.getMainLooper()).post(() -> {
                holder.getTxtTitle().setText(currentQuestion.getTitle());
                holder.getTxtScore().setText(String.valueOf(maxQuestionScore));
                TextView txtAnswered = holder.getTxtAnswered();
                if (currentResponse.isAnswered()) {
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
