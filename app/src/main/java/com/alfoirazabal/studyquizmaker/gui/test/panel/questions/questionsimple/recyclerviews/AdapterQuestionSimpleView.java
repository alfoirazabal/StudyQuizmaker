package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;

import java.util.List;

public class AdapterQuestionSimpleView extends
        RecyclerView.Adapter<AdapterQuestionSimpleView.ViewHolder> {

    private final List<QuestionSimple> questionSimples;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtScore;
        private final TextView txtDescription;

        public ViewHolder(View view) {
            super(view);

            txtTitle = view.findViewById(R.id.txt_title);
            txtScore = view.findViewById(R.id.txt_score_total);
            txtDescription = view.findViewById(R.id.txt_description);
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtScore() {
            return txtScore;
        }

        public TextView getTxtDescription() {
            return txtDescription;
        }
    }

    public AdapterQuestionSimpleView(List<QuestionSimple> questionSimples) {
        this.questionSimples = questionSimples;
    }

    @NonNull
    @Override
    public AdapterQuestionSimpleView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_question_simple,
                parent,
                false
        );

        return new AdapterQuestionSimpleView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterQuestionSimpleView.ViewHolder holder, int position) {
        QuestionSimple currentQuestionSimple = questionSimples.get(position);
        holder.getTxtTitle().setText(currentQuestionSimple.title);
        holder.getTxtDescription().setText(currentQuestionSimple.description);
        holder.getTxtScore().setText(String.valueOf(currentQuestionSimple.score));
    }

    @Override
    public int getItemCount() {
        return questionSimples.size();
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
