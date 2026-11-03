public abstract class Report {
    private final String generatedFor;

    protected Report(String generatedFor) {
        this.generatedFor = generatedFor;
    }

    public String getGeneratedFor() { return generatedFor; }

    public abstract String generateReport();
}
