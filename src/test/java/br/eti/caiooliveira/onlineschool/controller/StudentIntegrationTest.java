package br.eti.caiooliveira.onlineschool.controller;

import br.eti.caiooliveira.onlineschool.model.Student;
import br.eti.caiooliveira.onlineschool.repository.StudentRepository;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class StudentIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @LocalServerPort
    private int port;

    private SessionFilter sessionFilter;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        studentRepository.deleteAll(); // Clear any existing data before each test
        login();
    }

    private void login() {
        sessionFilter = new SessionFilter();
        RestAssured.given()
                .filter(sessionFilter)
                .formParam("username", "caio")
                .formParam("password", "123456")
                .when()
                .post("/login").then()
                .statusCode(302);
    }

    @Test
    @DisplayName("Given valid student data, when creating a student, then the student should be created")
    public void testCreateStudent() {
        String studentJson = "{ \"name\": \"Caio Oliveira\" }";

        given()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(studentJson)
                .when()
                .post("/students")
                .then()
                .statusCode(200)
                .body("name", equalTo("Caio Oliveira"));
    }

    @Test
    @DisplayName("Given existing students, when getting all students, then all students should be retrieved")
    public void testGetAllStudents() {
        studentRepository.save(Student.of("Caio Oliveira"));

        RestAssured.given()
                .filter(sessionFilter)
                .when()
                .get("/students")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("Caio Oliveira"));
    }

    @Test
    @DisplayName("Given an existing student, when getting the student by ID, then the student should be retrieved")
    public void testGetStudentById() {
        Student savedStudent = studentRepository.save(Student.of("Caio Oliveira"));

        given()
                .filter(sessionFilter)
                .pathParam("id", savedStudent.id())
                .when()
                .get("/students/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Caio Oliveira"));
    }

    @Test
    @DisplayName("Given an existing student, when updating the student, then the student should be updated")
    public void testUpdateStudent() {
        Student savedStudent = studentRepository.save(Student.of("Caio"));

        String updatedStudentJson = "{ \"name\": \"Caio Oliveira\" }";

        given()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(updatedStudentJson)
                .pathParam("id", savedStudent.id())
                .when()
                .put("/students/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Caio Oliveira"));
    }

    @Test
    @DisplayName("Given an existing student, when deleting the student, then the student should be deleted")
    public void testDeleteStudent() {
        Student savedStudent = studentRepository.save(Student.of("Caio Oliveira"));

        given()
                .filter(sessionFilter)
                .pathParam("id", savedStudent.id())
                .when()
                .delete("/students/{id}")
                .then()
                .statusCode(200);
    }
}
