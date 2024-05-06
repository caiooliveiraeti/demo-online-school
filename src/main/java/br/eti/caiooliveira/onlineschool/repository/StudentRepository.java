package br.eti.caiooliveira.onlineschool.repository;


import br.eti.caiooliveira.onlineschool.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    Iterable<Student> findAllByName(String name);

}