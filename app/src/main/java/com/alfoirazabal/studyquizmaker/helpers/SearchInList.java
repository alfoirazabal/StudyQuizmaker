package com.alfoirazabal.studyquizmaker.helpers;

import java.util.Iterator;
import java.util.List;

public class SearchInList {

    private final List list;

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

    public void deleteIgnoreCase(String stringToSearchFor) {
        Iterator<String> itStringList = (Iterator<String>) list.iterator();
        boolean stringContainedInList = false;
        String current = null;
        while (!stringContainedInList && itStringList.hasNext()) {
            current = itStringList.next();
            stringContainedInList = current.equalsIgnoreCase(stringToSearchFor);
        }
        if (stringContainedInList) {
            this.list.remove(current);
        }
    }

}
