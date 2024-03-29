package com.alfoirazabal.studyquizmaker.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.alfoirazabal.studyquizmaker.db.dao.SubjectDAO;
import com.alfoirazabal.studyquizmaker.db.dao.TestDAO;
import com.alfoirazabal.studyquizmaker.db.dao.TopicDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionMCDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionMODAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionOptionMCDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionOptionMODAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionSimpleDAO;
import com.alfoirazabal.studyquizmaker.db.dao.question.QuestionTFDAO;
import com.alfoirazabal.studyquizmaker.db.dao.testrun.QuestionMCResponseDAO;
import com.alfoirazabal.studyquizmaker.db.dao.testrun.QuestionMOResponseDAO;
import com.alfoirazabal.studyquizmaker.db.dao.testrun.QuestionMOResponseOptionDAO;
import com.alfoirazabal.studyquizmaker.db.dao.testrun.QuestionSimpleResponseDAO;
import com.alfoirazabal.studyquizmaker.db.dao.testrun.QuestionTFResponseDAO;
import com.alfoirazabal.studyquizmaker.db.dao.testrun.TestRunDAO;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMO;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMCResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponseOption;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionTFResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

@Database(
        entities = {
                Subject.class,
                Topic.class,
                Test.class,
                TestRun.class,
                QuestionSimpleResponse.class,
                QuestionMC.class,
                QuestionOptionMC.class,
                QuestionSimple.class,
                QuestionTF.class,
                QuestionMO.class,
                QuestionOptionMO.class,
                QuestionTFResponse.class,
                QuestionMCResponse.class,
                QuestionMOResponse.class,
                QuestionMOResponseOption.class
        },
        version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract SubjectDAO subjectDAO();
    public abstract TopicDAO topicDAO();
    public abstract TestDAO testDAO();
    public abstract TestRunDAO testRunDAO();
    public abstract QuestionSimpleResponseDAO questionSimpleResponseDAO();
    public abstract QuestionMCDAO questionMCDAO();
    public abstract QuestionOptionMCDAO questionOptionMCDAO();
    public abstract QuestionSimpleDAO questionSimpleDAO();
    public abstract QuestionTFDAO questionTFDAO();
    public abstract QuestionMODAO questionMODAO();
    public abstract QuestionOptionMODAO questionOptionMODAO();
    public abstract QuestionTFResponseDAO questionTFResponseDAO();
    public abstract QuestionMCResponseDAO questionMCResponseDAO();
    public abstract QuestionMOResponseDAO questionMOResponseDAO();
    public abstract QuestionMOResponseOptionDAO questionMOResponseOptionDAO();

}
