package com.example.workoutbuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class Exercise {
    private String name;
    private int set;
    private int reps;

    private String type;
    private String muscleGroup;

    // Constructor
    public Exercise(String name, int set, int reps, String type, String muscleGroup) {
        this.name = name;
        this.set = set;
        this.reps = reps;
        this.type = type;
        this.muscleGroup = muscleGroup;
    }

    // Getter and Setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter methods for set
    public int getset() {
        return set;
    }

    public void setset(int set) {
        this.set = set;
    }

    // Getter and Setter methods for reps
    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("set", set);
        json.put("reps", reps);
        json.put("type", type);
        json.put("muscleGroup", muscleGroup);
        return json;
    }

    // Display the exercise as a string


    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", set=" + set +
                ", reps=" + reps +
                ", type='" + type + '\'' +
                ", muscleGroup='" + muscleGroup + '\'' +
                '}';
    }

    public static Exercise fromJSON(JSONObject json) throws JSONException {
        String name = json.getString("name");
        int set = json.getInt("set");
        int reps = json.getInt("reps");
        String type = json.getString("type");
        String muscleGroup = json.getString("muscleGroup");
        return new Exercise(name, set, reps, type, muscleGroup);
    }
}
