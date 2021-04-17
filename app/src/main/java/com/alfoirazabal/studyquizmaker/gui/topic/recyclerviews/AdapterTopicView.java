package com.alfoirazabal.studyquizmaker.gui.topic.recyclerviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.gui.test.ViewTests;
import com.alfoirazabal.studyquizmaker.gui.topic.UpdateTopic;

import java.util.List;

public class AdapterTopicView extends RecyclerView.Adapter<AdapterTopicView.ViewHolder> {

    private final List<Topic> topics;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtSubjectName;
        private final TextView txtSubjectDescription;
        private final ImageView imgTopicIcon;

        public ViewHolder(View view) {
            super(view);

            txtSubjectName = view.findViewById(R.id.txt_topic_name);
            txtSubjectDescription = view.findViewById(R.id.txt_topic_description);
            imgTopicIcon = view.findViewById(R.id.img_topic_icon);

            view.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), txtSubjectName);
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
                return false;
            });

            view.setOnClickListener(v -> handleView());

        }

        private void handleEdit() {
            int topicPosition = getAdapterPosition();
            Topic topic = topics.get(topicPosition);
            Context context = itemView.getContext();
            Intent intentEditTopic = new Intent(context, UpdateTopic.class);
            intentEditTopic.putExtra("TOPICID", topic.id);
            context.startActivity(intentEditTopic);
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
                                    AppConstants.getDBLocation(context)
                            ).build();
                            db.topicDAO().delete(topic);
                        }).start();
                        topics.remove(topic);
                        notifyItemRemoved(topicPosition);
                    })
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show();
        }

        private void handleView() {
            int topicPosition = getAdapterPosition();
            Topic currentTopic = topics.get(topicPosition);
            Intent intentViewTests = new Intent(itemView.getContext(), ViewTests.class);
            intentViewTests.putExtra("TOPICID", currentTopic.id);
            itemView.getContext().startActivity(intentViewTests);
        }

        public TextView getTxtSubjectName() {
            return txtSubjectName;
        }

        public TextView getTxtSubjectDescription() {
            return txtSubjectDescription;
        }

        public ImageView getImgTopicIcon() {
            return imgTopicIcon;
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
        holder.getImgTopicIcon().setColorFilter(currentTopic.color, PorterDuff.Mode.MULTIPLY);
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
