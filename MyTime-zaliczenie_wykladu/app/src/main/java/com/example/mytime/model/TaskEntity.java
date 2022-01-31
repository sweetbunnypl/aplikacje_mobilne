package com.example.mytime.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task_table")
public class TaskEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "info")
    private String mInfo;

    @ColumnInfo(name = "date")
    private long mDate;

    @ColumnInfo(name = "done")
    private boolean mDone;


    public TaskEntity(String title, String info, long date, boolean done) {
        this.mTitle = title;
        this.mInfo = info;
        this.mDate = date;
        this.mDone = done;
    }

    public int getId() {
        return this.mId;
    }

    public String getTitle() {
        return this.mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getInfo() {
        return this.mInfo;
    }
    public void setInfo(String info) {
        this.mInfo = info;
    }

    public long getDate() { return this.mDate; }
    public void setDate(long date) { this.mDate = date; }

    public boolean getDone() { return this.mDone; }
    public void setDone(boolean done) { this.mDone = done; }
}
