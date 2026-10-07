package eu.senla.components.data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public record ApplicationRow(WebElement root) {

    private static final By CELLS = By.cssSelector("td.MuiTableCell-root");

    private List<String> texts() {
        return root.findElements(CELLS).stream()
                .map(e -> e.getText().trim().replaceAll("\\s+", " "))
                .toList();
    }

    private String cell(int i) {
        List<String> t = texts();
        return i < t.size() ? t.get(i) : "";
    }

    public String number() {
        return cell(0);
    }

    public String applicant() {
        return cell(1);
    }

    public String type() {
        return cell(2);
    }

    public String time() {
        return cell(3);
    }

    public String status() {
        return cell(4);
    }

    public int idAsInt() {
        String applicant = applicant();
        if (applicant.matches("\\d+")) {
            return Integer.parseInt(applicant);
        }
        throw new IllegalStateException(
                "В колонке «Заявитель» не число: '" + applicant + "', вся строка: " + texts());
    }

    public boolean isMarriageCertificate() {
        return type().contains("Получение свидетельства о браке");
    }
}