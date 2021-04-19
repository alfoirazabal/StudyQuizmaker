package com.alfoirazabal.studyquizmaker.db.dao.question;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;

import java.util.List;

@Dao
public interface QuestionOptionMODAO {

    @Query("SELECT * FROM questionOptionMO")
    List<QuestionOptionMO> getAll();

    @Query("SELECT * FROM questionOptionMO WHERE id = :questionMOId")
    QuestionOptionMO getById(String questionMOId);

    @Query("SELECT * FROM questionOptionMO WHERE questionMOId = :questionMOId")
    List<QuestionOptionMO> getFromQuestionMO(String questionMOId);

    @Query("SELECT MAX(qoMO.score) FROM questionOptionMO qoMO WHERE qoMO.questionMOId IN (SELECT id FROM questionMO WHERE testId = :testId)")
    double getMaxScore(String testId);

    @Insert
    void insert(QuestionOptionMO questionOptionMO);

    @Update
    void update(QuestionOptionMO questionOptionMO);

    @Delete
    void delete(QuestionOptionMO questionOptionMO);

}
