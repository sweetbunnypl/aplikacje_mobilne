package com.example.lista3_bolanowski;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.viewpager2.widget.ViewPager2;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.LinkedList;
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
    int currentPosition;

    private ViewPager2 viewPager2;
    private boolean startEdit;
    TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_2);

        intent = getIntent();
        crimeId = UUID.fromString(intent.getStringExtra("id"));
        crime = crimes.getCrime(crimeId);

        viewPager2 = findViewById(R.id.viewPager2);

        CrimePagerAdapter adapter = new CrimePagerAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(crimes.getCrimePosition(crimeId));
        currentPosition = crimes.getCrimePosition(crimeId);
        startEdit = false;
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                startEdit = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (!startEdit) {
                    try {
                        titleText.removeTextChangedListener(textWatcher);
                    } catch (NullPointerException e) {
                        System.out.println(e);
                    }
                    changeEditText();
                }
                startEdit = true;
            }
        });
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
                return super.onOptionsItemSelected(item);
            case R.id.firstElementButton:
                firstCrime();
                return super.onOptionsItemSelected(item);
            case R.id.lastElementButton:
                lastCrime();
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickDate(View view) {
        changeCurrentCrime();
        datePicker = findViewById(R.id.datePicker);
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

    public void changeEditText() {
        changeCurrentCrime();
        textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(titleText.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        titleText = findViewById(R.id.titleTextEdit);
        titleText.addTextChangedListener(textWatcher);
    }

    public void changeSolved(View view) {
        changeCurrentCrime();
        checkBox = findViewById(R.id.checkBoxSolved);
        crime.setSolved(checkBox.isChecked());

    }

    public void deleteCrime(){
        crimes.getCrimes().remove(crime);
        finish();
    }

    public void changeCurrentCrime() {
        crimeId = crimes.getCrimeUUID(currentPosition);
        crime = crimes.getCrime(crimeId);
    }

    public void firstCrime(){
        changeCurrentCrime();
        viewPager2.setCurrentItem(0);
        changeCurrentCrime();
    }

    public void lastCrime(){
        changeCurrentCrime();
        viewPager2.setCurrentItem(crimes.getCrimes().size()-1);
        changeCurrentCrime();
    }
}
