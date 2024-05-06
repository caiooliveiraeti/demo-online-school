package br.eti.caiooliveira.onlineschool.model;

import org.springframework.data.annotation.Id;

public record Student(@Id Long id, String name) {
    public static Student of(String name) {
        return new Student(null, name);
    }
}
