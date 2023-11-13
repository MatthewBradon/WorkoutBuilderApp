package com.example.workoutbuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Program {

    private ArrayList<Workout> workouts;

    private String name;

    private String description;

    public Program(ArrayList<Workout> workouts, String name, String description) {
        this.workouts = workouts;
        this.name = name;
        this.description = description;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private JSONArray workoutsToJSON() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(Workout workout : workouts){
            jsonArray.put(workout.toJSON());
        }
        return jsonArray;
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("workouts", workoutsToJSON());

        return json;
    }
}
