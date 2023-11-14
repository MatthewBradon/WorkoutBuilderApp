package com.example.workoutbuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    private ArrayList<Exercise> exercises;

    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_rv_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise currentExercise = exercises.get(position);

        holder.exerciseNameTextView.setText(currentExercise.getName());
        holder.rpeTextView.setText(String.valueOf(currentExercise.getRpe()));
        holder.repTextView.setText(String.valueOf(currentExercise.getReps()));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exerciseNameTextView;
        public TextView rpeTextView;
        public TextView repTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseName);
            rpeTextView = itemView.findViewById(R.id.rpeTV);
            repTextView = itemView.findViewById(R.id.repTV);
        }
    }
}
