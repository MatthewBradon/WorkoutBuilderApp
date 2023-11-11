import java.util.ArrayList;

public class Workout {
    private String name;
    private String description;
    private ArrayList<Exercise> exercises;

    // Constructor
    public Workout(String name, String description) {
        this.name = name;
        this.description = description;
        this.exercises = new ArrayList<>();
    }

    // Getter and Setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter methods for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
