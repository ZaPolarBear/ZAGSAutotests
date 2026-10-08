package api.factory;

import com.github.javafaker.Faker;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.util.ApplicationMode;
import eu.senla.components.util.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class UserRequestFactory {

    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Faker RU = new Faker(new Locale("ru"));
    private static final Faker EN = new Faker(Locale.ENGLISH);
    public static final String INPUT_DATE_FORMAT = "ddMMyyyy";

    private UserRequestFactory() {
    }

    public static UserRequest marriage() {
        String surname = surname();
        String firstname = firstname();
        String middlename = middlename();
        String address = address();

        return UserRequest.builder()
                .mode(ApplicationMode.MARRIAGE)
                .personalLastName(surname)
                .personalFirstName(firstname)
                .personalMiddleName(middlename)
                .personalPhoneNumber(phone())
                .personalNumberOfPassport(passport())
                .personalAddress(address)
                .citizenLastName(surname)
                .citizenFirstName(firstname)
                .citizenMiddleName(middlename)
                .citizenBirthDate(isoDate(birthDate()))
                .citizenNumberOfPassport(passport())
                .citizenGender(Gender.MALE)
                .citizenAddress(address)
                .dateOfMarriage(isoDate(futureDate()))
                .newLastName(surname)
                .anotherPersonLastName(surname())
                .anotherPersonFirstName(firstname())
                .anotherPersonMiddleName(femaleMiddlename())
                .birthOfAnotherPerson(isoDate(birthDate()))
                .anotherPersonPassport(passport())

                .build();
    }

    public static String surname() {
        return truncate(RU.name().lastName(), 100);
    }

    public static String firstname() {
        return truncate(RU.name().firstName(), 100);
    }

    public static String middlename() {
        return truncate(RU.name().nameWithMiddle().split("\\s+")[2], 100);
    }

    public static String femaleMiddlename() {
        String male = middlename();
        return truncate(male.replaceAll("вич$", "вна").replaceAll("ич$", "на"), 20);
    }

    public static String address() {
        return truncate("г. " + RU.address().cityName()
                + ", ул. " + RU.address().streetName()
                + ", д. " + RU.address().buildingNumber(), 50);
    }

    public static String phone() {
        return EN.numerify("###########");
    }

    public static String passport() {
        return truncate(EN.letterify("??").toUpperCase() + EN.numerify("######"), 8);
    }

    public static LocalDate birthDate() {
        return LocalDate.now().minusYears(ThreadLocalRandom.current().nextInt(18, 80));
    }

    public static LocalDate pastDate() {
        return LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 90));
    }

    public static LocalDate futureDate() {
        return LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 90));
    }

    public static String inputDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT));
    }

    private static String isoDate(LocalDate date) {
        return date.format(ISO_DATE);
    }

    private static String truncate(String s, int max) {
        return s == null ? null : (s.length() <= max ? s : s.substring(0, max));
    }
}