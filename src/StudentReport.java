public class StudentReport extends Report {
    private final Student student;

    public StudentReport(Student student) {
        super(student.getName());
        this.student = student;
    }

    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student Report\n");
        sb.append("==============================\n");
        sb.append("ID: ").append(student.getId()).append("\n");
        sb.append("Name: ").append(student.getName()).append("\n");
        sb.append("Branch/Semester: ").append(student.getBranch()).append(" / ").append(student.getSemester()).append("\n\n");
        sb.append("SUBJECT-WISE MARKS\n");
        sb.append("------------------------------\n");
        if (student.getMarksList().isEmpty()) {
            sb.append("No marks entered.\n");
        } else {
            for (Marks m : student.getMarksList()) {
                sb.append(String.format("%-6s %-38s Marks: %6.2f  Grade: %-2s%n",
                        m.getCourse().getCode(), m.getCourse().getName(), m.getScore(), m.getGrade()));
            }
        }
        sb.append("\n");
        sb.append(String.format("GPA: %.2f%n", student.calculateGPA()));
        sb.append(String.format("Average Marks: %.2f%n", student.averageMarks()));
        sb.append("Performance: ").append(student.getPerformanceTag()).append("\n");
        return sb.toString();
    }
}
