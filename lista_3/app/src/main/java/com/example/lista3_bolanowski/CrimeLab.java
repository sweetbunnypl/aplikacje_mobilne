package com.example.lista3_bolanowski;

import android.content.Context;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private LinkedList<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Crime crime = new Crime();
            crime.setDate(new Date());
            crime.setId(UUID.randomUUID());
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }

    public LinkedList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
