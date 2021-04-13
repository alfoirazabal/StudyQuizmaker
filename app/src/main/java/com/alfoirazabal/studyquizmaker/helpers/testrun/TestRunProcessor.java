package com.alfoirazabal.studyquizmaker.helpers.testrun;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

import java.util.Date;

public class TestRunProcessor {

    private TestRun testRun;
    private Date finishedDateTime;

    public TestRunProcessor(TestRun testRun) {
        this.testRun = testRun;
        this.finishedDateTime = new Date();
    }

    public void setFinishedDateTime(Date finishedDateTime) {
        this.finishedDateTime = finishedDateTime;
    }

    public void saveTestRunToDatabase(AppDatabase db) {
        int numberOfAnsweredQuestions = 0;
        double totalScore = 0;
        double totalScored = 0;
        this.testRun.dateTimeFinished = this.finishedDateTime;
        for (int i = 0; i < this.testRun.questionResponses.length ; i++) {
            QuestionResponse currentQuestionResponse =
                    this.testRun.questionResponses[i];
            if (currentQuestionResponse.isAnswered()) {
                numberOfAnsweredQuestions++;
            }
            totalScore += currentQuestionResponse.getQuestion(db).getScore();
            totalScored += currentQuestionResponse.getScore();
        }
        this.testRun.numberOfAnsweredQuestions = numberOfAnsweredQuestions;
        this.testRun.scoredPercentage = Math.round((totalScored / totalScore) * 100);
        db.testRunDAO().insert(testRun);
        for (int i = 0; i < this.testRun.questionResponses.length ; i++) {
            this.testRun.questionResponses[i].insertToDb(db);
        }
    }

}
