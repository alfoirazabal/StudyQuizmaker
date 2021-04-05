package com.alfoirazabal.studyquizmaker;

import android.content.Context;

import java.io.File;

public abstract class AppConstants {

    private static final boolean PREFER_EXTERNAL_SD_CARD = true;

    private static final String DATABASE_FILENAME = "Database.sqlite";

    private static String CURRENT_DATABASE_LOCATION = null;

    public static String getDBLocation(Context context) {
        if (CURRENT_DATABASE_LOCATION == null) {
            File databasePath;
            if (PREFER_EXTERNAL_SD_CARD) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    File[] externalFilesDirs = context.getExternalFilesDirs("/");
                    if (externalFilesDirs.length >= 2 && externalFilesDirs[1] != null) {
                        databasePath = externalFilesDirs[1];
                    }
                    else {
                        databasePath = externalFilesDirs[0];
                    }
                }
                else {
                    databasePath = context.getExternalFilesDir("/");
                }
            }
            else {
                databasePath = context.getExternalFilesDir("/");
            }
            CURRENT_DATABASE_LOCATION = databasePath.toString() + "/" + DATABASE_FILENAME;
        }
        return CURRENT_DATABASE_LOCATION;
    }

}
