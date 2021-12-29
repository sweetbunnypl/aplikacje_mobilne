package com.example.lista3_bolanowski;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class CrimeListAdapter extends RecyclerView.Adapter<CrimeListAdapter.CrimeViewHolder> {


    private LinkedList<Crime> crimeList;
    private LayoutInflater inflater;


    public CrimeListAdapter(Context context, LinkedList<Crime> crimeList) {
        inflater = LayoutInflater.from(context);
        this.crimeList = crimeList;
    }

    public void filterList(LinkedList<Crime> filteredList) {
        crimeList = filteredList;
        notifyDataSetChanged();
    }

    class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView crimeTitle;
        public final TextView crimeDate;
        public final ImageView crimeSolved;
        final CrimeListAdapter adapter;

        public CrimeViewHolder(@NonNull View itemView, CrimeListAdapter adapter) {
            super(itemView);
            crimeTitle = itemView.findViewById(R.id.crimeTitle);
            crimeDate = itemView.findViewById(R.id.crimeDate);
            crimeSolved = itemView.findViewById(R.id.crimeSolved);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Crime element = crimeList.get(position);

            Intent intent = new Intent(inflater.getContext(), CrimeActivity.class);
            intent.putExtra("id", element.getId());
            inflater.getContext().startActivity(intent);
        }
    }

    @NonNull
    @Override
    public CrimeListAdapter.CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.crime_list_item, parent, false);
        return new CrimeViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeListAdapter.CrimeViewHolder holder, int position) {
        Crime current = crimeList.get(position);
        holder.crimeTitle.setText(current.getTitle());
        holder.crimeDate.setText(current.getDate().toString());

        if (current.isSolved()){
            holder.crimeSolved.setImageResource(R.drawable.ic_baseline_done_all_24);
        }
        else {
            holder.crimeSolved.setImageResource(R.drawable.ic_baseline_report_problem_24);
        }
    }

    @Override
    public int getItemCount() {
        return crimeList.size();
    }

}
