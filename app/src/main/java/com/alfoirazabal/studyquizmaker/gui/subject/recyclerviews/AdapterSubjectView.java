package com.alfoirazabal.studyquizmaker.gui.subject.recyclerviews;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.Subject;

import java.util.List;

public class AdapterSubjectView extends RecyclerView.Adapter<AdapterSubjectView.ViewHolder> {

    private List<Subject> subjects;

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
                            // TODO
                            return true;
                        case R.id.item_delete:
                            // TODO
                            return true;
                        default:
                            return false;
                    }
                });
                return false;
            });
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
