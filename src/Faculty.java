import java.util.List;

public class Faculty extends Person {
    private String department;

    public Faculty(String id, String name, String email, String department) {
        super(id, name, email);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String getRole() { return "Faculty"; }

    public String generateClassReport(List<Student> students) {
        if (students.isEmpty()) return "No student records available.";
        double average = students.stream().mapToDouble(Student::calculateGPA).average().orElse(0);
        long needsHelp = students.stream().filter(s -> s.calculateGPA() < 5).count();
        return "Faculty Report - " + getName() + "\n" +
                "Department: " + department + "\n" +
                "Students: " + students.size() + "\n" +
                String.format("Class Average GPA: %.2f%n", average) +
                "Students needing help: " + needsHelp;
    }
}
