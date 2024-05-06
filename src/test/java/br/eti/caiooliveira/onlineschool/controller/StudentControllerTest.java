package br.eti.caiooliveira.onlineschool.controller;

import br.eti.caiooliveira.onlineschool.model.Student;
import br.eti.caiooliveira.onlineschool.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void shouldReturnAllStudentsWhenFindAll() {
        // Given
        Student student = new Student(1L, "Caio Oliveira");
        when(studentRepository.findAll()).thenReturn(singletonList(student));

        // When
        Iterable<Student> result = studentController.getAllStudents();

        // Then
        assertEquals(singletonList(student), result);
    }

    @Test
    public void shouldReturnStudentByIdWhenFindById() {
        // Given
        Student student = new Student(1L, "Caio Oliveira");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // When
        Student result = studentController.getStudentById(1L);

        // Then
        assertEquals(student, result);
    }

    @Test
    public void shouldCreateStudentWhenSave() {
        // Given
        Student student = new Student(1L, "Caio Oliveira");
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // When
        Student result = studentController.createStudent(student);

        // Then
        assertEquals(student, result);
    }

    @Test
    public void shouldUpdateStudentWhenSave() {
        // Given
        Student student = new Student(1L, "Caio");
        Student updatedStudent = new Student(1L, "Caio Oliveira");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        // When
        Student result = studentController.updateStudent(1L, updatedStudent);

        // Then
        assertEquals(updatedStudent, result);
    }

    @Test
    public void shouldDeleteStudentWhenDelete() {
        // Given
        Student student = new Student(1L, "Caio");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // When
        studentController.deleteStudent(1L);

        // Then
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    public void shouldReturnNotFoundWhenStudentDoesNotExistForGetById() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> studentController.getStudentById(1L));

        // Then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Student not found with id: 1", exception.getReason());
    }

    @Test
    public void shouldReturnNotFoundWhenStudentDoesNotExistForUpdate() {
        // Given
        Student updatedStudent = new Student(1L, "Caio Oliveira");
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> studentController.updateStudent(1L, updatedStudent));

        // Then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Student not found with id: 1", exception.getReason());
    }

    @Test
    public void shouldReturnNotFoundWhenStudentDoesNotExistForDelete() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> studentController.deleteStudent(1L));

        // Then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Student not found with id: 1", exception.getReason());
    }
}
