public class UndergraduateStudent extends Student {
    private int year;

    public UndergraduateStudent(String id, String name, String email, String branch, int semester, int year) {
        super(id, name, email, branch, semester);
        this.year = year;
    }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    @Override
    public String getRole() { return "Undergraduate Student"; }
}
