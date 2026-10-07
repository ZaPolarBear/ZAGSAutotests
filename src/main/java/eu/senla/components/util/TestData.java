package eu.senla.components.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestData {

    public static final String USERNAME = System.getenv("APP_USERNAME");
    public static final String PASSWORD = System.getenv("APP_PASSWORD");
    public static final String TARGET_URL = "https://regoffice.senla.eu/";

    public static final String VALID_SURNAME = "Иванов";
    public static final String VALID_FIRSTNAME = "Иван";
    public static final String VALID_MIDDLENAME = "Иванович";
    public static final String VALID_PHONE_NUMBER = "1723727";
    public static final String VALID_DATE = "15092026";
    public static final String VALID_API_DATE = "2026-09-15";
    public static final String VALID_GENDER = "Муж";
    public static final String VALID_PASSPORT = "PS1234";
    public static final String VALID_ADDRESS = "ул. Иванович д. 3 кв. 7";
    public static final String VALID_PERSON_ADDRESS = "г. Минск, ул. Ленина, д. 1";
    public static final String VALID_CITIZEN_ADDRESS = "г. Минск, ул. Ленина, д. 1";

    public static final String MARRIAGE_PARTNER_MIDDLENAME = "Ивановна";

    public static final String BIRTH_PLACE = "г. Минск";
    public static final String MOTHER = "Иванова Мария";
    public static final String FATHER = "Иванов Пётр";
    public static final String GRANDMOTHER = "Иванова Ольга";
    public static final String GRANDFATHER = "Иванов Сергей";

    public static final String DEATH_DATE = "01012026";
    public static final String DEATH_PLACE = "г. Минск";

    public static final String ADMIN_SURNAME    = "Админов";
    public static final String ADMIN_FIRSTNAME  = "Админ";
    public static final String ADMIN_MIDDLENAME = "Админович";
    public static final String ADMIN_PHONE      = "12345678901";
    public static final String ADMIN_PASSPORT   = "AD123456";
    public static final String ADMIN_BIRTH_DATE = "01011990";
}