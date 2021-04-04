package com.alfoirazabal.studyquizmaker.helpers;

import java.util.Iterator;
import java.util.List;

public class SearchInList {

    private List list;

    public<T> SearchInList(List<T> list) {
        this.list = list;
    }

    public boolean containsStringIgnoreCase(String stringToSearchFor) {
        Iterator<String> itStringList = (Iterator<String>) list.iterator();
        boolean stringContainedInList = false;
        while (!stringContainedInList && itStringList.hasNext()) {
            String current = itStringList.next();
            stringContainedInList = current.equalsIgnoreCase(stringToSearchFor);
        }
        return stringContainedInList;
    }

}
