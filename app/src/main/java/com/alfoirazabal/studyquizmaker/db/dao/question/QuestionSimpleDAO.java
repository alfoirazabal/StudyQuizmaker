package com.alfoirazabal.studyquizmaker.db.dao.question;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;

import java.util.List;

@Dao
public interface QuestionSimpleDAO {

    @Query("SELECT * FROM questionSimple")
    List<QuestionSimple> getAll();

    @Query("SELECT * FROM questionsimple WHERE id = :questionSimpleId")
    QuestionSimple getById(String questionSimpleId);

    @Query("SELECT * FROM questionsimple WHERE testId = :testId")
    List<QuestionSimple> getFromTest(String testId);

    @Query("SELECT title FROM questionsimple WHERE testId = :testId")
    List<String> getAllTitles(String testId);

    @Query("SELECT COUNT(*) FROM questionsimple WHERE testId = :testId")
    int getCountFromTest(String testId);

    @Query("SELECT MAX(score) FROM QuestionSimple WHERE testId = :testId")
    double getMaxScore(String testId);

    @Insert
    void insert(QuestionSimple questionSimple);

    @Update
    void update(QuestionSimple questionSimple);

    @Delete
    void delete(QuestionSimple questionSimple);

}
