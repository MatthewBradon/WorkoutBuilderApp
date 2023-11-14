package com.example.workoutbuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    private ArrayList<Exercise> exercises;

    private Context context;

    private String programStringJSON;

    private String workoutName;



    public ExerciseAdapter(ArrayList<Exercise> exercises, Context context, String programStringJSON, String workoutName) {
        this.exercises = exercises;
        this.context = context;
        this.programStringJSON = programStringJSON;
        this.workoutName = workoutName;
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
        holder.setTextView.setText(String.valueOf(currentExercise.getset()));
        holder.repTextView.setText(String.valueOf(currentExercise.getReps()));

        // Set OnLongClickListener for the TextView
        holder.exerciseNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Handle the long click event
                try{
                    Intent intent = new Intent(context, ChangeExercise.class);
                    intent.putExtra("programJSONString", programStringJSON);
                    intent.putExtra("workoutName", workoutName);
                    intent.putExtra("exerciseToEditJSON", currentExercise.toJSON().toString());
                    //context.startActivity(intent);
                    ((Activity) context).startActivityForResult(intent, DisplayedWorkout.EDIT_EXERCISE_REQUEST);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public Exercise getItem(int position) {
        return exercises.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exerciseNameTextView;
        public TextView setTextView;
        public TextView repTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseName);
            setTextView = itemView.findViewById(R.id.setTV);
            repTextView = itemView.findViewById(R.id.repTV);
        }
    }
}
