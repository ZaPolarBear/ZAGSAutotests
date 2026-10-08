package db;

import api.UserApiClient;
import api.factory.UserRequestFactory;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.util.Gender;
import eu.senla.components.util.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Types;
import java.util.UUID;

import static eu.senla.components.util.TestData.env;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseSchemaTest {

    private static final String URL = env("PG_ADDRESS");
    private static final String USER = env("PG_USER");
    private static final String PASSWORD = env("PG_PASSWORD");

    private Connection connection;
    private final UserApiClient api = new UserApiClient(TestData.USERNAME, TestData.PASSWORD);

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.close();
        }
    }

    @Test
    @DisplayName("Заявка, созданная через API, попадает в applicants/citizens/applications с ожидаемыми полями")
    void applicationCreatedViaApiIsPersisted() throws SQLException {

        UserRequest request = UserRequestFactory.marriage();
        long applicationId = api.sendUserRequest(request).getData().getApplicationId();
        String passport = request.getPersonalNumberOfPassport();
        String surname = request.getCitizenLastName();

        long citizenId;
        long applicantId;
        String kind;
        String status;

        try (
                PreparedStatement ps = connection.prepareStatement("""
                        SELECT citizenid, applicantid, kindofapplication, statusofapplication
                        FROM reg_office.applications
                        WHERE applicationid = ?""")
        ) {
            ps.setLong(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Заявка id=" + applicationId + " не найдена в БД");
                citizenId = rs.getLong("citizenid");
                applicantId = rs.getLong("applicantid");
                kind = rs.getString("kindofapplication");
                status = rs.getString("statusofapplication");
            }
        }

        long finalCitizenId = citizenId;
        long finalApplicantId = applicantId;

        assertAll(
                "Заявка в БД",
                () -> assertEquals("Получение свидетельства о браке", kind),
                () -> assertEquals("under consideration", status.toLowerCase()),
                () -> assertTrue(finalCitizenId > 0, "citizenid должен быть > 0"),
                () -> assertTrue(finalApplicantId > 0, "applicantid должен быть > 0")
                 );

        try (
                PreparedStatement ps = connection.prepareStatement("""
                        SELECT surname, name, middlename, passportnumber, dateofbirth, gender
                        FROM reg_office.citizens WHERE citizenid = ?""")
        ) {
            ps.setLong(1, citizenId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "citizen id=" + citizenId + " не найден");
                assertAll(
                        "citizens",
                        () -> assertEquals(surname, rs.getString("surname")),
                        () -> assertEquals(request.getPersonalFirstName(), rs.getString("name")),
                        () -> assertEquals(request.getCitizenMiddleName(), rs.getString("middlename")),
                        () -> assertEquals(request.getCitizenNumberOfPassport(), rs.getString("passportnumber")),
                        () -> assertEquals(request.getCitizenBirthDate(), rs.getDate("dateofbirth").toString()),
                        () -> assertEquals(Gender.MALE, rs.getString("gender").toLowerCase())
                         );
            }
        }

        try (
                PreparedStatement ps = connection.prepareStatement("""
                        SELECT surname, passportnumber
                        FROM reg_office.applicants WHERE applicantid = ?""")
        ) {
            ps.setLong(1, applicantId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "applicant id=" + applicantId + " не найден");
                assertAll(
                        "applicants",
                        () -> assertEquals(surname, rs.getString("surname")),
                        () -> assertEquals(passport, rs.getString("passportnumber"))
                         );
            }
        }
    }

    @Test
    @DisplayName("Схема отклоняет NOT NULL, FK и уникальные нарушения")
    void schemaRejectsInvalidInserts() {
        assertAll("Ограничения схемы",

                () -> assertSchemaRejects("NULL surname",
                        () -> insertApplicant(null, uniquePassport())),

                () -> assertSchemaRejects("Несуществующий citizenid/applicantid", this::insertApplication),

                () -> assertSchemaRejects("Дубликат passportnumber", () -> {
                    String dup = uniquePassport();
                    insertApplicant(UserRequestFactory.surname(), dup);
                    insertApplicant(UserRequestFactory.surname(), dup);
                })
                 );
    }

    private void assertSchemaRejects(String label, ThrowingRunnable block) throws SQLException {
        Savepoint sp = connection.setSavepoint(label);
        try {
            assertThrows(SQLException.class, block::run, label);
        } finally {
            connection.rollback(sp);
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws SQLException;
    }

    private static String uniquePassport() {
        return "P-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void insertApplicant(String surname, String passport) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("""
            INSERT INTO reg_office.applicants
            (surname, name, middlename, passportnumber, phonenumber, registration_address)
            VALUES (?, ?, ?, ?, ?, ?)""")) {
            if (surname == null) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, surname);
            }
            ps.setString(2, UserRequestFactory.firstname());
            ps.setString(3, UserRequestFactory.middlename());
            ps.setString(4, passport);
            ps.setString(5, UserRequestFactory.phone());
            ps.setString(6, UserRequestFactory.address());
            ps.executeUpdate();
        }
    }

    private void insertApplication() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("""
            INSERT INTO reg_office.applications
            (citizenid, applicantid, kindofapplication, statusofapplication)
            VALUES (?, ?, ?, ?)""")) {
            ps.setLong(1, -1L);
            ps.setLong(2, -1L);
            ps.setString(3, "Получение свидетельства о браке");
            ps.setString(4, "under consideration");
            ps.executeUpdate();
        }
    }
}
