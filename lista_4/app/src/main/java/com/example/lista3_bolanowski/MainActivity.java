package com.example.lista3_bolanowski;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CrimeListAdapter crimeListAdapter;
    private CrimeLab crimes = CrimeLab.get(this);
    private LinkedList<Crime> crimeList = crimes.getCrimes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        crimeListAdapter = new CrimeListAdapter(this, crimeList);
        recyclerView.setAdapter(crimeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void CreateCrime() {
        Crime newCrime = new Crime();
        newCrime.setTitle("Please, edit a created crime.");
        newCrime.setId(UUID.randomUUID());
        newCrime.setDate(new Date());
        newCrime.setSolved(false);
        crimeList.add(newCrime);
        crimeListAdapter.notifyDataSetChanged();

        Intent intent = new Intent(this, CrimeActivity.class);
        intent.putExtra("id", newCrime.getId().toString());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        crimeListAdapter.notifyDataSetChanged();
    }

}