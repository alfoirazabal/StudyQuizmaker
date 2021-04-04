package com.alfoirazabal.studyquizmaker;

import android.os.Environment;

public abstract class AppConstants {

    private static final String APPLICATION_NAME = "Study Quizmaker";
    private static final String DATABASE_FILENAME = "Database.sqlite";
    public static final String DATABASE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/AppDataAlfoIrazabal/" + APPLICATION_NAME;
    public static final String DATABASE_LOCATION = DATABASE_PATH + "/" + DATABASE_FILENAME;

}
