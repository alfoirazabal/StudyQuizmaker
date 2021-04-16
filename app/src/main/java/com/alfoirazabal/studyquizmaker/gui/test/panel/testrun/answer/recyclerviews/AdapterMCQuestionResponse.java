package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.recyclerviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdapterMCQuestionResponse extends RecyclerView.Adapter<AdapterMCQuestionResponse.ViewHolder> {

    private List<QuestionOptionMCSelection> questionOptionMCsSelections;

    private class QuestionOptionMCSelection {
        public QuestionOptionMC questionOptionMC;
        public boolean selected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton rbtnMCOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                int questionOptionMCPosition = getAdapterPosition();
                QuestionOptionMCSelection currentQuestionOptionMCsSelection =
                        questionOptionMCsSelections.get(questionOptionMCPosition);
                if (currentQuestionOptionMCsSelection.selected) {
                    currentQuestionOptionMCsSelection.selected = false;
                }
                else {
                    deselectAllQuestions();
                    currentQuestionOptionMCsSelection.selected = true;
                }
                ();
            });

            rbtnMCOption = itemView.findViewById(R.id.rbtn_mc_option);
        }

        public RadioButton getRbtnMCOption() {
            return rbtnMCOption;
        }
    }

    public AdapterMCQuestionResponse(
            List<QuestionOptionMC> questionOptionMCs
    ) {
        this.questionOptionMCsSelections = new ArrayList<>();
        for (int i = 0 ; i < questionOptionMCs.size() ; i++) {
            QuestionOptionMC questionOptionMC = questionOptionMCs.get(i);
            QuestionOptionMCSelection questionOptionMCSelection =
                    new QuestionOptionMCSelection();
            questionOptionMCSelection.questionOptionMC = questionOptionMC;
            questionOptionMCSelection.selected = false;
            this.questionOptionMCsSelections.add(questionOptionMCSelection);
        }

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public AdapterMCQuestionResponse.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_mc_option,
                parent,
                false
        );

        return new AdapterMCQuestionResponse.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMCQuestionResponse.ViewHolder holder, int position) {
        holder.getRbtnMCOption().setText(questionOptionMCsSelections.get(position).questionOptionMC.answerText);
        holder.getRbtnMCOption().setChecked(questionOptionMCsSelections.get(position).selected);
    }

    @Override
    public int getItemCount() {
        return this.questionOptionMCsSelections.size();
    }

    public void setSelectedQuestionOption(QuestionOptionMC questionOptionMC) {
        deselectAllQuestions();
        Iterator<QuestionOptionMCSelection> itQuestionOptionMCSelection =
                questionOptionMCsSelections.iterator();
        boolean foundSelectableQuestion = false;
        while (!foundSelectableQuestion && itQuestionOptionMCSelection.hasNext()) {
            QuestionOptionMCSelection currentQuestionOptionMCSelection = itQuestionOptionMCSelection.next();
            if (currentQuestionOptionMCSelection.questionOptionMC == questionOptionMC) {
                currentQuestionOptionMCSelection.selected = true;
                foundSelectableQuestion = true;
            }
        }
        notifyDataSetChanged();
    }

    public QuestionOptionMC getSelectedQuestionOption() {
        boolean found = false;
        QuestionOptionMC questionOptionMC = null;
        Iterator<QuestionOptionMCSelection> itQuestionOptionMCSelection =
                questionOptionMCsSelections.iterator();
        while (!found && itQuestionOptionMCSelection.hasNext()) {
            QuestionOptionMCSelection currQuestionOptionMCSelection =
                    itQuestionOptionMCSelection.next();
            if (currQuestionOptionMCSelection.selected) {
                questionOptionMC = currQuestionOptionMCSelection.questionOptionMC;
                found = true;
            }
        }
        return  questionOptionMC;
    }

    private void deselectAllQuestions() {
        Iterator<QuestionOptionMCSelection> itQuestionOptionMCSelection =
                questionOptionMCsSelections.iterator();
        while (itQuestionOptionMCSelection.hasNext()) {
            itQuestionOptionMCSelection.next().selected = false;
        }
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
