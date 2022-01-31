package com.example.mytime.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mytime.model.TaskEntity;
import com.example.mytime.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository mRepository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
    }

    public LiveData<List<TaskEntity>> getTasks() {
        return mRepository.getTasks();
    }

    public LiveData<List<TaskEntity>> getToDoTasks(long date) {
        return mRepository.getToDoTasks(date);
    }

    public LiveData<List<TaskEntity>> getDoneTasks() {
        return mRepository.getDoneTasks();
    }

    public LiveData<List<TaskEntity>> getExpiredTasks(long date) {
        return mRepository.getExpiredTasks(date);
    }

    public TaskEntity getTask(int mId) { return mRepository.getTask(mId); }

    public int getLastTaskId() { return mRepository.getLastTaskId(); }

    public void insert(TaskEntity task) {
        mRepository.insert(task);
    }

    public void delete(int mId) {
        mRepository.delete(mId);
    }

    public void update(TaskEntity task) { mRepository.update(task); }
}
