package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionmo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;

import java.util.List;

public class AdapterMOQuestion extends RecyclerView.Adapter<AdapterMOQuestion.ViewHolder> {

    private List<QuestionOptionMO> questionOptionMOs;

    public AdapterMOQuestion(
            List<QuestionOptionMO> questionOptionMOs
    ) {
        this.questionOptionMOs = questionOptionMOs;

        this.setHasStableIds(true);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtScore;
        private TextView txtAnswer;
        private ImageView imgbtnDelete;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_question_mc_and_mo_add,
                parent,
                false
        );

        return new AdapterMOQuestion.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionOptionMO questionOptionMO = this.questionOptionMOs.get(position);
        holder.getTxtScore().setText(String.valueOf(questionOptionMO.score));
        holder.getTxtAnswer().setText(questionOptionMO.answerText);
        holder.getImgbtnDelete().setOnClickListener(v -> {
            this.questionOptionMOs.remove(questionOptionMO);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return this.questionOptionMOs.size();
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
