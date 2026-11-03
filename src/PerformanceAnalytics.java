import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceAnalytics {
    public static List<Student> topStudents(List<Student> students, int limit) {
        return students.stream()
                .sorted(Comparator.comparingDouble((Student s) -> s.calculateGPA()).reversed()
                        .thenComparing(Student::getName, String.CASE_INSENSITIVE_ORDER))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static List<Student> filterByPerformance(List<Student> students, String tag) {
        return students.stream()
                .filter(s -> s.getPerformanceTag().equalsIgnoreCase(tag))
                .sorted(Comparator.comparingDouble((Student s) -> s.calculateGPA()).reversed())
                .collect(Collectors.toList());
    }
}
