package com.alfoirazabal.studyquizmaker.gui.subject.recyclerviews;

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
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.gui.subject.UpdateSubject;

import java.util.List;

public class AdapterSubjectView extends RecyclerView.Adapter<AdapterSubjectView.ViewHolder> {

    private final List<Subject> subjects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtSubjectName;
        private final TextView txtSubjectDescription;

        public ViewHolder(View view) {
            super(view);

            txtSubjectName = view.findViewById(R.id.txt_subject_name);
            txtSubjectDescription = view.findViewById(R.id.txt_subject_description);

            view.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), txtSubjectName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupMenu.setForceShowIcon(true);
                }
                popupMenu.inflate(R.menu.menu_subject);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((menuItem) -> {
                    switch (menuItem.getItemId()) {
                        case R.id.item_edit:
                            handleEdit();
                            return true;
                        case R.id.item_delete:
                            handleDelete();
                            return true;
                        default:
                            return false;
                    }
                });
                return false;
            });
        }

        private void handleEdit() {
            int subjectPosition = getAdapterPosition();
            Subject subject = subjects.get(subjectPosition);
            Context context = itemView.getContext();
            Intent intentSubjectUpdate = new Intent(context, UpdateSubject.class);
            intentSubjectUpdate.putExtra("SUBJECTID", subject.id);
            itemView.getContext().startActivity(intentSubjectUpdate);
        }

        private void handleDelete() {
            int subjectPosition = getAdapterPosition();
            Subject subject = subjects.get(subjectPosition);
            Context context = itemView.getContext();
            String subjectDeletionDescription =
                    context.getString(R.string.msg_delete_confirmation_subject) + "\n" +
                    subject.toString(context);
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle(context.getString(R.string.delete_confirmation))
                    .setMessage(subjectDeletionDescription)
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase db = Room.databaseBuilder(
                                    itemView.getContext(),
                                    AppDatabase.class,
                                    AppConstants.DATABASE_LOCATION
                            ).build();
                            db.subjectDAO().delete(subject);
                        }).start();
                        subjects.remove(subject);
                        notifyItemRangeRemoved(subjectPosition, subjects.size());
                    })
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show();
        }

        public TextView getTxtSubjectName() {
            return txtSubjectName;
        }

        public TextView getTxtSubjectDescription() {
            return txtSubjectDescription;
        }
    }

    public AdapterSubjectView(List<Subject> subjects) {
        this.subjects = subjects;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_subject,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject currentSubject = subjects.get(position);
        holder.getTxtSubjectName().setText(currentSubject.name);
        if (currentSubject.description.equals("")) {
            holder.getTxtSubjectDescription().setVisibility(View.GONE);
        }
        else {
            holder.getTxtSubjectDescription().setVisibility(View.VISIBLE);
            holder.getTxtSubjectDescription().setText(currentSubject.description);
        }
    }

    @Override
    public int getItemCount() {
        return subjects.size();
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