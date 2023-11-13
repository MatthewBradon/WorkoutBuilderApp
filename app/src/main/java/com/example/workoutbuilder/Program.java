package com.example.workoutbuilder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName = "program_table")
@TypeConverters(WorkoutListConverter.class)
public class Program {

    @PrimaryKey(autoGenerate = true)
    private int id;
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
        json.put("name", name);
        json.put("description", description);

        return json;
    }

    public static Program fromJSON(JSONObject json) throws JSONException {
        ArrayList<Workout> workouts = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("workouts");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonWorkout = jsonArray.getJSONObject(i);
            workouts.add(Workout.fromJSON(jsonWorkout));
        }
        System.out.println(json.getString("name"));
        return new Program(workouts, json.getString("name"), json.getString("description"));
    }

    @Override
    public String toString() {
        return "Program{" +
                "workouts=" + workouts +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
