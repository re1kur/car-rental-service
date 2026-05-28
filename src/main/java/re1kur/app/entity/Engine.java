package re1kur.app.entity;

public enum Engine {
    ELECTRIC("Electric"),
    V6("V6"),
    HYBRID("Hybrid"),
    INLINE_4("Inline-4");

    private final String label;

    Engine(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
