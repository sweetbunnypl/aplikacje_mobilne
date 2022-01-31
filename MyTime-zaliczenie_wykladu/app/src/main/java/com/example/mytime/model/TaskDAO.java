package com.example.mytime.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TaskEntity task);

    @Query("DELETE FROM task_table WHERE id = :mId")
    void delete(int mId);

    @Update
    void update(TaskEntity task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table ORDER BY date ASC")
    LiveData<List<TaskEntity>> getTasks();

    @Query("SELECT * FROM task_table WHERE done = 0 AND date > :todaysDate ORDER BY date ASC")
    LiveData<List<TaskEntity>> getToDoTasks(long todaysDate);

    @Query("SELECT * FROM task_table WHERE done = 1 ORDER BY date ASC")
    LiveData<List<TaskEntity>> getDoneTasks();

    @Query("SELECT * FROM task_table WHERE date < :todaysDate ORDER BY date ASC")
    LiveData<List<TaskEntity>> getExpiredTasks(long todaysDate);

    @Query("SELECT * FROM task_table WHERE id = :mId")
    TaskEntity getTask(int mId);

    @Query("SELECT id FROM task_table WHERE id = (SELECT MAX(id) FROM task_table)")
    int getLastTaskId();
}
