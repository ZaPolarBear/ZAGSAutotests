package eu.senla.components.enums;

public enum ApplicationStatus {

    APPROVED("Одобрена",         "8, 142, 8"),   // #088e08
    REJECTED("Отклонена",        "195, 49, 23"), // #c33117
    PENDING("На рассмотрении",   "27, 126, 175"); // #1b7eaf

    private final String text;
    private final String rgb;

    ApplicationStatus(String text, String rgb) {
        this.text = text;
        this.rgb = rgb;
    }

    public String text() { return text; }
    public String rgb()  { return rgb; }

    public boolean matchesText(String actual)  { return text.equalsIgnoreCase(actual.trim()); }
    public boolean matchesColor(String actual) { return actual != null && actual.contains(rgb); }
}