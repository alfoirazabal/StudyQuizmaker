package com.alfoirazabal.studyquizmaker.db.dao.testrun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;

@Dao
public interface QuestionSimpleResponseDAO {

    @Query("SELECT * FROM questionSimpleResponse WHERE testRunId = :testRunId")
    QuestionSimpleResponse[] getResponsesFromTestRun(String testRunId);

    @Insert
    void insert(QuestionSimpleResponse questionSimpleResponse);

    @Delete
    void delete(QuestionSimpleResponse questionSimpleResponse);

}
