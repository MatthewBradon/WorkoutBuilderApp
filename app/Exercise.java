public class Exercise {
    private String name;
    private int rpe; // Assuming RPE is an integer value
    private int reps;

    // Constructor
    public Exercise(String name, int rpe, int reps) {
        this.name = name;
        this.rpe = rpe;
        this.reps = reps;
    }

    // Getter and Setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter methods for RPE
    public int getRpe() {
        return rpe;
    }

    public void setRpe(int rpe) {
        this.rpe = rpe;
    }

    // Getter and Setter methods for reps
    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    // You can add more methods as needed for your application
}
