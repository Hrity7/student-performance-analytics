import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Clean minimalist Swing UI for the Student Academic Performance Analytics System.
 * The UI is intentionally dependency-free so the project can run directly in VS Code.
 */
public class Main {
    private static final List<Student> students = new ArrayList<>();
    private static final StorageManager STORAGE = new StorageManager(resolveDataFile());
    private static final List<Course> COURSES = List.of(
            new Course("OOTS", "Object Oriented Techniques using Java", 4),
            new Course("DSA", "Data Structures and Algorithms", 4),
            new Course("DBMS", "Database Management Systems", 3),
            new Course("OS", "Operating Systems", 3),
            new Course("DM", "Discrete Mathematics", 3)
    );

    private static JFrame frame;
    private static JTable studentTable;
    private static DefaultTableModel tableModel;
    private static TableRowSorter<DefaultTableModel> tableSorter;
    private static JTextField searchField;
    private static JPanel analyticsPanel;
    private static JLabel totalValue, averageValue, topperValue, helpValue, statusLabel;
    private static JTextArea detailsArea;

    private static final Color BG = new Color(247, 248, 250);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(30, 35, 43);
    private static final Color MUTED = new Color(105, 113, 126);
    private static final Color BORDER = new Color(225, 229, 235);
    private static final Color ACCENT = new Color(67, 97, 238);
    private static final Color ACCENT_DARK = new Color(49, 72, 190);
    private static final Color GREEN = new Color(27, 140, 88);
    private static final Color RED = new Color(196, 61, 61);

    public static void main(String[] args) {
        loadData(false);
        SwingUtilities.invokeLater(Main::createAndShowUI);
    }

    private static void createAndShowUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        frame = new JFrame("Student Academic Performance Analytics System");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1050, 700));
        frame.setSize(1180, 760);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(buildRoot());
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                saveData();
                frame.dispose();
            }
        });
        frame.setVisible(true);
        refreshDashboard();
    }

    private static JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);
        return root;
    }

    private static JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        JLabel title = label("Academic Performance", 23, Font.BOLD, TEXT);
        JLabel subtitle = label("Student analytics dashboard", 13, Font.PLAIN, MUTED);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(3));
        titleBox.add(subtitle);

        JButton save = button("Save Records", ACCENT);
        save.setForeground(Color.BLACK);
        save.addActionListener(e -> saveData());
        header.add(titleBox, BorderLayout.WEST);
        header.add(save, BorderLayout.EAST);
        return header;
    }

    private static JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(18, 18));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel sidebar = buildSidebar();
        body.add(sidebar, BorderLayout.WEST);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        content.add(buildStats(), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabs.addTab("Student Records", buildMainPanel());
        analyticsPanel = buildAnalyticsPanel();
        tabs.addTab("Charts & Analytics", analyticsPanel);
        content.add(tabs, BorderLayout.CENTER);
        body.add(content, BorderLayout.CENTER);
        return body;
    }

    private static JPanel buildSidebar() {
        JPanel side = card(new BorderLayout());
        side.setPreferredSize(new Dimension(190, 0));
        side.setBorder(new EmptyBorder(16, 12, 16, 12));

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        JLabel section = label("MENU", 11, Font.BOLD, MUTED);
        section.setBorder(new EmptyBorder(0, 8, 10, 8));
        menu.add(section);
        menu.add(navButton("Dashboard", () -> refreshDashboard()));
        menu.add(navButton("Add Student", () -> addStudentDialog()));
        menu.add(navButton("Enter Marks", () -> updateMarksDialog()));
        menu.add(navButton("Student Report", () -> studentReportDialog()));
        menu.add(navButton("Top Performers", () -> topperDialog()));
        menu.add(navButton("Performance Filter", () -> filterDialog()));
        menu.add(navButton("Admin Summary", () -> showTextReport(new Admin("ADM001", "Admin User", "admin@college.edu").generateAdminReport(students))));
        menu.add(Box.createVerticalGlue());
        menu.add(navButton("Reload Local Data", () -> { loadData(true); refreshDashboard(); }));

        side.add(menu, BorderLayout.CENTER);
        return side;
    }

    private static JButton navButton(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setForeground(TEXT);
        b.setBackground(Color.WHITE);
        b.setBorder(new EmptyBorder(11, 10, 11, 10));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> action.run());
        return b;
    }

    private static JPanel buildStats() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        totalValue = new JLabel("0");
        averageValue = new JLabel("0.00");
        topperValue = new JLabel("—");
        helpValue = new JLabel("0");
        stats.add(statCard("TOTAL STUDENTS", totalValue, ACCENT));
        stats.add(statCard("AVERAGE GPA", averageValue, GREEN));
        stats.add(statCard("TOP GPA", topperValue, ACCENT));
        stats.add(statCard("NEED SUPPORT", helpValue, RED));
        return stats;
    }

    private static JPanel statCard(String caption, JLabel value, Color accent) {
        JPanel p = card(new BorderLayout());
        p.setBorder(new EmptyBorder(15, 17, 15, 17));
        JLabel c = label(caption, 10, Font.BOLD, MUTED);
        value.setFont(new Font("SansSerif", Font.BOLD, 25));
        value.setForeground(accent);
        p.add(c, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private static JPanel buildMainPanel() {
        JPanel panel = card(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(17, 17, 17, 17));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel heading = label("Student Records", 17, Font.BOLD, TEXT);
        statusLabel = label("Local storage active", 12, Font.PLAIN, MUTED);
        top.add(heading, BorderLayout.WEST);
        top.add(statusLabel, BorderLayout.EAST);
        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        searchField = field();
        searchField.setToolTipText("Search by student ID, name, branch, semester, GPA, or performance");
        searchRow.add(label("Search records", 12, Font.BOLD, TEXT), BorderLayout.WEST);
        searchRow.add(searchField, BorderLayout.CENTER);
        JButton clearSearch = new JButton("Clear");
        clearSearch.addActionListener(e -> searchField.setText(""));
        searchRow.add(clearSearch, BorderLayout.EAST);
        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.setOpaque(false);
        north.add(top, BorderLayout.NORTH);
        north.add(searchRow, BorderLayout.SOUTH);
        panel.add(north, BorderLayout.NORTH);

        String[] cols = {"ID", "Student", "Branch", "Sem", "GPA", "Performance"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(tableModel);
        tableSorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(tableSorter);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateFilter() {
                String query = searchField.getText().trim();
                tableSorter.setRowFilter(query.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(query)));
            }
            @Override public void insertUpdate(DocumentEvent e) { updateFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { updateFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { updateFilter(); }
        });
        studentTable.setRowHeight(34);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        studentTable.setForeground(TEXT);
        studentTable.setGridColor(new Color(238, 240, 243));
        studentTable.setShowVerticalLines(false);
        studentTable.setSelectionBackground(new Color(235, 239, 255));
        studentTable.setSelectionForeground(TEXT);
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        studentTable.getTableHeader().setForeground(MUTED);
        studentTable.getTableHeader().setBackground(new Color(249, 250, 252));
        studentTable.getTableHeader().setPreferredSize(new Dimension(0, 34));
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(95);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(190);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(75);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(145);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.CENTER);
        studentTable.getColumnModel().getColumn(3).setCellRenderer(right);
        studentTable.getColumnModel().getColumn(4).setCellRenderer(right);

        studentTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) showSelectedStudent();
            }
        });

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setForeground(MUTED);
        detailsArea.setBackground(new Color(250, 251, 253));
        detailsArea.setBorder(new EmptyBorder(10, 12, 10, 12));
        detailsArea.setRows(4);
        panel.add(detailsArea, BorderLayout.SOUTH);
        return panel;
    }

    private static void refreshDashboard() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        students.stream().sorted(Comparator.comparing(Student::getId)).forEach(s ->
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getBranch(), s.getSemester(), String.format("%.2f", s.calculateGPA()), s.getPerformanceTag()}));

        totalValue.setText(String.valueOf(students.size()));
        double avg = students.stream().mapToDouble(Student::calculateGPA).average().orElse(0);
        averageValue.setText(String.format("%.2f", avg));
        double top = students.stream().mapToDouble(Student::calculateGPA).max().orElse(0);
        topperValue.setText(students.isEmpty() ? "—" : String.format("%.2f", top));
        long needs = students.stream().filter(s -> s.calculateGPA() < 5).count();
        helpValue.setText(String.valueOf(needs));
        statusLabel.setText("" + students.size() + " records • saved locally");
        detailsArea.setText("Tip: double-click a student row to open their detailed academic report. Use Search records to filter the table.");
        if (analyticsPanel != null) analyticsPanel.repaint();
    }

    private static JPanel buildAnalyticsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 14));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(new BarChartPanel(true));
        panel.add(new BarChartPanel(false));
        return panel;
    }

    /** Lightweight charts drawn with Java2D; no external chart dependency is required. */
    private static class BarChartPanel extends JPanel {
        private final boolean gpaChart;
        BarChartPanel(boolean gpaChart) {
            this.gpaChart = gpaChart;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(8, 8, 8, 8)));
            setPreferredSize(new Dimension(600, 210));
        }
        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g.setColor(TEXT); g.setFont(new Font("SansSerif", Font.BOLD, 15));
            g.drawString(gpaChart ? "GPA Distribution" : "Average Marks by Subject", 18, 24);
            Map<String, Double> values = gpaChart ? gpaDistribution() : subjectAverages();
            if (values.isEmpty()) {
                g.setColor(MUTED); g.setFont(new Font("SansSerif", Font.PLAIN, 13));
                g.drawString("Enter student marks to see analytics here.", 20, 58);
                g.dispose(); return;
            }
            int left = 42, right = 18, top = 42, bottom = 42;
            int chartW = Math.max(1, w - left - right), chartH = Math.max(1, h - top - bottom);
            double max = gpaChart ? Math.max(1, values.values().stream().mapToDouble(Double::doubleValue).max().orElse(1))
                                  : 100.0;
            g.setColor(BORDER); g.drawLine(left, top + chartH, left + chartW, top + chartH);
            int count = values.size();
            int slot = Math.max(1, chartW / count);
            int barW = Math.max(12, Math.min(54, slot - 14));
            int i = 0;
            for (Map.Entry<String, Double> entry : values.entrySet()) {
                int x = left + i * slot + (slot - barW) / 2;
                int barH = (int) Math.round((entry.getValue() / max) * (chartH - 8));
                int y = top + chartH - barH;
                g.setColor(gpaChart ? ACCENT : GREEN);
                g.fillRoundRect(x, y, barW, barH, 8, 8);
                g.setColor(TEXT); g.setFont(new Font("SansSerif", Font.PLAIN, 10));
                String valueLabel = gpaChart ? String.valueOf(entry.getValue().intValue()) : String.format("%.0f", entry.getValue());
                FontMetrics fm = g.getFontMetrics();
                g.drawString(valueLabel, x + Math.max(0, (barW - fm.stringWidth(valueLabel)) / 2), Math.max(top + 10, y - 4));
                String label = entry.getKey();
                if (label.length() > 12) label = label.substring(0, 11) + "…";
                fm = g.getFontMetrics();
                g.drawString(label, x + Math.max(0, (barW - fm.stringWidth(label)) / 2), top + chartH + 17);
                i++;
            }
            g.dispose();
        }
    }

    private static Map<String, Double> gpaDistribution() {
        Map<String, Double> result = new LinkedHashMap<>();
        result.put("0–4.99", 0.0); result.put("5–6.99", 0.0); result.put("7–8.99", 0.0); result.put("9–10", 0.0);
        for (Student student : students) {
            double gpa = student.calculateGPA();
            String key = gpa < 5 ? "0–4.99" : gpa < 7 ? "5–6.99" : gpa < 9 ? "7–8.99" : "9–10";
            result.put(key, result.get(key) + 1.0);
        }
        return result;
    }

    private static Map<String, Double> subjectAverages() {
        Map<String, double[]> totals = new TreeMap<>();
        for (Student student : students) for (Marks mark : student.getMarksList()) {
            String code = mark.getCourse().getCode();
            double[] aggregate = totals.computeIfAbsent(code, k -> new double[2]);
            aggregate[0] += mark.getScore(); aggregate[1]++;
        }
        Map<String, Double> result = new LinkedHashMap<>();
        totals.entrySet().stream().sorted((a, b) -> Double.compare(b.getValue()[0] / b.getValue()[1], a.getValue()[0] / a.getValue()[1]))
                .limit(8).forEach(e -> result.put(e.getKey(), e.getValue()[0] / e.getValue()[1]));
        return result;
    }

    private static void addStudentDialog() {
        JTextField id = field();
        JTextField name = field();
        JTextField email = field();
        JComboBox<String> branch = new JComboBox<>(new String[]{"CSE", "IT", "ECE", "EEE", "ME", "CE", "AI & DS", "AI & ML"});
        JComboBox<Integer> semester = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        JPanel p = formPanel(new String[]{"Student ID", "Name", "Email", "Branch", "Semester"}, new JComponent[]{id, name, email, branch, semester});
        if (showForm("Add Student", p)) {
            try {
                String sid = id.getText().trim();
                if (sid.isEmpty() || findStudent(sid) != null) throw new IllegalArgumentException("Student ID is blank or already exists.");
                int sem = (Integer) semester.getSelectedItem();
                String studentName = name.getText().trim();
                String studentEmail = email.getText().trim();
                if (studentName.isEmpty()) throw new IllegalArgumentException("Student name cannot be blank.");
                if (!studentName.matches("[\\p{L}][\\p{L} .'-]{1,79}")) throw new IllegalArgumentException("Enter a valid name (letters, spaces, apostrophes, periods, or hyphens). Use at least 2 characters.");
                if (!studentEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) throw new IllegalArgumentException("Enter a valid email address, for example student@college.edu.");
                students.add(new UndergraduateStudent(sid, studentName, studentEmail, (String) branch.getSelectedItem(), sem, (sem + 1) / 2));
                saveData();
                refreshDashboard();
                info("Student added successfully.");
            } catch (Exception ex) { error(ex.getMessage()); }
        }
    }

    private static List<Course> coursesForSemester(int semester) {
        // Semester 4 preserves the original project subjects and sample-data course codes.
        switch (semester) {
            case 1: return List.of(new Course("MATH1", "Engineering Mathematics I", 4), new Course("PHY1", "Engineering Physics", 3), new Course("CHEM1", "Engineering Chemistry", 3), new Course("BEE1", "Basic Electrical Engineering", 3), new Course("PROG1", "Programming Fundamentals", 4));
            case 2: return List.of(new Course("MATH2", "Engineering Mathematics II", 4), new Course("PHY2", "Applied Physics", 3), new Course("ENG2", "Communication Skills", 2), new Course("CHEM2", "Applied Chemistry", 3), new Course("EC2", "Basic Electronics", 3));
            case 3: return List.of(new Course("DSA3", "Data Structures and Algorithms", 4), new Course("OOP3", "Object Oriented Programming", 4), new Course("MATH3", "Discrete Mathematics", 3), new Course("DIG3", "Digital Logic", 3), new Course("COA3", "Computer Organization", 3));
            case 4: return List.of(new Course("OOTS", "Object Oriented Techniques using Java", 4), new Course("DSA", "Data Structures and Algorithms", 4), new Course("DBMS", "Database Management Systems", 3), new Course("OS", "Operating Systems", 3), new Course("DM", "Discrete Mathematics", 3));
            case 5: return List.of(new Course("CN5", "Computer Networks", 4), new Course("SE5", "Software Engineering", 3), new Course("ADA5", "Analysis and Design of Algorithms", 4), new Course("TOC5", "Theory of Computation", 3), new Course("WEB5", "Web Technologies", 3));
            case 6: return List.of(new Course("AI6", "Artificial Intelligence", 4), new Course("ML6", "Machine Learning", 4), new Course("CC6", "Cloud Computing", 3), new Course("IS6", "Information Security", 3), new Course("DBA6", "Database Administration", 3));
            case 7: return List.of(new Course("DL7", "Deep Learning", 4), new Course("IOT7", "Internet of Things", 3), new Course("BD7", "Big Data Analytics", 4), new Course("SE7", "Software Testing", 3), new Course("EL7", "Professional Elective I", 3));
            default: return List.of(new Course("PROJ8", "Major Project", 6), new Course("INT8", "Internship", 4), new Course("ETH8", "Professional Ethics", 2), new Course("EL8", "Professional Elective II", 3), new Course("SEM8", "Seminar", 2));
        }
    }

    private static void updateMarksDialog() {
        if (students.isEmpty()) { error("No students are available."); return; }
        JComboBox<String> studentBox = new JComboBox<>(students.stream().map(s -> s.getId() + " — " + s.getName()).toArray(String[]::new));
        JComboBox<Integer> semesterBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(10, 8, 4, 8));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 7, 6, 7); g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        form.add(label("Select student, semester, and enter marks", 14, Font.BOLD, TEXT), g);
        g.gridwidth = 1; g.gridy = 1; g.gridx = 0; g.weightx = 0;
        form.add(label("Student", 12, Font.BOLD, TEXT), g); g.gridx = 1; g.weightx = 1; form.add(studentBox, g);
        g.gridy = 2; g.gridx = 0; g.weightx = 0; form.add(label("Semester", 12, Font.BOLD, TEXT), g);
        g.gridx = 1; g.weightx = 1; form.add(semesterBox, g);
        JPanel subjectsPanel = new JPanel(new GridBagLayout()); subjectsPanel.setBackground(Color.WHITE);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2; g.weightx = 1; form.add(subjectsPanel, g);
        Map<String, JTextField> scoreFields = new LinkedHashMap<>();
        Runnable rebuildSubjects = () -> {
            subjectsPanel.removeAll(); scoreFields.clear();
            List<Course> courses = coursesForSemester((Integer) semesterBox.getSelectedItem());
            GridBagConstraints sg = new GridBagConstraints(); sg.insets = new Insets(5, 7, 5, 7); sg.anchor = GridBagConstraints.WEST; sg.fill = GridBagConstraints.HORIZONTAL;
            sg.gridx = 0; sg.gridy = 0; sg.weightx = 0;
            subjectsPanel.add(label("Subject", 12, Font.BOLD, TEXT), sg);
            sg.gridx = 1; sg.weightx = 1; subjectsPanel.add(label("Marks (0–100)", 12, Font.BOLD, TEXT), sg);
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i); JTextField score = field(); score.setToolTipText("Enter marks from 0 to 100"); scoreFields.put(c.getCode(), score);
                sg.gridy = i + 1; sg.gridx = 0; sg.weightx = 0; subjectsPanel.add(label(c.getCode() + " — " + c.getName(), 12, Font.PLAIN, TEXT), sg);
                sg.gridx = 1; sg.weightx = 1; subjectsPanel.add(score, sg);
            }
            subjectsPanel.revalidate(); subjectsPanel.repaint(); form.revalidate(); form.repaint();
        };
        semesterBox.addActionListener(e -> rebuildSubjects.run());
        rebuildSubjects.run();
        JScrollPane scroll = new JScrollPane(form); scroll.setBorder(BorderFactory.createEmptyBorder()); scroll.setPreferredSize(new Dimension(650, 430));
        int result = JOptionPane.showConfirmDialog(frame, scroll, "Enter / Update Marks", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Student student = students.get(studentBox.getSelectedIndex());
                List<Course> courses = coursesForSemester((Integer) semesterBox.getSelectedItem());
                int entered = 0;
                for (Course course : courses) {
                    String raw = scoreFields.get(course.getCode()).getText().trim();
                    if (raw.isEmpty()) continue;
                    double value = Double.parseDouble(raw);
                    if (!Double.isFinite(value) || value < 0 || value > 100) throw new IllegalArgumentException(course.getCode() + " marks must be a number between 0 and 100.");
                    student.addMarks(new Marks(course, value)); entered++;
                }
                if (entered == 0) throw new IllegalArgumentException("Enter marks for at least one subject.");
                saveData(); refreshDashboard();
                info("Marks saved for " + entered + " subject(s).\nCurrent overall GPA: " + String.format("%.2f", student.calculateGPA()));
            } catch (NumberFormatException ex) { error("Please enter valid numeric marks for each subject."); }
            catch (Exception ex) { error(ex.getMessage()); }
        }
    }

    private static void studentReportDialog() {
        if (students.isEmpty()) { error("No students are available."); return; }
        JComboBox<String> box = new JComboBox<>(students.stream().map(s -> s.getId() + " — " + s.getName()).toArray(String[]::new));
        JPanel p = formPanel(new String[]{"Student"}, new JComponent[]{box});
        if (showForm("Student Report", p)) showTextReport(new StudentReport(students.get(box.getSelectedIndex())).generateReport());
    }

    private static void topperDialog() {
        if (students.isEmpty()) { error("No student records available."); return; }
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(Math.min(5, students.size()), 1, students.size(), 1));
        JPanel p = formPanel(new String[]{"Number of toppers"}, new JComponent[]{spinner});
        if (showForm("Top Performers", p)) {
            int n = (Integer) spinner.getValue();
            List<Student> top = PerformanceAnalytics.topStudents(students, n);
            StringBuilder sb = new StringBuilder("TOP PERFORMERS\n\n");
            for (int i = 0; i < top.size(); i++) sb.append(String.format("%d. %-24s GPA %.2f%n", i + 1, top.get(i).getName(), top.get(i).calculateGPA()));
            showTextReport(sb.toString());
        }
    }

    private static void filterDialog() {
        JComboBox<String> box = new JComboBox<>(new String[]{"Excellent", "Good", "Average", "Needs Improvement"});
        JPanel p = formPanel(new String[]{"Performance level"}, new JComponent[]{box});
        if (showForm("Performance Filter", p)) {
            String tag = (String) box.getSelectedItem();
            List<Student> result = PerformanceAnalytics.filterByPerformance(students, tag);
            StringBuilder sb = new StringBuilder(tag.toUpperCase() + " — " + result.size() + " STUDENT(S)\n\n");
            result.forEach(s -> sb.append(String.format("%-12s %-24s GPA %.2f%n", s.getId(), s.getName(), s.calculateGPA())));
            showTextReport(sb.toString());
        }
    }

    private static void showSelectedStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) return;
        int modelRow = studentTable.convertRowIndexToModel(row);
        String id = String.valueOf(tableModel.getValueAt(modelRow, 0));
        Student s = findStudent(id);
        if (s != null) showTextReport(new StudentReport(s).generateReport());
    }

    private static void showTextReport(String text) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setForeground(TEXT);
        area.setBackground(Color.WHITE);
        area.setBorder(new EmptyBorder(18, 18, 18, 18));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(590, 350));
        JOptionPane.showMessageDialog(frame, scroll, "Report", JOptionPane.PLAIN_MESSAGE);
    }

    private static boolean showForm(String title, JPanel panel) {
        return JOptionPane.showConfirmDialog(frame, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }

    private static JPanel formPanel(String[] labels, JComponent[] fields) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10, 8, 4, 8));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 7, 7, 7);
        g.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0; g.fill = GridBagConstraints.NONE;
            p.add(label(labels[i], 12, Font.BOLD, TEXT), g);
            g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
            p.add(fields[i], g);
        }
        return p;
    }

    private static JTextField field() {
        JTextField f = new JTextField(22);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(7, 8, 7, 8)));
        return f;
    }

    private static JButton button(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(9, 15, 9, 15));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(ACCENT_DARK); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(color); }
        });
        return b;
    }

    private static JPanel card(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(1, 1, 1, 1)));
        return p;
    }

    private static JLabel label(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", style, size));
        l.setForeground(color);
        return l;
    }

    private static void info(String message) { JOptionPane.showMessageDialog(frame, message, "Done", JOptionPane.INFORMATION_MESSAGE); }
    private static void error(String message) { JOptionPane.showMessageDialog(frame, message, "Please check", JOptionPane.ERROR_MESSAGE); }

    private static void saveData() {
        try {
            STORAGE.save(students);
            if (statusLabel != null) statusLabel.setText(students.size() + " records • saved locally");
        } catch (Exception e) { error("Save failed: " + e.getMessage()); }
    }

    private static void loadData(boolean notify) {
        try {
            students.clear();
            students.addAll(STORAGE.load());
            if (notify && frame != null) info("Reloaded " + students.size() + " records from the local students file.");
        } catch (Exception e) {
            if (notify) error("Load failed: " + e.getMessage());
        }
    }

    private static Path resolveDataFile() {
        Path current = Path.of("").toAbsolutePath().normalize();
        Path direct = current.resolve("data").resolve("students.txt");
        if (Files.exists(direct) || Files.isDirectory(current.resolve("data"))) return direct;

        Path parent = current.getParent();
        if (parent != null) {
            Path parentData = parent.resolve("data").resolve("students.txt");
            if (Files.exists(parentData) || Files.isDirectory(parent.resolve("data"))) return parentData;
        }
        return direct;
    }

    private static Student findStudent(String id) {
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
}
