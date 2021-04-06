package com.alfoirazabal.studyquizmaker.db.dao.question;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;

import java.util.List;

@Dao
public interface QuestionTFDAO {

    @Query("SELECT * FROM questionTF")
    List<QuestionTF> getAll();

    @Query("SELECT * FROM questionTF WHERE id = :questionTFId")
    QuestionTF getById(String questionTFId);

    @Query("SELECT * FROM questionTF WHERE testId = :testId")
    List<QuestionTF> getFromTest(String testId);

    @Insert
    void insert(QuestionTF questionTF);

    @Update
    void update(QuestionTF questionTF);

    @Delete
    void delete(QuestionTF questionTF);

}
