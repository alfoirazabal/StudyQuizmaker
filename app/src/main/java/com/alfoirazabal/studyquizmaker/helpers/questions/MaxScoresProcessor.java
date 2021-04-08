package com.alfoirazabal.studyquizmaker.helpers.questions;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;

public class MaxScoresProcessor {

    private final double maxScoreQuestionsMC;
    private final double maxScoreQuestionsSimple;
    private final double maxScoreQuestionsTF;

    private boolean roundToTwoDecimals;

    public MaxScoresProcessor(AppDatabase db, String testId) {
        this.maxScoreQuestionsMC = db.questionOptionMCDAO().getMaxScore(testId);
        this.maxScoreQuestionsSimple = db.questionSimpleDAO().getMaxScore(testId);
        this.maxScoreQuestionsTF = db.questionTFDAO().getMaxScore(testId);
        this.roundToTwoDecimals = false;
    }

    public void setRoundToTwoDecimals(boolean roundToTwoDecimals) {
        this.roundToTwoDecimals = roundToTwoDecimals;
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public double getMaxScoreQuestionsMC() {
        if (roundToTwoDecimals) {
            return roundToTwoDecimals(this.maxScoreQuestionsMC);
        }
        else {
            return this.maxScoreQuestionsMC;
        }
    }

    public double getMaxScoreQuestionsSimple() {
        if (roundToTwoDecimals) {
            return roundToTwoDecimals(this.maxScoreQuestionsSimple);
        }
        else {
            return this.maxScoreQuestionsSimple;
        }
    }

    public double getMaxScoreQuestionsTF() {
        if (roundToTwoDecimals) {
            return roundToTwoDecimals(this.maxScoreQuestionsTF);
        }
        else {
            return this.maxScoreQuestionsTF;
        }
    }

    public double getMaxScoreFromAllQuestions() {
        double maxScore =
            this.maxScoreQuestionsMC +
            this.maxScoreQuestionsTF +
            this.maxScoreQuestionsSimple;
        if (roundToTwoDecimals) {
            return roundToTwoDecimals(maxScore);
        }
        else {
            return maxScore;
        }
    }
}
