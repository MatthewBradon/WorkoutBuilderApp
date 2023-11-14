package com.example.workoutbuilder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Workout> workouts;

    private String programJSONString;

    public WorkoutAdapter(Context context, ArrayList<Workout> workouts, String programJSONString) {
        this.workouts = workouts;
        this.context = context;
        this.programJSONString = programJSONString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_rv_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Workout currentWorkout = workouts.get(position);

        holder.workoutNameTV.setText(currentWorkout.getName());
        // Initialize and set up the ExerciseAdapter for the RecyclerView
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(currentWorkout.getExercises(), context, programJSONString, currentWorkout.getName());
        holder.exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.exerciseRecyclerView.setAdapter(exerciseAdapter);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView exerciseRecyclerView;

        public TextView workoutNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutNameTV = itemView.findViewById(R.id.workoutNameTV);
            exerciseRecyclerView = itemView.findViewById(R.id.exerciseRecyclerView);
        }
    }
}
