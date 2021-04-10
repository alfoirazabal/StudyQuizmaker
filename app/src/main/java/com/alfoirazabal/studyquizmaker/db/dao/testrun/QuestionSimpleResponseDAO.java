package com.alfoirazabal.studyquizmaker.db.dao.testrun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;

import java.util.List;

@Dao
public interface QuestionSimpleResponseDAO {

    @Query("SELECT * FROM questionSimpleResponse WHERE testRunId = :testRunId")
    List<QuestionSimpleResponse> getResponsesFromTestRun(String testRunId);

    @Insert
    void insert(QuestionSimpleResponse questionSimpleResponse);

    @Delete
    void delete(QuestionSimpleResponse questionSimpleResponse);

}
