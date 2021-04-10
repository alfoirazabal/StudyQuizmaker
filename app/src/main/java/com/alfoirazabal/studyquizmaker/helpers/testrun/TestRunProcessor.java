package com.alfoirazabal.studyquizmaker.helpers.testrun;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

import java.util.Date;

public class TestRunProcessor {

    private TestRun testRun;

    public TestRunProcessor(TestRun testRun) {
        this.testRun = testRun;
    }

    public void saveTestRunToDatabase(AppDatabase db) {
        int numberOfAnsweredQuestions = 0;
        double totalScore = 0;
        double totalScored = 0;
        this.testRun.dateTimeFinished = new Date();
        for (int i = 0 ; i < this.testRun.questionSimpleResponses.length ; i++) {
            QuestionSimpleResponse currentQuestionSimpleResponse =
                    this.testRun.questionSimpleResponses[i];
            if (currentQuestionSimpleResponse.isAnswered) {
                numberOfAnsweredQuestions++;
            }
            totalScore += db.questionSimpleDAO().getById(
                    currentQuestionSimpleResponse.questionSimpleId
            ).score;
            totalScored += currentQuestionSimpleResponse.score;
        }
        this.testRun.numberOfAnsweredQuestions = numberOfAnsweredQuestions;
        this.testRun.scoredPercentage = Math.round((totalScored / totalScore) * 100);
        db.testRunDAO().insert(testRun);
        for (int i = 0 ; i < this.testRun.questionSimpleResponses.length ; i++) {
            db.questionSimpleResponseDAO().insert(this.testRun.questionSimpleResponses[i]);
        }
    }

}
