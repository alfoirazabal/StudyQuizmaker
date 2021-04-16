package com.alfoirazabal.studyquizmaker.db.dao.testrun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMCResponse;

@Dao
public interface QuestionMCResponseDAO {

    @Query("SELECT * FROM QuestionMCResponse WHERE testRunId = :testRunId")
    QuestionMCResponse[] getResponsesFromTestRun(String testRunId);

    @Insert
    void insert(QuestionMCResponse questionMCResponse);

    @Delete
    void delete(QuestionMCResponse questionMCResponse);

}
