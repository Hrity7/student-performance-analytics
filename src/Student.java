import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String branch;
    private int semester;
    private final List<Marks> marksList = new ArrayList<>();

    public Student(String id, String name, String email, String branch, int semester) {
        super(id, name, email);
        this.branch = branch;
        this.semester = semester;
    }

    public String getBranch() { return branch; }
    public int getSemester() { return semester; }
    public List<Marks> getMarksList() { return marksList; }

    public void setBranch(String branch) { this.branch = branch; }
    public void setSemester(int semester) { this.semester = semester; }

    public void addMarks(Marks marks) {
        marksList.removeIf(m -> m.getCourse().getCode().equalsIgnoreCase(marks.getCourse().getCode()));
        marksList.add(marks);
    }

    public double calculateGPA() {
        if (marksList.isEmpty()) return 0.0;
        double weighted = 0.0, credits = 0.0;
        for (Marks m : marksList) {
            weighted += m.getGradePoint() * m.getCourse().getCredits();
            credits += m.getCourse().getCredits();
        }
        return credits == 0 ? 0.0 : weighted / credits;
    }

    // Overloaded GPA method required by the OOTS design.
    public double calculateGPA(boolean includeFailedCourses) {
        if (marksList.isEmpty()) return 0.0;
        double weighted = 0.0, credits = 0.0;
        for (Marks m : marksList) {
            if (!includeFailedCourses && m.getGrade().equals("F")) continue;
            weighted += m.getGradePoint() * m.getCourse().getCredits();
            credits += m.getCourse().getCredits();
        }
        return credits == 0 ? 0.0 : weighted / credits;
    }

    public double averageMarks() {
        if (marksList.isEmpty()) return 0.0;
        return marksList.stream().mapToDouble(Marks::getScore).average().orElse(0.0);
    }

    public String getPerformanceTag() {
        double gpa = calculateGPA();
        if (gpa >= 9) return "Excellent";
        if (gpa >= 7) return "Good";
        if (gpa >= 5) return "Average";
        return "Needs Improvement";
    }

    public String getGradeSummary() {
        if (marksList.isEmpty()) return "No marks entered";
        return marksList.stream()
                .map(m -> m.getCourse().getCode() + ": " + m.getGrade())
                .reduce((a, b) -> a + ", " + b).orElse("");
    }

    @Override
    public String getRole() { return "Student"; }
}
