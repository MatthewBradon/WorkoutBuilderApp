package com.example.workoutbuilder;

import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkoutListConverter {

    @TypeConverter
    public static ArrayList<Workout> fromString(String value) {
        try {
            // Convert JSON string to ArrayList<Workout>
            JSONArray jsonArray = new JSONArray(value);
            ArrayList<Workout> workouts = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonWorkout = jsonArray.getJSONObject(i);
                workouts.add(Workout.fromJSON(jsonWorkout)); // Assuming you have a method to create Workout from JSON
            }
            return workouts;
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the exception, e.g., return an empty list or throw a RuntimeException
            return new ArrayList<>();
        }
    }

    @TypeConverter
    public static String toString(ArrayList<Workout> list) {
        try {
            JSONArray jsonArray = workoutsToJSON(list);
            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static JSONArray workoutsToJSON(ArrayList<Workout> workouts) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Workout workout : workouts) {
            jsonArray.put(workout.toJSON());
        }
        return jsonArray;
    }
}
