package com.alfoirazabal.studyquizmaker.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.alfoirazabal.studyquizmaker.db.dao.SubjectDAO;
import com.alfoirazabal.studyquizmaker.domain.Subject;

@Database(
        entities = {
                Subject.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SubjectDAO subjectDAO();

}
