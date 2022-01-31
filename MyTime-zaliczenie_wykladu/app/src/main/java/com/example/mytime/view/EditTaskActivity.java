package com.example.mytime.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.ColumnInfo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.mytime.R;
import com.example.mytime.databinding.ActivityEditTaskBinding;
import com.example.mytime.model.TaskEntity;
import com.example.mytime.viewmodel.TaskViewModel;

import java.util.Calendar;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {

    private ActivityEditTaskBinding binding;
    private TaskViewModel viewModel;
    private TaskEntity task;

    private int taskId;
    private Date dateDate;
    private Calendar dateCalendar;
    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("id", 0);
        setValues();

        binding.saveChanges.setOnClickListener(v -> {
            task.setTitle(binding.titleTextEdit.getText().toString());
            task.setInfo(binding.infoTextEdit.getText().toString());
            task.setDate(date);
            task.setDone(binding.checkBoxDone.isChecked());
            viewModel.update(task);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteTaskButton:
                deleteTask();
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setValues() {
        task = viewModel.getTask(taskId);
        dateDate = new Date(task.getDate());
        date = dateDate.getTime();

        binding.titleTextEdit.setText(task.getTitle());
        binding.infoTextEdit.setText(task.getInfo());

        int year = dateDate.getYear() + 1900;
        binding.datePicker.setText(dateDate.toString().substring(4, 16) +" "+ year);

        binding.checkBoxDone.setChecked(task.getDone());
    }

    public void pickDate(View view) {

        dateCalendar = Calendar.getInstance();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateCalendar.set(Calendar.HOUR_OF_DAY, dateDate.getHours());
                dateCalendar.set(Calendar.MINUTE, dateDate.getMinutes());
                dateCalendar.set(year, monthOfYear, dayOfMonth);

                new TimePickerDialog(peekAvailableContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        dateCalendar.set(Calendar.MINUTE, minute);

                        dateDate = dateCalendar.getTime();
                        date = dateDate.getTime();
                        int year = dateDate.getYear() + 1900;
                        binding.datePicker.setText(dateDate.toString().substring(4, 16) +" "+ year);
                    }
                }, dateDate.getHours(), dateDate.getMinutes(), true).show();
            }
        }, dateDate.getYear()+1900, dateDate.getMonth(), dateDate.getDate()).show();
    }

    public void deleteTask(){
        viewModel.delete(taskId);
        finish();
    }
}