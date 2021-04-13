package com.alfoirazabal.studyquizmaker.domain.question;

import java.util.Comparator;

public class QuestionComparators {

    public static class CompareByDateCreated implements Comparator<Question> {

        @Override
        public int compare(Question o1, Question o2) {
            return o1.getDateCreated().compareTo(o2.getDateCreated());
        }
    }

    public static class CompareByDateModified implements Comparator<Question> {

        @Override
        public int compare(Question o1, Question o2) {
            return o1.getDateModified().compareTo(o2.getDateModified());
        }
    }

}
