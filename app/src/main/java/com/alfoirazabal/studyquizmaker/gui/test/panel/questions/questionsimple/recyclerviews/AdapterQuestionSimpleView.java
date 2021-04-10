package com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.recyclerviews;

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
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.UpdateQuestionSimple;

import java.util.List;

public class AdapterQuestionSimpleView extends
        RecyclerView.Adapter<AdapterQuestionSimpleView.ViewHolder> {

    private final List<QuestionSimple> questionSimples;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtScore;
        private final TextView txtAnswer;

        public ViewHolder(View view) {
            super(view);

            txtTitle = view.findViewById(R.id.txt_title);
            txtScore = view.findViewById(R.id.txt_score_total);
            txtAnswer = view.findViewById(R.id.txt_answer);

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
            int questionSimplePosition = getAdapterPosition();
            QuestionSimple questionSimpleToEdit = questionSimples.get(questionSimplePosition);
            Intent intentEditQuestionSimple = new Intent(
                    itemView.getContext(),
                    UpdateQuestionSimple.class
            );
            intentEditQuestionSimple.putExtra("TESTID", questionSimpleToEdit.testId);
            intentEditQuestionSimple.putExtra("QUESTIONSIMPLEID", questionSimpleToEdit.id);
            itemView.getContext().startActivity(intentEditQuestionSimple);
        }

        private void handleDelete() {
            int questionSimplePosition = getAdapterPosition();
            QuestionSimple questionSimpleToDelete = questionSimples.get(questionSimplePosition);
            Context context = itemView.getContext();
            String testDeletionDescription =
                    context.getString(R.string.msg_delete_confirmation_question) + "\n" +
                            questionSimpleToDelete.toString(context);
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle(R.string.delete_confirmation)
                    .setMessage(testDeletionDescription)
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase db = Room.databaseBuilder(
                                    context,
                                    AppDatabase.class,
                                    AppConstants.getDBLocation(context)
                            ).build();
                            db.questionSimpleDAO().delete(questionSimpleToDelete);
                        }).start();
                        questionSimples.remove(questionSimpleToDelete);
                        notifyItemRemoved(questionSimplePosition);
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtScore() {
            return txtScore;
        }

        public TextView getTxtAnswer() {
            return txtAnswer;
        }
    }

    public AdapterQuestionSimpleView(List<QuestionSimple> questionSimples) {
        this.questionSimples = questionSimples;

        this.setHasStableIds(true);
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
        holder.getTxtAnswer().setText(currentQuestionSimple.answer);
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
