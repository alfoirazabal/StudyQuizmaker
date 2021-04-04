package com.alfoirazabal.studyquizmaker.gui.topic.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
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
