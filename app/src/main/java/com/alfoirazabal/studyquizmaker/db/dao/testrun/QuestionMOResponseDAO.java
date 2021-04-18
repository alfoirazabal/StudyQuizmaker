package com.alfoirazabal.studyquizmaker.db.dao.testrun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponse;

@Dao
public interface QuestionMOResponseDAO {

    @Query("SELECT * FROM QuestionMOResponse WHERE testRunId = :testRunId")
    QuestionMOResponse[] getResponsesFromTestRun(String testRunId);

    @Insert
    void insert(QuestionMOResponse questionMOResponse);

    @Delete
    void delete(QuestionMOResponse questionMOResponse);

}
