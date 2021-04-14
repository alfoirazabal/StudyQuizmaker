package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionmc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;

import java.util.List;

public class AdapterMCQuestion extends RecyclerView.Adapter<AdapterMCQuestion.ViewHolder> {

    private final List<QuestionOptionMC> questionOptionsMC;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtScore;
        private final TextView txtAnswer;
        private final ImageView imgbtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtScore = itemView.findViewById(R.id.txt_score);
            txtAnswer = itemView.findViewById(R.id.txt_answer);
            imgbtnDelete = itemView.findViewById(R.id.imgbtn_delete);
        }

        public TextView getTxtScore() {
            return txtScore;
        }

        public TextView getTxtAnswer() {
            return txtAnswer;
        }

        public ImageView getImgbtnDelete() {
            return imgbtnDelete;
        }
    }

    public AdapterMCQuestion(
            List<QuestionOptionMC> questionOptionsMC
    ) {
        this.questionOptionsMC = questionOptionsMC;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public AdapterMCQuestion.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_question_mc_add,
                parent,
                false
        );

        return new AdapterMCQuestion.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMCQuestion.ViewHolder holder, int position) {
        QuestionOptionMC questionOptionMC = questionOptionsMC.get(position);
        holder.getTxtScore().setText(String.valueOf(questionOptionMC.score));
        holder.getTxtAnswer().setText(questionOptionMC.answerText);
        holder.getImgbtnDelete().setOnClickListener(v -> {
            questionOptionsMC.remove(questionOptionMC);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return questionOptionsMC.size();
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
