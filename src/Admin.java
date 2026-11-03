import java.util.List;

public class Admin extends Person {
    public Admin(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRole() { return "Admin"; }

    public String generateAdminReport(List<Student> students) {
        double avg = students.stream().mapToDouble(Student::calculateGPA).average().orElse(0);
        return "Admin Report - " + getName() + "\n" +
                "Total records: " + students.size() + "\n" +
                String.format("Overall Average GPA: %.2f%n", avg) +
                "Data status: " + (students.isEmpty() ? "EMPTY" : "ACTIVE");
    }
}
