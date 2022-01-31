package com.example.mytime.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mytime.database.TaskRoom;
import com.example.mytime.model.TaskDAO;
import com.example.mytime.model.TaskEntity;

import java.util.List;

public class TaskRepository {

    private TaskDAO mTaskDao;

    public TaskRepository(Application application) {
        TaskRoom db = TaskRoom.getDatabase(application);
        mTaskDao = db.taskDAO();
    }

    public LiveData<List<TaskEntity>> getTasks() {
        return mTaskDao.getTasks();
    }

    public LiveData<List<TaskEntity>> getToDoTasks(long date) {
        return mTaskDao.getToDoTasks(date);
    }

    public LiveData<List<TaskEntity>> getDoneTasks() {
        return mTaskDao.getDoneTasks();
    }

    public LiveData<List<TaskEntity>> getExpiredTasks(long date) {
        return mTaskDao.getExpiredTasks(date);
    }

    public void insert(TaskEntity task) {
        mTaskDao.insert(task);
    }

    public void delete(int mId) {
        mTaskDao.delete(mId);
    }

    public void update(TaskEntity task) {
        mTaskDao.update(task);
    }

    public TaskEntity getTask(int mId) {
        return mTaskDao.getTask(mId);
    }

    public int getLastTaskId() { return mTaskDao.getLastTaskId();}

}
