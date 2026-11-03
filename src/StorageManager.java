import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private final Path file;

    public StorageManager(Path file) { this.file = file; }

    public void save(List<Student> students) throws IOException {
        if (file.getParent() != null) Files.createDirectories(file.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("id|name|email|branch|semester|year|marks");
            writer.newLine();
            for (Student s : students) {
                String marks = s.getMarksList().stream().map(Marks::toStorageString).reduce((a,b) -> a + ";" + b).orElse("");
                writer.write(escape(s.getId()) + "|" + escape(s.getName()) + "|" + escape(s.getEmail()) + "|" +
                        escape(s.getBranch()) + "|" + s.getSemester() + "|" +
                        (s instanceof UndergraduateStudent ? ((UndergraduateStudent)s).getYear() : 0) + "|" + marks);
                writer.newLine();
            }
        }
    }

    public List<Student> load() throws IOException {
        List<Student> students = new ArrayList<>();
        if (!Files.exists(file)) return students;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                try {
                    String[] p = line.split("\\|", -1);
                    if (p.length < 7) continue;
                    Student s = new UndergraduateStudent(unescape(p[0]), unescape(p[1]), unescape(p[2]), unescape(p[3]), Integer.parseInt(p[4]), Integer.parseInt(p[5]));
                    if (!p[6].isBlank()) {
                        for (String mark : p[6].split(";")) {
                            String[] m = mark.split("~", -1);
                            if (m.length != 4) continue;
                            Course c = new Course(unescape(m[0]), unescape(m[1]), Double.parseDouble(m[2]));
                            s.addMarks(new Marks(c, Double.parseDouble(m[3])));
                        }
                    }
                    students.add(s);
                } catch (RuntimeException ignored) { /* malformed record: skip it safely */ }
            }
        }
        return students;
    }

    private String escape(String value) { return value.replace("|", "/").replace(";", ",").replace("~", "-"); }
    private String unescape(String value) { return value; }
}
