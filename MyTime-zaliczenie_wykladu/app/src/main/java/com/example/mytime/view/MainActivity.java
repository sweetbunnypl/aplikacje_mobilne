package com.example.mytime.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelLazy;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.databinding.ActivityEditTaskBinding;
import com.example.mytime.databinding.ActivityMainBinding;
import com.example.mytime.model.TaskEntity;
import com.example.mytime.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel viewModel;
    private ActivityMainBinding binding;
    private TaskRecyclerViewAdapter adapter;
    private int currentLoadFilter;
    private List<TaskEntity> currentTasksList;
    private String currentText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CharSequence sequence = DateFormat.format("MMM dd, yyyy", new Date().getTime());//DateFormat.getInstance().format("MMMM d, YYYY");
        binding.todayTextView.setText(sequence);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TaskViewModel.class);

        currentText = "";
        currentLoadFilter = 0;

        viewModel.getTasks().observe(this, taskEntities -> {
            if(currentLoadFilter == 0) {
                binding.allFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            }
            binding.allFilter.setOnClickListener(v -> {
                currentLoadFilter = 0;
                binding.allFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            });
        });

        viewModel.getToDoTasks(new Date().getTime()).observe(this, taskEntities -> {
            if(currentLoadFilter == 1) {
                binding.toDoFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            }
            binding.toDoFilter.setOnClickListener(v -> {
                currentLoadFilter = 1;
                binding.toDoFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            });
        });

        viewModel.getDoneTasks().observe(this, taskEntities -> {
            if(currentLoadFilter == 2) {
                binding.doneFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            }
            binding.doneFilter.setOnClickListener(v -> {
                currentLoadFilter = 2;
                binding.doneFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            });
        });

        viewModel.getExpiredTasks(new Date().getTime()).observe(this, taskEntities -> {
            if(currentLoadFilter == 3) {
                binding.expiredFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            }
            binding.expiredFilter.setOnClickListener(v -> {
                currentLoadFilter = 3;
                binding.expiredFilter.setChecked(true);
                currentTasksList = taskEntities;
                filter(currentText);
            });
        });
    }

    private void setAdapter(List<TaskEntity> taskEntities) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskRecyclerViewAdapter(MainActivity.this, taskEntities);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTaskButton:
                CreateTask();
                return super.onOptionsItemSelected(item);
            case R.id.searchTaskButton:
                SearchTask(item);
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void CreateTask() {
        viewModel.insert(new TaskEntity("New task.", "Task info.", new Date().getTime(), false));
        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
        intent.putExtra("id", (int) viewModel.getLastTaskId());
        startActivity(intent);
    }

    public void SearchTask(MenuItem item) {
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setQueryHint("Search for a task.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                currentText = newText;
                return false;
            }
        });
    }

    private void filter(String text) {

        List<TaskEntity> filteredList = new ArrayList<>();

        for (TaskEntity item : currentTasksList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            setAdapter(filteredList);
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            setAdapter(filteredList);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            currentLoadFilter = savedInstanceState.getInt("current_load_filter");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_load_filter", currentLoadFilter);
    }
}