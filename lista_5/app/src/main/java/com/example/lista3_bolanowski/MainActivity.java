package com.example.lista3_bolanowski;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private CrimeListAdapter crimeListAdapter;
    private DBHandler dbHandler;
    private LinkedList<Crime> crimeList = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this);
        crimeList = dbHandler.getCrimesList();

        recyclerView = findViewById(R.id.recyclerView);
        crimeListAdapter = new CrimeListAdapter(this, crimeList);
        recyclerView.setAdapter(crimeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        dbHandler.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCrimeButton:
                CreateCrime();
                return super.onOptionsItemSelected(item);
            case R.id.searchCrimeButton:
                SearchCrime(item);
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void CreateCrime() {
        dbHandler.addCrime(new Crime("Please, edit a created crime.", new Date(), false));
        crimeList = dbHandler.getCrimesList();
        crimeListAdapter.filterList(crimeList);

        Intent intent = new Intent(this, CrimeActivity.class);
        intent.putExtra("id", crimeList.getLast().getId());
        startActivity(intent);
    }

    public void SearchCrime(MenuItem item) {
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setQueryHint("Search for a crime.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        LinkedList<Crime> filteredList = new LinkedList<>();

        for (Crime item : crimeList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            crimeListAdapter.filterList(filteredList);
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            crimeListAdapter.filterList(filteredList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        crimeList = dbHandler.getCrimesList();
        crimeListAdapter.filterList(crimeList);
    }

}