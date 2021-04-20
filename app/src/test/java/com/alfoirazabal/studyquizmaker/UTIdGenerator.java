package com.alfoirazabal.studyquizmaker;

import com.alfoirazabal.studyquizmaker.helpers.IdGenerator;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class UTIdGenerator {

    @Test
    public void testIdGeneratorEntropy() {
        int idsToGenerate = 10000000;
        int acceptableAmountOfCollisions = 30;
        int keyLength = 8;
        HashSet<String> ids = new HashSet<>();
        for (int i = 0 ; i < idsToGenerate ; i++) {
            String newId = IdGenerator.generateId(keyLength);
            ids.add(newId);
        }
        int collisionsFound = idsToGenerate - ids.size();
        boolean thresholdBroken = acceptableAmountOfCollisions - collisionsFound < 0;
        String collissionsFoundDescription = "The number of collissions was " + collisionsFound +
                " with a threshold of " + acceptableAmountOfCollisions + ".";
        System.out.println(collissionsFoundDescription);
        Assert.assertFalse(collissionsFoundDescription, thresholdBroken);
    }

}
