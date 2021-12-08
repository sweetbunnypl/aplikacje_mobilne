package com.example.lista3_bolanowski;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class CrimePagerAdapter extends RecyclerView.Adapter<CrimePagerAdapter.ViewHolder> {

    private Context context;
    private LinkedList<Crime> crimeList;

    public CrimePagerAdapter(Context context) {
        crimeList = CrimeLab.mCrimes;
        this.context = context;
    }

    @NonNull
    @Override
    public CrimePagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_crime, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrimePagerAdapter.ViewHolder holder, int position) {

        Crime current = crimeList.get(position);
        holder.datePicker.setText(current.getDate().toString());
        holder.checkBox.setChecked(current.isSolved());
        holder.titleText.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return crimeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button datePicker;
        private EditText titleText;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datePicker = itemView.findViewById(R.id.datePicker);
            titleText = itemView.findViewById(R.id.titleTextEdit);
            checkBox = itemView.findViewById(R.id.checkBoxSolved);
        }
    }
}
