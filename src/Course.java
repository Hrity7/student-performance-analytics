public class Course {
    private final String code;
    private final String name;
    private final double credits;

    public Course(String code, String name, double credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getCredits() { return credits; }

    @Override
    public String toString() {
        return code + " - " + name + " (" + credits + " credits)";
    }
}
