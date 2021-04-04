package com.alfoirazabal.studyquizmaker.gui.topic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.gui.topic.recyclerviews.AdapterTopicView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ViewTopics extends AppCompatActivity {

    private RecyclerView recyclerviewTopics;
    private FloatingActionButton fabtnAdd;

    private AppDatabase db;

    private AdapterTopicView adapterTopics;

    private List<Topic> topics;
    private Subject subject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_view);

        this.getSupportActionBar().setTitle(null);
        this.getSupportActionBar().setSubtitle(R.string.topics);

        recyclerviewTopics = findViewById(R.id.recyclerview_topics);
        fabtnAdd = findViewById(R.id.fabtn_add);

        fabtnAdd.setOnClickListener((view) -> {
            // TODO
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL
        );
        this.recyclerviewTopics.setLayoutManager(layoutManager);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.DATABASE_LOCATION
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            String subjectId = bundle.getString("SUBJECTID");
            subject = db.subjectDAO().getById(subjectId);
            topics = db.topicDAO().getFromSubject(subject.id);
            adapterTopics = new AdapterTopicView(topics);
            runOnUiThread(() -> {
                getSupportActionBar().setTitle(subject.name);
                recyclerviewTopics.setAdapter(adapterTopics);
            });
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            topics.clear();
            topics.addAll(db.topicDAO().getFromSubject(subject.id));
            adapterTopics.notifyDataSetChanged();
        });
    }
}
