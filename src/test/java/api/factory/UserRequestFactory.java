package api.factory;

import com.github.javafaker.Faker;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.util.ApplicationMode;
import eu.senla.components.util.Gender;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UserRequestFactory {

    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Faker RU = new Faker(new Locale("ru"));
    private static final Faker EN = new Faker(Locale.ENGLISH);

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
                .citizenBirthDate(isoDate(birthDate(18, 80)))
                .citizenNumberOfPassport(passport())
                .citizenGender(Gender.MALE)
                .citizenAddress(address)
                .dateOfMarriage(isoDate(futureDate(1, 60)))
                .newLastName(surname)
                .anotherPersonLastName(surname())
                .anotherPersonFirstName(firstname())
                .anotherPersonMiddleName(femaleMiddlename())
                .birthOfAnotherPerson(isoDate(birthDate(18, 80)))
                .anotherPersonPassport(passport())

                .build();
    }

    public static UserRequest birth() {
        String surname = surname();
        return UserRequest.builder()
                .mode(ApplicationMode.BIRTH)
                .personalLastName(surname)
                .personalFirstName(firstname())
                .personalMiddleName(middlename())
                .personalPhoneNumber(phone())
                .personalNumberOfPassport(passport())
                .personalAddress(address())
                .citizenLastName(surname)
                .citizenFirstName(firstname())
                .citizenMiddleName(middlename())
                .citizenBirthDate(isoDate(pastDate(0, 1)))
                .citizenNumberOfPassport(passport())
                .citizenGender(Gender.MALE)
                .citizenAddress(address())
                .birthPlace(shortText(50))
                .birthMother(shortText(20))
                .birthFather(shortText(20))
                .birthGrandma(shortText(20))
                .birthGrandpa(shortText(20))
                .build();
    }

    public static UserRequest death() {
        String surname = surname();
        return UserRequest.builder()
                .mode(ApplicationMode.DEATH)
                .personalLastName(surname)
                .personalFirstName(firstname())
                .personalMiddleName(middlename())
                .personalPhoneNumber(phone())
                .personalNumberOfPassport(passport())
                .personalAddress(address())
                .citizenLastName(surname)
                .citizenFirstName(firstname())
                .citizenMiddleName(middlename())
                .citizenBirthDate(isoDate(pastDate(60, 90)))
                .citizenNumberOfPassport(passport())
                .citizenGender(Gender.MALE)
                .citizenAddress(address())
                .deathDateOfDeath(isoDate(pastDate(0, 1)))
                .deathPlaceOfDeath(shortText(50))
                .build();
    }

    private static String surname() {
        return truncate(RU.name().lastName(), 100);
    }

    private static String firstname() {
        return truncate(RU.name().firstName(), 100);
    }

    private static String middlename() {
        return truncate(RU.name().nameWithMiddle().split("\\s+")[2], 100);
    }

    private static String femaleMiddlename() {
        String male = middlename();
        return truncate(male.replaceAll("ич$", "на").replaceAll("вич$", "вна"), 20);
    }

    private static String address() {
        return truncate("г. " + RU.address().cityName()
                + ", ул. " + RU.address().streetName()
                + ", д. " + RU.address().buildingNumber(), 50);
    }

    private static String phone() {
        return EN.numerify("###########");
    }

    private static String passport() {
        return truncate(EN.letterify("??").toUpperCase() + EN.numerify("######"), 8);
    }

    private static String shortText(int maxLength) {
        return truncate(EN.lorem().word() + " " + EN.lorem().word(), maxLength);
    }

    private static Date birthDate(int minAge, int maxAge) {
        return RU.date().birthday(minAge, maxAge);
    }

    private static Date pastDate(int minYearsAgo, int maxYearsAgo) {
        return RU.date().past(maxYearsAgo, TimeUnit.DAYS);
    }

    private static Date futureDate(int minYearsAhead, int maxYearsAhead) {
        return RU.date().future(maxYearsAhead * 365, TimeUnit.DAYS);
    }

    private static String isoDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(ISO_DATE);
    }

    private static String truncate(String s, int max) {
        return s == null ? null : (s.length() <= max ? s : s.substring(0, max));
    }
}