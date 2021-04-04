package com.alfoirazabal.studyquizmaker.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.Topic;

import java.util.List;

@Dao
public interface TopicDAO {

    @Query("SELECT * FROM topic")
    List<Topic> getAll();

    @Query("SELECT name FROM topic")
    List<String> getAllNames();

    @Query("SELECT * FROM topic WHERE id = :topicId")
    Topic getById(String topicId);

    @Query("SELECT * FROM topic WHERE subjectId = :subjectId")
    List<Topic> getFromSubject(String subjectId);

    @Update
    void update(Topic topic);

    @Insert
    void insert(Topic topic);

    @Delete
    void delete(Topic topic);

}
