package com.alfoirazabal.studyquizmaker.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.alfoirazabal.studyquizmaker.db.dao.SubjectDAO;
import com.alfoirazabal.studyquizmaker.db.dao.TestDAO;
import com.alfoirazabal.studyquizmaker.db.dao.TopicDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionMCDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionOptionMCDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionSimpleDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionTFDAO;
import com.alfoirazabal.studyquizmaker.db.dao.testrun.TestRunDAO;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

@Database(
        entities = {
                Subject.class,
                Topic.class,
                Test.class,
                TestRun.class,
                QuestionMC.class,
                QuestionOptionMC.class,
                QuestionSimple.class,
                QuestionTF.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SubjectDAO subjectDAO();
    public abstract TopicDAO topicDAO();
    public abstract TestDAO testDAO();
    public abstract TestRunDAO testRunDAO();
    public abstract QuestionMCDAO questionMCDAO();
    public abstract QuestionOptionMCDAO questionOptionMCDAO();
    public abstract QuestionSimpleDAO questionSimpleDAO();
    public abstract QuestionTFDAO questionTFDAO();

}
