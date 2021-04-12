package com.alfoirazabal.studyquizmaker.db.dao.testrun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionTFResponse;

@Dao
public interface QuestionTFResponseDAO {

    @Query("SELECT * FROM QuestionTFResponse WHERE testRunId = :testRunId")
    QuestionTFResponse[] getResponsesFromTestRun(String testRunId);

    @Insert
    void insert(QuestionTFResponse questionTFResponse);

    @Delete
    void delete(QuestionTFResponse questionTFResponse);

}
