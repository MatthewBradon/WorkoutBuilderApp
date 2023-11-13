package com.example.workoutbuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Workout {
    private ArrayList<Exercise> exercises;

    // Constructor
    public Workout(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    // Getter and Setter methods for exercises
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    // Method to add an exercise to the workout
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }


    public JSONArray exercisesToJSON() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Exercise exercise : exercises) {
            jsonArray.put(exercise.toJSON());
        }
        return jsonArray;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("exercises", exercisesToJSON());
        return json;
    }

    public static Workout fromJSON(JSONObject json) throws JSONException {
        ArrayList<Exercise> exercises = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("exercises");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonExercise = jsonArray.getJSONObject(i);
            exercises.add(Exercise.fromJSON(jsonExercise));
        }
        return new Workout(exercises);
    }

    //Display the workout as a string

    @Override
    public String toString() {
        return "Workout{" +
                "exercises=" + exercises +
                '}';
    }
}
