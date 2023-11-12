package com.example.workoutbuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/*
    Room can only store primitive data types so this class
    is an intermediary between room and workout by converting
    exercises ArrayList to a json string which can be stored
 */
public class WorkoutRoomAdapter {

    private String name;
    private String description;
    private String exercisesJson;

    // Convert exercises to JSON string so it can be stored in room
    public void setExerciseJson(ArrayList<Exercise> exercises) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Exercise exercise : exercises) {
                JSONObject exerciseJson = new JSONObject();
                exerciseJson.put("name", exercise.getName());
                exerciseJson.put("rpe", exercise.getRpe());
                exerciseJson.put("reps", exercise.getReps());
                jsonArray.put(exerciseJson);
            }
            this.exercisesJson = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Convert JSON string to list arraylist of exercises
    public ArrayList<Exercise> jsonToExercises(String json) {
        try {
            ArrayList<Exercise> exercises = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            //Parses each JSON object for exercise data adding it to array list
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject exerciseJson = jsonArray.getJSONObject(i);
                String exerciseName = exerciseJson.getString("name");
                int exerciseRpe = exerciseJson.getInt("rpe");
                int exerciseReps = exerciseJson.getInt("reps");
                Exercise exercise = new Exercise(exerciseName, exerciseRpe, exerciseReps);
                exercises.add(exercise);
            }
            return exercises;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
