package com.alfoirazabal.studyquizmaker.helpers;

import java.util.Random;

public class ArrayShuffler<T> {

    public ArrayShuffler() {

    }

    public void shuffleFisherYates(T[] array) {
        int index;
        Random random = new Random();
        for (int i = 0 ; i < array.length ; i++) {
            index = random.nextInt(i + 1);
            if (index != i) {
                T temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }
    }

}
