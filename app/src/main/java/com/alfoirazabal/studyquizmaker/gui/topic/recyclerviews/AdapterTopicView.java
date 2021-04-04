package com.alfoirazabal.studyquizmaker.gui.topic.recyclerviews;

import android.app.AlertDialog;
import android.content.Context;
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
import com.alfoirazabal.studyquizmaker.domain.Topic;

import java.util.List;

public class AdapterTopicView extends RecyclerView.Adapter<AdapterTopicView.ViewHolder> {

    private List<Topic> topics;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtSubjectName;
        private final TextView txtSubjectDescription;

        public ViewHolder(View view) {
            super(view);

            txtSubjectName = view.findViewById(R.id.txt_topic_name);
            txtSubjectDescription = view.findViewById(R.id.txt_topic_description);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), txtSubjectName);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        popupMenu.setForceShowIcon(true);
                    }
                    popupMenu.inflate(R.menu.menu_topic);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener((menuItem) -> {
                        switch (menuItem.getItemId()) {
                            case R.id.item_edit:
                                // TODO
                                return true;
                            case R.id.item_delete:
                                handleDelete();
                                return true;
                            default:
                                return false;
                        }
                    });
                    return false;
                }
            });

        }

        private void handleDelete() {
            int topicPosition = getAdapterPosition();
            Topic topic = topics.get(topicPosition);
            Context context = itemView.getContext();
            String topicDeletionDescription =
                    context.getString(R.string.msg_delete_confirmation_topic) + "\n" +
                            topic.toString(context);
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle(context.getString(R.string.delete_confirmation))
                    .setMessage(topicDeletionDescription)
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase db = Room.databaseBuilder(
                                    itemView.getContext(),
                                    AppDatabase.class,
                                    AppConstants.DATABASE_LOCATION
                            ).build();
                            db.topicDAO().delete(topic);
                        }).start();
                        topics.remove(topic);
                        notifyItemRangeRemoved(topicPosition, topics.size());
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

    public AdapterTopicView(List<Topic> topics) {
        this.topics = topics;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_topic,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic currentTopic = topics.get(position);
        holder.getTxtSubjectName().setText(currentTopic.name);
        if (currentTopic.description.equals("")) {
            holder.getTxtSubjectDescription().setVisibility(View.GONE);
        }
        else {
            holder.getTxtSubjectDescription().setVisibility(View.VISIBLE);
            holder.getTxtSubjectDescription().setText(currentTopic.description);
        }
    }

    @Override
    public int getItemCount() {
        return topics.size();
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
