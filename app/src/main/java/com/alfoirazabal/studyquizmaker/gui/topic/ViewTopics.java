package com.alfoirazabal.studyquizmaker.gui.topic;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class ViewTopics extends AppCompatActivity {

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

        RecyclerView recyclerviewTopics = findViewById(R.id.recyclerview_topics);
        fabtnAdd = findViewById(R.id.fabtn_add);

        fabtnAdd.setOnClickListener((view) -> {
            Intent intentAddTopic = new Intent(this, AddTopic.class);
            intentAddTopic.putExtra("SUBJECTID", subject.id);
            startActivity(intentAddTopic);
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL
        );
        recyclerviewTopics.setLayoutManager(layoutManager);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        topics = new ArrayList<>();
        adapterTopics = new AdapterTopicView(topics);
        recyclerviewTopics.setAdapter(adapterTopics);

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            String subjectId = bundle.getString("SUBJECTID");
            subject = db.subjectDAO().getById(subjectId);
            runOnUiThread(() -> {
                getSupportActionBar().setTitle(subject.name);
                fabtnAdd.setEnabled(true);
            });
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        String subjectId = bundle.getString("SUBJECTID");
        new Thread(() -> {
            topics.clear();
            topics.addAll(db.topicDAO().getFromSubject(subjectId));
            runOnUiThread(() -> adapterTopics.notifyDataSetChanged());
        }).start();
    }
}
