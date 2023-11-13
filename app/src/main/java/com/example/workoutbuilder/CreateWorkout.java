package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;


import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CreateWorkout extends AppCompatActivity {

    private Button stepCounterBtn, workoutsBtn, createBtn;
    private TextInputEditText workoutName, workoutDescription;
    private Spinner spinner;
    private ArrayList<CheckBox> daysOfWeek;

    private enum ProgramType {
        FULL_BODY, UPPER_LOWER, PUSH_PULL_LEGS, PUSH_PULL_LEGS_UPPER_LOWER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        stepCounterBtn = findViewById(R.id.btnStepCounter);
        workoutsBtn = findViewById(R.id.btnWorkouts);
        createBtn = findViewById(R.id.createbutton);
        spinner = findViewById(R.id.dropdownMenu);
        workoutName = findViewById(R.id.NameTextInput);
        workoutDescription = findViewById(R.id.DescTextInput);

        daysOfWeek.add(findViewById(R.id.checkboxMonday));
        daysOfWeek.add(findViewById(R.id.checkboxTuesday));
        daysOfWeek.add(findViewById(R.id.checkboxWednesday));
        daysOfWeek.add(findViewById(R.id.checkboxThursday));
        daysOfWeek.add(findViewById(R.id.checkboxFriday));
        daysOfWeek.add(findViewById(R.id.checkboxSaturday));
        daysOfWeek.add(findViewById(R.id.checkboxSunday));


        // Set up the ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        workoutsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stepCounterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateWorkout.this, StepCounter.class);
                startActivity(intent);
            }
        });


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //Calculate number of available days and the max consecutive rest days
                //Both of these are used to determine the number of workouts and split to generate
                int numOfAvailableDays = 0;
                int maxConsecutiveRestDays = 0;
                int currentConsecutiveRestDays = 0;
                for(CheckBox day : daysOfWeek){
                    if(day.isChecked()){
                        numOfAvailableDays++;
                        currentConsecutiveRestDays = 0;
                    }else{
                        currentConsecutiveRestDays++;
                        if(currentConsecutiveRestDays > maxConsecutiveRestDays){
                            maxConsecutiveRestDays = currentConsecutiveRestDays;
                        }
                    }
                }

                if(numOfAvailableDays == 0 || numOfAvailableDays > 6){
                    //Display error message
                    return;
                }

                //Generate the workout

                
                finish();
            }
        });
    }

    private Program generateProgram(int numOfAvailableDays, int maxConsecutiveRestDays) {

        ProgramType programType = ProgramType.FULL_BODY;
        
        String filePath = "exercises.json";
        ArrayList<Exercise> allExercises = new ArrayList<>();
        ArrayList<Workout> workouts;
        ArrayList<Exercise> exercises;

        //Calculate the program split and type of workouts
        String trainingType = spinner.getSelectedItem().toString();
        int accessoryRepCount = trainingType.equals("Strength") ? 8 : 12;
        int compoundRepCount = trainingType.equals("Strength") ? 5 : 8;

        switch (numOfAvailableDays) {
            case 1:
                programType = ProgramType.FULL_BODY;
                break;
            case 2:
                programType = (maxConsecutiveRestDays > 3) ? ProgramType.FULL_BODY : ProgramType.UPPER_LOWER;
                break;
            case 3:
            case 6:
                programType = ProgramType.PUSH_PULL_LEGS;
                break;
            case 4:
                programType = ProgramType.UPPER_LOWER;
                break;
            case 5:
                programType = ProgramType.PUSH_PULL_LEGS_UPPER_LOWER;
                break;
        }
        // Create an ArrayList to store all exercises from JSON File
        try (InputStream inputStream = CreateWorkout.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.out.println("File not found: " + filePath);
            } else {
                try {
                    //Create a JSON array from the JSON file
                    JSONArray jsonArray = new JSONArray(new InputStreamReader(inputStream));

                    //Go through each exercise in the JSON array and add it to the ArrayList
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonExercise = jsonArray.getJSONObject(i);

                        String name = jsonExercise.getString("name");
                        String type = jsonExercise.getString("type");
                        String muscleGroup = jsonExercise.getString("muscle_group");

                        int repCount = type.equals("Compound") ? compoundRepCount : accessoryRepCount;
                        int rpe = type.equals("Compound") ? 9 : 7;

                        Exercise exercise = new Exercise(name, repCount, rpe, type, muscleGroup);
                        allExercises.add(exercise);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Generate the workout based on the number of available days
        switch (programType) {
            case FULL_BODY:
                workouts = generateFullBodyWorkouts(allExercises, numOfAvailableDays);
                break;
            case UPPER_LOWER:
                workouts = generateUpperLowerWorkouts(allExercises, numOfAvailableDays);
                break;
            case PUSH_PULL_LEGS:
                workouts = generatePushPullLegsWorkouts(allExercises, numOfAvailableDays);
                break;
            case PUSH_PULL_LEGS_UPPER_LOWER:
                workouts = generatePushPullLegsUpperLowerWorkouts(allExercises, numOfAvailableDays);
                break;
            default:
                workouts = null;
        }

        //Create the program
        return new Program(workouts, workoutName.getText().toString(), workoutDescription.getText().toString());

    }
/*
    private ArrayList<Exercise> createExerciseList(String[] names, ArrayList<Exercise> allExercises) {
        ArrayList<Exercise> exercises = new ArrayList<>();

        // Convert the names array to a list
        List<String> namesList = Arrays.asList(names);

        // Go through allExercises and add the ones with names in the names ArrayList to exercises
        for (Exercise exercise : allExercises) {
            if (namesList.contains(exercise.getName())) {
                exercises.add(exercise);
            }
        }
        return exercises;
    }
*/

    private ArrayList<Workout> generateFullBodyWorkouts(ArrayList<Exercise> allExercises, int numOfAvailableDays) {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        exercises.add(generateCompound("Chest", allExercises));
        exercises.add(generateCompound("Back", allExercises));
        exercises.add(generateCompound("Legs", allExercises));
        exercises.add(generateCompound("Shoulders", allExercises));

        workouts.add(new Workout(exercises));

        //Add a second workout if there are 2 available days
        if(numOfAvailableDays == 2){
            workouts.add(new Workout(exercises));
        }
        
        return workouts;
    }

    private ArrayList<Workout> generateUpperLowerWorkouts(ArrayList<Exercise> allExercises, int numOfAvailableDays) {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        //Upper Body Workout
        exercises.add(generateCompound("Chest", allExercises));
        exercises.add(generateCompound("Back", allExercises));
        exercises.add(generateCompound("Shoulders", allExercises));
        exercises.add(generateAccessory("Back", allExercises));
        exercises.add(generateAccessory("Bicep", allExercises));
        exercises.add(generateAccessory("Tricep", allExercises));

        workouts.add(new Workout(exercises));

        //Lower Body Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs", allExercises));
        exercises.add(generateAccessory("Quads", allExercises));
        exercises.add(generateAccessory("Hamstrings", allExercises));
        exercises.add(generateAccessory("Glutes", allExercises));
        exercises.add(generateAccessory("Calves", allExercises));

        workouts.add(new Workout(exercises));

        //Repeat if there are 4 available days
        if(numOfAvailableDays == 4){
            exercises = new ArrayList<>();

            //Upper Body Workout
            exercises.add(generateCompound("Chest", allExercises));
            exercises.add(generateCompound("Back", allExercises));
            exercises.add(generateCompound("Shoulders", allExercises));
            exercises.add(generateAccessory("Back", allExercises));
            exercises.add(generateAccessory("Bicep", allExercises));
            exercises.add(generateAccessory("Tricep", allExercises));

            workouts.add(new Workout(exercises));

            //Lower Body Workout
            exercises = new ArrayList<>();

            exercises.add(generateCompound("Legs", allExercises));
            exercises.add(generateAccessory("Quads", allExercises));
            exercises.add(generateAccessory("Hamstrings", allExercises));
            exercises.add(generateAccessory("Glutes", allExercises));
            exercises.add(generateAccessory("Calves", allExercises));

            workouts.add(new Workout(exercises));

        }

        return workouts;
    }


    private ArrayList<Workout> generatePushPullLegsWorkouts(ArrayList<Exercise> allExercises, int numOfAvailableDays) {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        //Push Workout
        exercises.add(generateCompound("Chest", allExercises));
        exercises.add(generateCompound("Shoulders", allExercises));
        exercises.add(generateAccessory("Tricep", allExercises));
        exercises.add(generateAccessory("Tricep", allExercises));
        exercises.add(generateAccessory("Chest", allExercises)); 

        workouts.add(new Workout(exercises));
        //Pull Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Back", allExercises));
        exercises.add(generateAccessory("Back", allExercises));
        exercises.add(generateAccessory("Bicep", allExercises));
        exercises.add(generateAccessory("Shoulders", allExercises));


        workouts.add(new Workout(exercises));
        //Legs Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs", allExercises));
        exercises.add(generateAccessory("Quads", allExercises));
        exercises.add(generateAccessory("Hamstrings", allExercises));
        exercises.add(generateAccessory("Glutes", allExercises));
        exercises.add(generateAccessory("Calves", allExercises));

        workouts.add(new Workout(exercises));

        //Repeat if there are 6 available days
        if(numOfAvailableDays == 6){
            exercises = new ArrayList<>();

            //Push Workout
            exercises.add(generateCompound("Chest", allExercises));
            exercises.add(generateCompound("Shoulders", allExercises));
            exercises.add(generateAccessory("Tricep", allExercises));
            exercises.add(generateAccessory("Tricep", allExercises));
            exercises.add(generateAccessory("Chest", allExercises)); 

            workouts.add(new Workout(exercises));
            //Pull Workout
            exercises = new ArrayList<>();

            exercises.add(generateCompound("Back", allExercises));
            exercises.add(generateAccessory("Back", allExercises));
            exercises.add(generateAccessory("Bicep", allExercises));
            exercises.add(generateAccessory("Shoulders", allExercises));

            workouts.add(new Workout(exercises));
            //Legs Workout
            exercises = new ArrayList<>();

            exercises.add(generateCompound("Legs", allExercises));
            exercises.add(generateAccessory("Quads", allExercises));
            exercises.add(generateAccessory("Hamstrings", allExercises));
            exercises.add(generateAccessory("Glutes", allExercises));
            exercises.add(generateAccessory("Calves", allExercises));
        }

        return workouts;
    }

    private ArrayList<Workout> generatePushPullLegsUpperLowerWorkouts(ArrayList<Exercise> allExercises, int numOfAvailableDays) {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        //Push Workout
        exercises.add(generateCompound("Chest", allExercises));
        exercises.add(generateCompound("Shoulders", allExercises));
        exercises.add(generateAccessory("Tricep", allExercises));
        exercises.add(generateAccessory("Tricep", allExercises));
        exercises.add(generateAccessory("Chest", allExercises)); 

        workouts.add(new Workout(exercises));
        //Pull Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Back", allExercises));
        exercises.add(generateAccessory("Back", allExercises));
        exercises.add(generateAccessory("Bicep", allExercises));
        exercises.add(generateAccessory("Shoulders", allExercises));


        workouts.add(new Workout(exercises));
        //Legs Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs", allExercises));
        exercises.add(generateAccessory("Quads", allExercises));
        exercises.add(generateAccessory("Hamstrings", allExercises));
        exercises.add(generateAccessory("Glutes", allExercises));
        exercises.add(generateAccessory("Calves", allExercises));

        workouts.add(new Workout(exercises));

        //Upper Body Workout
        exercises.add(generateCompound("Chest", allExercises));
        exercises.add(generateCompound("Back", allExercises));
        exercises.add(generateCompound("Shoulders", allExercises));
        exercises.add(generateAccessory("Back", allExercises));
        exercises.add(generateAccessory("Bicep", allExercises));
        exercises.add(generateAccessory("Tricep", allExercises));

        workouts.add(new Workout(exercises));

        //Lower Body Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs", allExercises));
        exercises.add(generateAccessory("Quads", allExercises));
        exercises.add(generateAccessory("Hamstrings", allExercises));
        exercises.add(generateAccessory("Glutes", allExercises));
        exercises.add(generateAccessory("Calves", allExercises));

        workouts.add(new Workout(exercises));
        return workouts;
    }

    private Exercise generateAccessory(String muscleGroup , ArrayList<Exercise> allExercises) {
        //TODO - Make sure exercise is not already in the workout


        //Create a list of all exercises that target the muscle group
        ArrayList<Exercise> muscleGroupExercises = new ArrayList<>();

        for(Exercise exercise : allExercises){
            if(exercise.getMuscleGroup().equals(muscleGroup) && exercise.getType().equals("Accessory")){
                muscleGroupExercises.add(exercise);
            }
        }

        //Pick a random exercise from the list
        int randomIndex = (int) (Math.random() * muscleGroupExercises.size());
        return muscleGroupExercises.get(randomIndex);

    }

    private Exercise generateCompound(String muscleGroup , ArrayList<Exercise> allExercises) {

        //Create a list of all exercises that target the muscle group
        ArrayList<Exercise> muscleGroupExercises = new ArrayList<>();

        for(Exercise exercise : allExercises){
            if(exercise.getMuscleGroup().equals(muscleGroup) && exercise.getType().equals("Compound")){
                muscleGroupExercises.add(exercise);
            }
        }

        //Pick a random exercise from the list
        int randomIndex = (int) (Math.random() * muscleGroupExercises.size());
        return muscleGroupExercises.get(randomIndex);
    }
}