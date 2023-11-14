package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import java.util.HashMap;


public class CreateWorkout extends AppCompatActivity {

    private Button stepCounterBtn, workoutsBtn, createBtn;
    private TextInputEditText workoutName, workoutDescription;
    private Spinner spinner;
    private final ArrayList<CheckBox> daysOfWeek = new ArrayList<>();
    private HashMap<String, ArrayList<Exercise>> muscleGroupMap = new HashMap<>();


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

        workoutsBtn.setOnClickListener(v -> {
                finish();
        });

        stepCounterBtn.setOnClickListener(v -> {
                Intent intent = new Intent(CreateWorkout.this, StepCounter.class);
                startActivity(intent);
        });


        createBtn.setOnClickListener(v -> {
                
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

                //Calculate the program split and type of workouts
                String trainingType = spinner.getSelectedItem().toString();
                int accessoryRepCount = trainingType.equals("Strength") ? 8 : 12;
                int compoundRepCount = trainingType.equals("Strength") ? 5 : 8;

                //Get the muscle group map
                muscleGroupMap = getMuscleGroupMap(this ,accessoryRepCount, compoundRepCount, "exercises.json");

                //Generate Program
                Program program = generateProgram(numOfAvailableDays, maxConsecutiveRestDays);

                //Display the program
                System.out.println(program.toString());


                Intent data = new Intent();

                try {
                    System.out.println(program.toJSON().toString());
                    data.putExtra("program", program.toJSON().toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }

                setResult(RESULT_OK, data);

                finish();
        });
    }

    private Program generateProgram(int numOfAvailableDays, int maxConsecutiveRestDays) {

        ProgramType programType = ProgramType.FULL_BODY;
        
        ArrayList<Workout> workouts;

 

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



        //Generate the workout based on the number of available days
        switch (programType) {
            case FULL_BODY:
                System.out.println("FULL BODY");
                workouts = generateFullBodyWorkouts(numOfAvailableDays);
                break;
            case UPPER_LOWER:
                System.out.println("UPPER LOWER");
                workouts = generateUpperLowerWorkouts(numOfAvailableDays);
                break;
            case PUSH_PULL_LEGS:
                System.out.println("PUSH PULL LEGS");
                workouts = generatePushPullLegsWorkouts(numOfAvailableDays);
                break;
            case PUSH_PULL_LEGS_UPPER_LOWER:
                System.out.println("PUSH PULL LEGS UPPER LOWER");
                workouts = generatePushPullLegsUpperLowerWorkouts();
                break;
            default:
                return null;
        }

        //Create the program
        return new Program(workouts, workoutName.getText().toString(), workoutDescription.getText().toString());

    }

    private ArrayList<Workout> generateFullBodyWorkouts(int numOfAvailableDays) {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        exercises.add(generateCompound("Chest"));
        exercises.add(generateCompound("Back"));
        exercises.add(generateCompound("Legs"));
        exercises.add(generateCompound("Shoulders"));

        workouts.add(new Workout(exercises, "Full Body"));

        //Add a second workout if there are 2 available days
        if(numOfAvailableDays == 2){
            workouts.add(new Workout(exercises, "Full Body 2"));
        }
        
        return workouts;
    }

    private ArrayList<Workout> generateUpperLowerWorkouts(int numOfAvailableDays) {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        //Upper Body Workout
        exercises.add(generateCompound("Chest"));
        exercises.add(generateCompound("Back"));
        exercises.add(generateCompound("Shoulders"));
        exercises.add(generateAccessory("Back"));
        exercises.add(generateAccessory("Biceps"));
        exercises.add(generateAccessory("Triceps"));

        workouts.add(new Workout(exercises, "Upper"));

        //Lower Body Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs"));
        exercises.add(generateAccessory("Quads"));
        exercises.add(generateAccessory("Hamstrings"));
        exercises.add(generateAccessory("Glutes"));
        exercises.add(generateAccessory("Calves"));

        workouts.add(new Workout(exercises, "Lower"));

        //Repeat if there are 4 available days
        if(numOfAvailableDays == 4){
            exercises = new ArrayList<>();

            //Upper Body Workout
            exercises.add(generateCompound("Chest"));
            exercises.add(generateCompound("Back"));
            exercises.add(generateCompound("Shoulders"));
            exercises.add(generateAccessory("Back"));
            exercises.add(generateAccessory("Biceps"));
            exercises.add(generateAccessory("Triceps"));

            workouts.add(new Workout(exercises, "Upper 2"));

            //Lower Body Workout
            exercises = new ArrayList<>();

            exercises.add(generateCompound("Legs"));
            exercises.add(generateAccessory("Quads"));
            exercises.add(generateAccessory("Hamstrings"));
            exercises.add(generateAccessory("Glutes"));
            exercises.add(generateAccessory("Calves"));

            workouts.add(new Workout(exercises, "Lower 2"));

        }

        return workouts;
    }


    private ArrayList<Workout> generatePushPullLegsWorkouts(int numOfAvailableDays) {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        //Push Workout
        exercises.add(generateCompound("Chest"));
        exercises.add(generateCompound("Shoulders"));
        exercises.add(generateAccessory("Triceps"));
        exercises.add(generateAccessory("Triceps"));
        exercises.add(generateAccessory("Chest"));


        workouts.add(new Workout(exercises, "Push"));

        //Pull Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Back"));
        exercises.add(generateAccessory("Back"));
        exercises.add(generateAccessory("Biceps"));
        exercises.add(generateAccessory("Shoulders"));


        workouts.add(new Workout(exercises, "Pull"));
        //Legs Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs"));
        exercises.add(generateAccessory("Quads"));
        exercises.add(generateAccessory("Hamstrings"));
        exercises.add(generateAccessory("Glutes"));
        exercises.add(generateAccessory("Calves"));

        workouts.add(new Workout(exercises, "Legs"));

        //Repeat if there are 6 available days
        if(numOfAvailableDays == 6){
            exercises = new ArrayList<>();

            //Push Workout
            exercises.add(generateCompound("Chest"));
            exercises.add(generateCompound("Shoulders"));
            exercises.add(generateAccessory("Triceps"));
            exercises.add(generateAccessory("Triceps"));
            exercises.add(generateAccessory("Chest")); 

            workouts.add(new Workout(exercises, "Push 2"));
            //Pull Workout
            exercises = new ArrayList<>();

            exercises.add(generateCompound("Back"));
            exercises.add(generateAccessory("Back"));
            exercises.add(generateAccessory("Biceps"));
            exercises.add(generateAccessory("Shoulders"));

            workouts.add(new Workout(exercises, "Pull 2"));
            //Legs Workout
            exercises = new ArrayList<>();

            exercises.add(generateCompound("Legs"));
            exercises.add(generateAccessory("Quads"));
            exercises.add(generateAccessory("Hamstrings"));
            exercises.add(generateAccessory("Glutes"));
            exercises.add(generateAccessory("Calves"));

            workouts.add(new Workout(exercises, "Legs 2"));
        }

        return workouts;
    }

    private ArrayList<Workout> generatePushPullLegsUpperLowerWorkouts() {
        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        //Push Workout
        exercises.add(generateCompound("Chest"));
        exercises.add(generateCompound("Shoulders"));
        exercises.add(generateAccessory("Triceps"));
        exercises.add(generateAccessory("Triceps"));
        exercises.add(generateAccessory("Chest")); 

        workouts.add(new Workout(exercises, "Push"));
        //Pull Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Back"));
        exercises.add(generateAccessory("Back"));
        exercises.add(generateAccessory("Biceps"));
        exercises.add(generateAccessory("Shoulders"));


        workouts.add(new Workout(exercises, "Pull"));
        //Legs Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs"));
        exercises.add(generateAccessory("Quads"));
        exercises.add(generateAccessory("Hamstrings"));
        exercises.add(generateAccessory("Glutes"));
        exercises.add(generateAccessory("Calves"));

        workouts.add(new Workout(exercises, "Legs"));

        //Upper Body Workout
        exercises.add(generateCompound("Chest"));
        exercises.add(generateCompound("Back"));
        exercises.add(generateCompound("Shoulders"));
        exercises.add(generateAccessory("Back"));
        exercises.add(generateAccessory("Biceps"));
        exercises.add(generateAccessory("Triceps"));

        workouts.add(new Workout(exercises, "Upper"));

        //Lower Body Workout
        exercises = new ArrayList<>();

        exercises.add(generateCompound("Legs"));
        exercises.add(generateAccessory("Quads"));
        exercises.add(generateAccessory("Hamstrings"));
        exercises.add(generateAccessory("Glutes"));
        exercises.add(generateAccessory("Calves"));

        workouts.add(new Workout(exercises, "Lower"));
        return workouts;
    }

    private Exercise generateAccessory(String muscleGroup) {
        //TODO - Make sure exercise is not already in the workout

        ArrayList<Exercise> muscleGroupExercises = muscleGroupMap.get(muscleGroup);
        ArrayList<Exercise> accessoryExercises = new ArrayList<>();

        if(muscleGroupExercises == null || muscleGroupExercises.isEmpty()){
            return null;
        }

        for(Exercise exercise : muscleGroupExercises){
            if(exercise.getType().equals("Accessory")){
                accessoryExercises.add(exercise);   
            }
        }

        //Pick a random exercise from the list
        int randomIndex = (int) (Math.random() * accessoryExercises.size());
        return accessoryExercises.get(randomIndex);

    }

    private Exercise generateCompound(String muscleGroup) {

        //Create a list of all exercises that target the muscle group
        ArrayList<Exercise> muscleGroupExercises = muscleGroupMap.get(muscleGroup);
        ArrayList<Exercise> compoundExercises = new ArrayList<>();
        
        if(muscleGroupExercises == null || muscleGroupExercises.isEmpty()){
            return null;
        }

        for(Exercise exercise : muscleGroupExercises){
            if(exercise.getType().equals("Compound")){
                compoundExercises.add(exercise);   
            }
        }
       
        //Pick a random exercise from the list
        int randomIndex = (int) (Math.random() * compoundExercises.size());
        return compoundExercises.get(randomIndex);
    }
    


   
    public static HashMap<String, ArrayList<Exercise>> getMuscleGroupMap(Context context, int accessoryRepCount, int compoundRepCount, String filePath) {
        HashMap<String, ArrayList<Exercise>> muscleGroupMap = new HashMap<>();
        ArrayList<Exercise> allExercises = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open(filePath);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            try {
                // Parse the main JSON object
                JSONObject mainJsonObject = new JSONObject(json);

                // Extract the "exercises" JSONArray from the main JSON object
                JSONArray jsonArray = mainJsonObject.getJSONArray("exercises");

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

                // Separate exercises into muscle groups
                for (Exercise exercise : allExercises) {
                    // If the muscle group is not in the map, add it and add the exercise to the list
                    muscleGroupMap.computeIfAbsent(exercise.getMuscleGroup(), k -> new ArrayList<>()).add(exercise);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return muscleGroupMap;
    }

}