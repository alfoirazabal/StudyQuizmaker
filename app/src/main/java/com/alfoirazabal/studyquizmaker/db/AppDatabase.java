package com.alfoirazabal.studyquizmaker.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.alfoirazabal.studyquizmaker.db.dao.SubjectDAO;
import com.alfoirazabal.studyquizmaker.db.dao.TestDAO;
import com.alfoirazabal.studyquizmaker.db.dao.TopicDAO;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.Topic;

@Database(
        entities = {
                Subject.class,
                Topic.class,
                Test.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SubjectDAO subjectDAO();
    public abstract TopicDAO topicDAO();
    public abstract TestDAO testDAO();

}
