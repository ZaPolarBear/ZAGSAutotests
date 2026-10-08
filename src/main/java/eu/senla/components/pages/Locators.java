package eu.senla.components.pages;

import org.openqa.selenium.By;

public final class Locators {

    private Locators() {}

    public static By byLabel(String labelPrefix) {
        return By.xpath(
                "//input[@id=//label[starts-with(normalize-space(.), '"
                        + labelPrefix + "')]/@for]");
    }

    public static By byExactLabel(String labelText) {
        String safe = labelText.replace("'", "&apos;");
        return By.xpath(
                "//input[@id=//label[" +
                        "normalize-space(.)='" + safe + "' or " +
                        "normalize-space(.)='" + safe + " *'" +
                        "]/@for]");
    }

    public static By byPlaceholder(String placeholderFragment) {
        return By.cssSelector("input[placeholder*='" + placeholderFragment + "']");
    }

    public static By nextButton() {
        return By.xpath("//button[contains(normalize-space(.), 'Далее')]");
    }

    public static By endButton() {
        return By.xpath("//button[contains(normalize-space(.), 'Завершить')]");
    }
}