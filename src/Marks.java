public class Marks {
    private final Course course;
    private double score;

    public Marks(Course course, double score) {
        this.course = course;
        setScore(score);
    }

    public Course getCourse() { return course; }
    public double getScore() { return score; }

    public void setScore(double score) {
        if (!Double.isFinite(score) || score < 0 || score > 100) throw new IllegalArgumentException("Marks must be a finite number between 0 and 100.");
        this.score = score;
    }

    public String getGrade() {
        if (score >= 90) return "A+";
        if (score >= 80) return "A";
        if (score >= 70) return "B";
        if (score >= 60) return "C";
        if (score >= 50) return "D";
        if (score >= 40) return "E";
        return "F";
    }

    public double getGradePoint() {
        if (score >= 90) return 10.0;
        if (score >= 80) return 9.0;
        if (score >= 70) return 8.0;
        if (score >= 60) return 7.0;
        if (score >= 50) return 6.0;
        if (score >= 40) return 5.0;
        return 0.0;
    }

    // Local-file representation for one course record.
    public String toStorageString() {
        return course.getCode() + "~" + course.getName() + "~" + course.getCredits() + "~" + score;
    }
}
