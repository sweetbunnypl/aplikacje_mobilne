package com.example.lista3_bolanowski;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity {

    CrimeLab crimes = CrimeLab.get(this);
    Intent intent;
    UUID crimeId;
    Crime crime;

    Button datePicker;
    EditText titleText;
    CheckBox checkBox;
    Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        intent = getIntent();
        crimeId = UUID.fromString(intent.getStringExtra("id"));
        crime = crimes.getCrime(crimeId);

        datePicker = findViewById(R.id.datePicker);
        titleText = findViewById(R.id.titleTextEdit);
        checkBox = findViewById(R.id.checkBoxSolved);

        checkBox.setChecked(crime.isSolved());
        titleText.setText(crime.getTitle());
        datePicker.setText(crime.getDate().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crime, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteCrimeButton:
                deleteCrime();
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickDate(View view) {
        final Calendar currentDate = Calendar.getInstance();

        date = Calendar.getInstance();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(Calendar.HOUR_OF_DAY, crime.getDate().getHours());
                date.set(Calendar.MINUTE, crime.getDate().getMinutes());
                date.set(year, monthOfYear, dayOfMonth);
                datePicker.setText(date.getTime().toString());
                crime.setDate(date.getTime());

                new TimePickerDialog(peekAvailableContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        datePicker.setText(date.getTime().toString());
                        crime.setDate(date.getTime());
                    }
                }, crime.getDate().getHours(), crime.getDate().getMinutes(), true).show();
            }
        }, crime.getDate().getYear()+1900, crime.getDate().getMonth(), crime.getDate().getDate()).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!titleText.getText().toString().equals("")){
            crime.setTitle(titleText.getText().toString());
        }
        crime.setSolved(checkBox.isChecked());
    }

    public void deleteCrime(){
        crimes.getCrimes().remove(crime);
        finish();
    }
}
