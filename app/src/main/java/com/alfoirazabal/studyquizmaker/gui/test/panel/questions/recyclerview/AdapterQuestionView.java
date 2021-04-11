package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.Question;

import java.util.List;

public class AdapterQuestionView extends
        RecyclerView.Adapter<AdapterQuestionView.ViewHolder> {

    private final List<Question> questions;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtQuestionType;
        private final TextView txtScore;
        private final TextView txtAnswer;
        private final TextView txtWrongAnswers;

        public ViewHolder(View view) {
            super(view);

            txtTitle = view.findViewById(R.id.txt_title);
            txtQuestionType = view.findViewById(R.id.txt_question_type);
            txtScore = view.findViewById(R.id.txt_score_total);
            txtAnswer = view.findViewById(R.id.txt_answer);
            txtWrongAnswers = view.findViewById(R.id.txt_wrong_answers);

            view.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupMenu.setForceShowIcon(true);
                }
                popupMenu.inflate(R.menu.menu_edit_delete);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((menuItem) -> {
                    int menuItemId = menuItem.getItemId();
                    if (menuItemId == R.id.item_edit) {
                        handleEdit();
                        return true;
                    }
                    else if (menuItemId == R.id.item_delete) {
                        handleDelete();
                        return true;
                    }
                    else {
                        return false;
                    }
                });
            });
        }

        private void handleEdit() {
            int questionToEditPosition = getAdapterPosition();
            Question questionToEdit = questions.get(questionToEditPosition);
            Intent intentEditQuestionSimple = new Intent(
                    itemView.getContext(),
                    questionToEdit.getUpdateGUIClass()
            );
            intentEditQuestionSimple.putExtra("TESTID", questionToEdit.getTestId());
            intentEditQuestionSimple.putExtra("QUESTIONID", questionToEdit.getId());
            itemView.getContext().startActivity(intentEditQuestionSimple);
        }

        private void handleDelete() {
            int questionPosition = getAdapterPosition();
            Question questionToDelete = questions.get(questionPosition);
            Context context = itemView.getContext();
            String questionDeletionDescription =
                    context.getString(R.string.msg_delete_confirmation_question) + "\n" +
                            questionToDelete.toString(context);
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle(R.string.delete_confirmation)
                    .setMessage(questionDeletionDescription)
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase db = Room.databaseBuilder(
                                    context,
                                    AppDatabase.class,
                                    AppConstants.getDBLocation(context)
                            ).build();
                            questionToDelete.deleteFromDB(db);
                        }).start();
                        questions.remove(questionToDelete);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtQuestionType() { return txtQuestionType; }

        public TextView getTxtScore() {
            return txtScore;
        }

        public TextView getTxtAnswer() {
            return txtAnswer;
        }

        public TextView getTxtWrongAnswers() {
            return txtWrongAnswers;
        }
    }

    public AdapterQuestionView(List<Question> questions) {
        this.questions = questions;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public AdapterQuestionView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_question,
                parent,
                false
        );

        return new AdapterQuestionView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterQuestionView.ViewHolder holder, int position) {
        Question currentQuestion = questions.get(position);
        holder.getTxtTitle().setText(currentQuestion.getTitle());
        holder.getTxtQuestionType().setText(currentQuestion.getQuestionTypeName(
                holder.itemView.getContext()
        ));
        holder.getTxtAnswer().setText(currentQuestion.getAnswer());
        try {
            String wrongAnswers = currentQuestion.getWrongAnswers();
            holder.getTxtWrongAnswers().setVisibility(View.VISIBLE);
            holder.getTxtWrongAnswers().setText(wrongAnswers);
        } catch (Question.NoWrongAnswers noWrongAnswers) {
            holder.getTxtWrongAnswers().setVisibility(View.GONE);
        }
        holder.getTxtScore().setText(String.valueOf(currentQuestion.getScore()));
    }

    @Override
    public int getItemCount() {
        return questions.size();
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
