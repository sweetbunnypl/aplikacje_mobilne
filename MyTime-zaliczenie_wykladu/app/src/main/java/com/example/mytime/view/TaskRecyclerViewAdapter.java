package com.example.mytime.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytime.R;
import com.example.mytime.model.TaskEntity;

import java.util.Date;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {

    private MainActivity mainActivity;
    private List<TaskEntity> tasks;

    public TaskRecyclerViewAdapter(MainActivity mainActivity, List<TaskEntity> tasks) {
        this.mainActivity = mainActivity;
        this.tasks = tasks;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView taskTitle;
        private final TextView taskInfo;
        private final TextView taskDate;
        private final ImageView taskDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskInfo = itemView.findViewById(R.id.taskInfo);
            taskDate = itemView.findViewById(R.id.taskDate);
            taskDone = itemView.findViewById(R.id.taskDone);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @NonNull
    @Override
    public TaskRecyclerViewAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.TaskViewHolder holder, int position) {
        TaskEntity current = tasks.get(position);
        Date dateDate = new Date(current.getDate());
        Date currentDate = new Date();

        holder.taskTitle.setText(current.getTitle());

        if (current.getInfo().length() >= 20) {
            holder.taskInfo.setText(current.getInfo().substring(0, 17)+" ...");
        }
        else {
            holder.taskInfo.setText(current.getInfo());
        }

        int year = dateDate.getYear() + 1900;
        holder.taskDate.setText(dateDate.toString().substring(4, 16) +" "+ year);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, EditTaskActivity.class);
                intent.putExtra("id", current.getId());
                mainActivity.startActivity(intent);
            }
        });

        if (current.getDone()){
            holder.taskDone.setImageResource(R.drawable.ic_baseline_done_all_24);
        }
        else {
            if (dateDate.getTime() - currentDate.getTime() >= 604800000) {
                holder.taskDone.setImageResource(R.drawable.not_done_green);
            }
            else if (dateDate.getTime() - currentDate.getTime() >= 86400000 && dateDate.getTime() - currentDate.getTime() < 604800000) {
                holder.taskDone.setImageResource(R.drawable.not_done_yellow);
            }
            else {
                holder.taskDone.setImageResource(R.drawable.not_done_red);
            }
        }
    }

    @Override
    public int getItemCount() { return tasks.size(); }

}
