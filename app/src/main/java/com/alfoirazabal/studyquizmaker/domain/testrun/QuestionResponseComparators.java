package com.alfoirazabal.studyquizmaker.domain.testrun;

import java.util.Comparator;

public class QuestionResponseComparators {

    public static class CompareByAskOrder implements Comparator<QuestionResponse> {

        @Override
        public int compare(QuestionResponse o1, QuestionResponse o2) {
            return o1.getAskOrder() - o2.getAskOrder();
        }
    }

}
