package com.alfoirazabal.studyquizmaker.domain.testrun;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.Question;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionActivity;

import java.io.Serializable;

public interface QuestionResponse extends Serializable {

    String getQuestionId();
    boolean isAnswered();
    double getScore();
    Question getQuestion(AppDatabase db);
    String getAnswered(AppDatabase db);
    Class<? extends AnswerQuestionActivity> getAnswerQuestionClass();
    int getAskOrder();
    void setAskOrder(int askOrder);

    void setTestRunId(String testRunId);
    void setQuestion(Question question);

    void insertToDb(AppDatabase db);

}
