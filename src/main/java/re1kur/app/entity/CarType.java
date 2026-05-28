package re1kur.app.entity;

public enum CarType {
    SEDAN("Sedan"),
    SUV("SUV"),
    COUPE("Coupe"),
    HATCHBACK("Hatchback");

    private final String label;

    CarType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
