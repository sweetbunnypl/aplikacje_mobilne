package com.example.lista3_bolanowski;

import java.util.Date;
import java.util.UUID;

public class Crime {


    private int mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;


    public Crime() {}

    public Crime(int mId, String mTitle, Date mDate, boolean mSolved) {
        this(mTitle, mDate, mSolved);
        this.mId = mId;
    }

    public Crime(String mTitle, Date mDate, boolean mSolved) {
        this.mTitle = mTitle;
        this.mDate = mDate;
        this.mSolved = mSolved;
    }

    public void setId(int mId) { this.mId = mId; }
    public int getId() { return mId; }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public String getTitle() {
        return mTitle;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }
    public Date getDate() {
        return mDate;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }
    public boolean isSolved() {
        return mSolved;
    }
}
